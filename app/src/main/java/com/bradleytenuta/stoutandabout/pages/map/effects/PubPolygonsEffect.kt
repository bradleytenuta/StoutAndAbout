package com.bradleytenuta.stoutandabout.pages.map.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.bradleytenuta.stoutandabout.data.PubDataStore
import com.bradleytenuta.stoutandabout.util.toMapbox
import com.bradleytenuta.stoutandabout.util.toJts
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.LineString
import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxDelicateApi
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.generated.FillExtrusionLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import org.locationtech.jts.geom.GeometryFactory

@OptIn(MapboxExperimental::class, MapboxDelicateApi::class)
@Composable
fun PubPolygonsEffect() {
    val pubs by PubDataStore.pubs.collectAsState()
    val sourceState = rememberGeoJsonSourceState()
    val geometryFactory = remember { GeometryFactory() }

    LaunchedEffect(pubs) {
        sourceState.data = GeoJSONData(pubs.mapNotNull { pub ->
            val geometry = pub.feature.geometry()
            when (geometry) {
                is Polygon, is MultiPolygon -> {
                    processPolygon(geometry, pub.feature, geometryFactory)
                }
                is Point -> {
                    processPoint(geometry, pub.feature, geometryFactory)
                }
                is LineString -> {
                    val firstCoord = geometry.coordinates().firstOrNull()
                    if (firstCoord != null) {
                        processPoint(firstCoord, pub.feature, geometryFactory)
                    } else {
                        pub.feature
                    }
                }
                else -> {
                    pub.feature
                }
            }
        })
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
}

private fun processPolygon(geometry: Geometry, feature: Feature, factory: GeometryFactory): Feature {
    return try {
        val jtsGeom = when (geometry) {
            is Polygon -> geometry.toJts(factory)
            is MultiPolygon -> geometry.toJts(factory)
            else -> null
        }

        // Buffer by ~1 meter in degrees (approx 0.00001)
        val bufferedJts = jtsGeom?.buffer(0.00001)
        val bufferedMapbox = bufferedJts?.toMapbox()

        if (bufferedMapbox != null) {
            Feature.fromGeometry(bufferedMapbox, feature.properties(), feature.id())
        } else {
            feature
        }
    } catch (_: Exception) {
        feature
    }
}

private fun processPoint(geometry: Point, feature: Feature, factory: GeometryFactory): Feature {
    return try {
        val jtsPoint = geometry.toJts(factory)

        // 10m buffer in degrees.
        val bufferedJts = jtsPoint.buffer(0.00010)
        val bufferedMapbox = bufferedJts?.toMapbox()

        if (bufferedMapbox != null) {
            Feature.fromGeometry(bufferedMapbox, feature.properties(), feature.id())
        } else {
            feature
        }
    } catch (_: Exception) {
        feature
    }
}
