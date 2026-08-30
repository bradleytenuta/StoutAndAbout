package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.data.PubDataStore
import com.mapbox.maps.MapboxDelicateApi
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.ColorValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.generated.FillExtrusionLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.plugin.LocationPuck3D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions

@OptIn(MapboxExperimental::class, MapboxDelicateApi::class)
@Composable
fun StoutAndAboutMap(
    mapViewportState: MapViewportState,
    isFreeRoam: Boolean,
    modifier: Modifier = Modifier
) {
    val pubs by PubDataStore.pubs.collectAsState()

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
        val sourceState = rememberGeoJsonSourceState()
        LaunchedEffect(pubs) {
            sourceState.data = GeoJSONData(pubs.map { it.feature })
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
