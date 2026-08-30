package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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
        var isFreeRoam by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                compass = {},
                scaleBar = {},
                logo = {
                    Logo(contentPadding = PaddingValues(start = 12.dp, bottom = 36.dp))
                },
                attribution = {
                    Attribution(contentPadding = PaddingValues(start = 100.dp, bottom = 36.dp))
                }
            ) {
                MapEffect(Unit) { mapView ->
                    mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS)
                    mapView.location.updateSettings {
                        enabled = true
                        puckBearingEnabled = true
                        puckBearing = PuckBearing.HEADING
                        locationPuck = LocationPuck3D(
                            modelUri = "asset://casual_character.glb",
                            modelScale = listOf(60f, 60f, 60f),
                            // Adjust the 3rd value (90f) if the character faces the wrong direction
                            modelRotation = listOf(0f, 0f, 180f)
                        )
                    }
                }

                MapEffect(isFreeRoam) { mapView ->
                    if (isFreeRoam) {
                        mapView.gestures.updateSettings {
                            scrollEnabled = true
                            pinchToZoomEnabled = true
                            rotateEnabled = true
                            pitchEnabled = true
                            doubleTapToZoomInEnabled = true
                            doubleTouchToZoomOutEnabled = true
                            quickZoomEnabled = true
                        }
                    } else {
                        mapView.gestures.updateSettings {
                            scrollEnabled = false
                            pinchToZoomEnabled = false
                            rotateEnabled = false
                            pitchEnabled = false
                            doubleTapToZoomInEnabled = false
                            doubleTouchToZoomOutEnabled = false
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

            // Gesture interceptor: Only active when NOT in free roam
            if (!isFreeRoam) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    isFreeRoam = true
                                }
                            )
                        }
                )
            }

            AnimatedVisibility(
                visible = isFreeRoam,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 32.dp) // Offset from Mapbox logo/attribution
            ) {
                FloatingActionButton(
                    onClick = { isFreeRoam = false },
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Reset to Follow Mode"
                    )
                }
            }
        }
    }
}
