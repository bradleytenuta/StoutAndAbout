package com.bradleytenuta.stoutandabout.data

import android.content.Context
import android.util.Log
import com.mapbox.geojson.FeatureCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/**
 * Global singleton store for Pub data.
 */
object PubDataStore {
    private const val TAG = "PubDataStore"
    private val _pubs = MutableStateFlow<List<Pub>>(emptyList())
    val pubs: StateFlow<List<Pub>> = _pubs.asStateFlow()

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
                    val loadedPubs = featureCollection.features()?.map { Pub(it) } ?: emptyList()
                    _pubs.value = loadedPubs
                    Log.d(TAG, "Successfully loaded ${loadedPubs.size} pubs.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading pubs GeoJSON", e)
            }
        }
    }
}
