package com.bradleytenuta.stoutandabout.data

import android.content.Context
import android.util.Log
import com.mapbox.geojson.FeatureCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/**
 * Global singleton store for Pub data.
 */
object PubDataStore {
    private const val TAG = "PubDataStore"
    private var _pubs = listOf<Pub>()
    val pubs: List<Pub> get() = _pubs

    /**
     * Initializes the store by reading the GeoJSON file from assets.
     * This should be called once at app startup.
     */
    suspend fun initialize(context: Context) {
        withContext(Dispatchers.IO) {
            try {
                context.assets.open("london-pubs.geojson").use { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    val featureCollection = FeatureCollection.fromJson(reader.readText())
                    _pubs = featureCollection.features()?.map { Pub(it) } ?: emptyList()
                    Log.d(TAG, "Successfully loaded ${_pubs.size} pubs.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading pubs GeoJSON", e)
            }
        }
    }
}
