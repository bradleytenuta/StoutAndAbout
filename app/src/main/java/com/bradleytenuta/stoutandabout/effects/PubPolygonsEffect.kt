package com.bradleytenuta.stoutandabout.effects

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.bradleytenuta.stoutandabout.PubDataStore
import com.bradleytenuta.stoutandabout.domain.Branding
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseBlack
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseWhite
import com.bradleytenuta.stoutandabout.util.toMapbox
import com.bradleytenuta.stoutandabout.util.toJts
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxDelicateApi
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.generated.FillExtrusionLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Geometry as JtsGeometry

@OptIn(MapboxExperimental::class, MapboxDelicateApi::class)
@Composable
fun PubPolygonsEffect() {
    val pubs by PubDataStore.pubs.collectAsState()
    val sourceState = rememberGeoJsonSourceState()
    val geometryFactory = remember { GeometryFactory() }
    val context = LocalContext.current
    
    val brandingIcons = remember(context) {
        Branding.entries.associateWith { branding ->
            context.assets.open(branding.iconPath).use {
                BitmapFactory.decodeStream(it)
            }
        }
    }

    var circleMarkers by remember { mutableStateOf<List<CircleAnnotationOptions>>(emptyList()) }
    var iconMarkers by remember { mutableStateOf<List<PointAnnotationOptions>>(emptyList()) }

    LaunchedEffect(pubs, brandingIcons) {
        withContext(Dispatchers.Default) {
            val processedFeatures = mutableListOf<Feature>()
            val tempCircleMarkers = mutableListOf<CircleAnnotationOptions>()
            val tempIconMarkers = mutableListOf<PointAnnotationOptions>()

            pubs.forEach { pub ->
                val geometry = pub.feature.geometry() ?: return@forEach
                
                // 1. Convert to JTS once
                val jtsGeom: JtsGeometry? = when (geometry) {
                    is Point -> geometry.toJts(geometryFactory)
                    is LineString -> geometry.toJts(geometryFactory)
                    is Polygon -> geometry.toJts(geometryFactory)
                    is MultiPolygon -> geometry.toJts(geometryFactory)
                    else -> null
                }

                if (jtsGeom == null) {
                    processedFeatures.add(pub.feature)
                    return@forEach
                }

                // 2. Process for Extrusion Feature
                try {
                    val bufferedJts = when (geometry) {
                        is Polygon, is MultiPolygon -> jtsGeom.buffer(0.00001)
                        is Point -> jtsGeom.buffer(0.00010)
                        is LineString -> {
                            // Replicate previous logic: use first coordinate as a point
                            geometry.coordinates().firstOrNull()?.toJts(geometryFactory)?.buffer(0.00010)
                        }
                        else -> null
                    }
                    val bufferedMapbox = bufferedJts?.toMapbox()
                    if (bufferedMapbox != null) {
                        processedFeatures.add(Feature.fromGeometry(bufferedMapbox, pub.feature.properties(), pub.feature.id()))
                    } else {
                        processedFeatures.add(pub.feature)
                    }
                } catch (_: Exception) {
                    processedFeatures.add(pub.feature)
                }

                // 3. Process for Markers
                try {
                    val centroid = jtsGeom.centroid
                    val markerPoint = Point.fromLngLat(centroid.x, centroid.y)

                    val brandEnum = Branding.entries.find { it.brandName == pub.brand } ?: Branding.DEFAULT
                    val iconBitmap = brandingIcons[brandEnum]

                    if (iconBitmap != null) {
                        tempIconMarkers.add(
                            PointAnnotationOptions()
                                .withPoint(markerPoint)
                                .withIconImage(iconBitmap)
                                .withIconSize(0.5)
                        )
                    }
                } catch (_: Exception) {
                    // Ignore marker if centroid fails
                }
            }

            // 4. Update states on main thread
            withContext(Dispatchers.Main) {
                sourceState.data = GeoJSONData(processedFeatures)
                circleMarkers = tempCircleMarkers
                iconMarkers = tempIconMarkers
                Log.d("PubPolygonsEffect", "Processed ${pubs.size} pubs: ${circleMarkers.size} circles, ${iconMarkers.size} icons")
            }
        }
    }

    FillExtrusionLayer(
        sourceState = sourceState,
        layerId = "pubs-extrusion-layer"
    ) {
        fillExtrusionColor = ColorValue(Color(0xFFD8BFD8))
        fillExtrusionHeight = DoubleValue(100.0)
        fillExtrusionOpacity = DoubleValue(0.9)
        fillExtrusionBase = DoubleValue(0.0)
    }

    CircleAnnotationGroup(
        annotations = circleMarkers,
        annotationConfig = null
    ) {
        maxZoom = 20.0
    }

    PointAnnotationGroup(
        annotations = iconMarkers,
        annotationConfig = null
    ) {
        maxZoom = 20.0
    }
}
