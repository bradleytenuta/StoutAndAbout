package com.bradleytenuta.stoutandabout.domain

import com.mapbox.geojson.Feature

/**
 * Represents a Pub entity, wrapping a Mapbox GeoJSON Feature.
 */
data class Pub(
    val feature: Feature
) {
    val id: String? = feature.id()
    val name: String? = feature.getProperty("name")?.asString
    val amenity: String? = feature.getProperty("amenity")?.asString
    val country: String? = feature.getProperty("addr:country")?.asString
    val city: String? = feature.getProperty("addr:city")?.asString
    val street: String? = feature.getProperty("addr:street")?.asString
    val houseNumber: String? = feature.getProperty("addr:housenumber")?.asString
    val postcode: String? = feature.getProperty("addr:postcode")?.asString 
        ?: feature.getProperty("postal_code")?.asString
    val brand: String? = feature.getProperty("brand")?.asString
    val brandWikidata: String? = feature.getProperty("brand:wikidata")?.asString
    val brandWikipedia: String? = feature.getProperty("brand:wikipedia")?.asString
    val brewery: String? = feature.getProperty("brewery")?.asString
    val building: String? = feature.getProperty("building")?.asString
    val checkDate: String? = feature.getProperty("check_date")?.asString
    val type: String? = feature.getProperty("type")?.asString
    val note: String? = feature.getProperty("note")?.asString
    val realAle: String? = feature.getProperty("real_ale")?.asString
    val toilets: String? = feature.getProperty("toilets")?.asString
    val toiletsAccess: String? = feature.getProperty("toilets:access")?.asString
    val wikidata: String? = feature.getProperty("wikidata")?.asString

    override fun toString(): String {
        return "Pub(name=$name, id=$id, postcode=$postcode)"
    }
}
