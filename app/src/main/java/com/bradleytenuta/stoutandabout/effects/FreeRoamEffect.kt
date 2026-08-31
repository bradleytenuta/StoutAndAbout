package com.bradleytenuta.stoutandabout.effects

import androidx.compose.runtime.Composable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.viewport.ViewportStatus
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.plugin.viewport.state.FollowPuckViewportState

@OptIn(MapboxExperimental::class)
@Composable
fun FreeRoamEffect(
    isFreeRoam: Boolean,
    mapViewportState: MapViewportState
) {
    MapEffect(isFreeRoam, mapViewportState.mapViewportStatus) { mapView ->
        val status = mapViewportState.mapViewportStatus
        val isFollowing = status is ViewportStatus.State && status.state is FollowPuckViewportState
        val isTransitioningToFollow = status is ViewportStatus.Transition && status.toState is FollowPuckViewportState

        // Assume free roam if not following or transitioning to follow
        val effectiveFreeRoam = isFreeRoam || (!isFollowing && !isTransitioningToFollow)

        mapView.gestures.updateSettings {
            scrollEnabled = effectiveFreeRoam
            pinchToZoomEnabled = effectiveFreeRoam
            rotateEnabled = effectiveFreeRoam
            pitchEnabled = effectiveFreeRoam
            doubleTapToZoomInEnabled = effectiveFreeRoam
            doubleTouchToZoomOutEnabled = effectiveFreeRoam
            quickZoomEnabled = effectiveFreeRoam
        }

        if (effectiveFreeRoam) {
            // Bounds for the UK (roughly)
            val ukBounds = CoordinateBounds(
                Point.fromLngLat(-10.85, 49.82), // Southwest (bottom left)
                Point.fromLngLat(2.02, 59.48),   // Northeast (top right)
                false
            )
            mapView.mapboxMap.setBounds(
                CameraBoundsOptions.Builder()
                    .bounds(ukBounds)
                    .minZoom(5.0)
                    .build()
            )
        } else {
            // Reset bounds and zoom limits when not in free roam
            mapView.mapboxMap.setBounds(
                CameraBoundsOptions.Builder()
                    .minZoom(0.0)
                    .bounds(
                        CoordinateBounds(
                            Point.fromLngLat(-180.0, -90.0),
                            Point.fromLngLat(180.0, 90.0),
                            false
                        )
                    )
                    .build()
            )
        }

        // If we're supposed to be following but we're not, transition now
        if (!isFreeRoam && !isFollowing && !isTransitioningToFollow) {
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
