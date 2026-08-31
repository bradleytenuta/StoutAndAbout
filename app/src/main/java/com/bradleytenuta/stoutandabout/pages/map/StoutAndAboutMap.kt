package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.models.PuckModel
import com.bradleytenuta.stoutandabout.pages.map.effects.LocationVisitEffect
import com.bradleytenuta.stoutandabout.pages.map.effects.PubPolygonsEffect
import com.bradleytenuta.stoutandabout.pages.map.effects.StandardStyleEffect
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.LocationPuck3D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions

@OptIn(MapboxExperimental::class)
@Composable
fun StoutAndAboutMap(
    mapViewportState: MapViewportState,
    isFreeRoam: Boolean,
    modifier: Modifier = Modifier,
    puckModel: PuckModel
) {
    MapboxMap(
        modifier = modifier.fillMaxSize(),
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
        StandardStyleEffect()
        PubPolygonsEffect()
        LocationVisitEffect()

        MapEffect(puckModel) { mapView ->
            mapView.location.updateSettings {
                enabled = true
                puckBearingEnabled = true
                puckBearing = PuckBearing.HEADING
                locationPuck = LocationPuck3D(
                    modelUri = puckModel.uri,
                    modelScale = puckModel.scale,
                    modelRotation = puckModel.rotation
                )
            }
        }

        MapEffect(isFreeRoam) { mapView ->
            mapView.gestures.updateSettings {
                scrollEnabled = isFreeRoam
                pinchToZoomEnabled = isFreeRoam
                rotateEnabled = isFreeRoam
                pitchEnabled = isFreeRoam
                doubleTapToZoomInEnabled = isFreeRoam
                doubleTouchToZoomOutEnabled = isFreeRoam
                quickZoomEnabled = isFreeRoam
            }
            if (!isFreeRoam) {
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
