package com.bradleytenuta.stoutandabout.pages.map.effects

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.bradleytenuta.stoutandabout.data.PubDataStore
import com.bradleytenuta.stoutandabout.util.toJts
import com.mapbox.geojson.MultiPolygon
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import org.locationtech.jts.geom.GeometryFactory

@OptIn(MapboxExperimental::class)
@Composable
fun LocationVisitEffect() {
    val pubs by PubDataStore.pubs.collectAsState()
    val context = LocalContext.current
    val geometryFactory = remember { GeometryFactory() }
    
    // Track the current pub ID to avoid repeated toasts while staying inside
    var currentPubId by remember { mutableStateOf<String?>(null) }
    
    // Capture the MapView instance
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }
    MapEffect(Unit) { mapView ->
        mapViewInstance = mapView
    }

    val mapView = mapViewInstance ?: return

    DisposableEffect(mapView, pubs) {
        val listener = OnIndicatorPositionChangedListener { point ->
            val userJtsPoint = point.toJts(geometryFactory)
            
            val enteredPub = pubs.find { pub ->
                val geometry = pub.feature.geometry()
                when (geometry) {
                    is Polygon -> geometry.toJts(geometryFactory).contains(userJtsPoint)
                    is MultiPolygon -> geometry.toJts(geometryFactory).contains(userJtsPoint)
                    else -> false
                }
            }

            if (enteredPub != null) {
                // Only toast if we've entered a NEW pub
                if (currentPubId != enteredPub.id) {
                    currentPubId = enteredPub.id
                    Toast.makeText(
                        context, 
                        "you have entered pub: ${enteredPub.name}", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Clear the current pub ID when we leave
                currentPubId = null
            }
        }

        mapView.location.addOnIndicatorPositionChangedListener(listener)
        
        onDispose {
            mapView.location.removeOnIndicatorPositionChangedListener(listener)
        }
    }
}
