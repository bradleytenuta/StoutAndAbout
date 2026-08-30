package com.bradleytenuta.stoutandabout.pages.map.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.bradleytenuta.stoutandabout.data.PubDataStore
import com.mapbox.geojson.Feature
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
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.Polygon as JtsPolygon

@OptIn(MapboxExperimental::class, MapboxDelicateApi::class)
@Composable
fun PubPolygonsEffect() {
    val pubs by PubDataStore.pubs.collectAsState()
    val sourceState = rememberGeoJsonSourceState()
    val geometryFactory = remember { GeometryFactory() }

    LaunchedEffect(pubs) {
        sourceState.data = GeoJSONData(pubs.map { pub ->
            val geometry = pub.feature.geometry()
            if (geometry is Polygon || geometry is MultiPolygon) {
                try {
                    val jtsGeom = when (geometry) {
                        is Polygon -> geometry.toJts(geometryFactory)
                        is MultiPolygon -> geometry.toJts(geometryFactory)
                        else -> null
                    }

                    // Buffer by ~1 meter in degrees (approx 0.00001)
                    val bufferedJts = jtsGeom?.buffer(0.00001)
                    val bufferedMapbox = bufferedJts?.toMapbox()

                    if (bufferedMapbox != null) {
                        Feature.fromGeometry(bufferedMapbox, pub.feature.properties(), pub.feature.id())
                    } else {
                        pub.feature
                    }
                } catch (_: Exception) {
                    pub.feature
                }
            } else {
                pub.feature
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

/**
 * Extension to convert Mapbox Polygon to JTS Polygon
 */
private fun Polygon.toJts(factory: GeometryFactory): JtsPolygon {
    val shell = factory.createLinearRing(this.coordinates()[0].map { Coordinate(it.longitude(), it.latitude()) }.toTypedArray())
    val holes = if (this.coordinates().size > 1) {
        this.coordinates().drop(1).map { ring ->
            factory.createLinearRing(ring.map { Coordinate(it.longitude(), it.latitude()) }.toTypedArray())
        }.toTypedArray()
    } else {
        emptyArray<LinearRing>()
    }
    return factory.createPolygon(shell, holes)
}

/**
 * Extension to convert Mapbox MultiPolygon to JTS MultiPolygon
 */
private fun MultiPolygon.toJts(factory: GeometryFactory): org.locationtech.jts.geom.MultiPolygon {
    val polygons = this.polygons().map { it.toJts(factory) }.toTypedArray()
    return factory.createMultiPolygon(polygons)
}

/**
 * Extension to convert JTS Geometry back to Mapbox Geometry
 */
private fun org.locationtech.jts.geom.Geometry.toMapbox(): com.mapbox.geojson.Geometry? {
    return when (this) {
        is JtsPolygon -> {
            val rings = mutableListOf<List<Point>>()
            rings.add(this.exteriorRing.coordinates.map { Point.fromLngLat(it.x, it.y) })
            for (i in 0 until this.numInteriorRing) {
                rings.add(this.getInteriorRingN(i).coordinates.map { Point.fromLngLat(it.x, it.y) })
            }
            Polygon.fromLngLats(rings)
        }
        is org.locationtech.jts.geom.MultiPolygon -> {
            val mapboxPolygons = mutableListOf<Polygon>()
            for (i in 0 until this.numGeometries) {
                val poly = this.getGeometryN(i) as JtsPolygon
                val rings = mutableListOf<List<Point>>()
                rings.add(poly.exteriorRing.coordinates.map { Point.fromLngLat(it.x, it.y) })
                for (j in 0 until poly.numInteriorRing) {
                    rings.add(poly.getInteriorRingN(j).coordinates.map { Point.fromLngLat(it.x, it.y) })
                }
                mapboxPolygons.add(Polygon.fromLngLats(rings))
            }
            MultiPolygon.fromPolygons(mapboxPolygons)
        }
        else -> null
    }
}
