package com.bradleytenuta.stoutandabout.util

import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.Polygon as JtsPolygon

/**
 * Extension to convert Mapbox Point to JTS Point
 */
fun Point.toJts(factory: GeometryFactory): org.locationtech.jts.geom.Point {
    return factory.createPoint(Coordinate(this.longitude(), this.latitude()))
}

/**
 * Extension to convert Mapbox Polygon to JTS Polygon
 */
fun Polygon.toJts(factory: GeometryFactory): JtsPolygon {
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
fun MultiPolygon.toJts(factory: GeometryFactory): org.locationtech.jts.geom.MultiPolygon {
    val polygons = this.polygons().map { it.toJts(factory) }.toTypedArray()
    return factory.createMultiPolygon(polygons)
}

/**
 * Extension to convert JTS Geometry back to Mapbox Geometry
 */
fun org.locationtech.jts.geom.Geometry.toMapbox(): com.mapbox.geojson.Geometry? {
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
