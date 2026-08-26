package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.LocationPuck3D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions

class MapScreen {
    @OptIn(MapboxExperimental::class)
    @Composable
    fun Content() {
        val mapViewportState = rememberMapViewportState()
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
        ) {
            MapEffect(Unit) { mapView ->
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS)
                mapView.location.updateSettings {
                    enabled = true
                    puckBearingEnabled = true
                    puckBearing = PuckBearing.HEADING
                    locationPuck = LocationPuck3D(
                        modelUri = "asset://casual_character.glb",
                        modelScale = listOf(60f, 60f, 60f)
                    )
                }
                mapView.gestures.updateSettings {
                    scrollEnabled = false
                    pinchToZoomEnabled = false
                    rotateEnabled = false
                    pitchEnabled = false
                    doubleTapToZoomInEnabled = false
                    quickZoomEnabled = false
                }
                mapViewportState.transitionToFollowPuckState(
                    FollowPuckViewportStateOptions.Builder()
                        .zoom(17.0)
                        .bearing(FollowPuckViewportStateBearing.SyncWithLocationPuck)
                        .pitch(45.0)
                        .build()
                )
            }
        }
    }
}
