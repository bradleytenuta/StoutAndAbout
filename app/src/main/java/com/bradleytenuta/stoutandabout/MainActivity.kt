package com.bradleytenuta.stoutandabout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.ui.theme.StoutAboutTheme
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
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

class MainActivity : ComponentActivity(), PermissionsListener {
    private lateinit var permissionsManager: PermissionsManager
    private var permissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionGranted = PermissionsManager.areLocationPermissionsGranted(this)
        if (!permissionGranted) {
            permissionsManager = PermissionsManager(this)
        }

        setContent {
            StoutAboutTheme {
                if (permissionGranted) {
                    MapScreen()
                } else {
                    WelcomeScreen(
                        onGrantPermission = {
                            permissionsManager.requestLocationPermissionsFromManifest(this)
                        }
                    )
                }
            }
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "This app needs location access to track your pub visits.", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        permissionGranted = granted
        if (!granted) {
            Toast.makeText(this, "Location permission is required to use the map.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::permissionsManager.isInitialized) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

@Composable
fun WelcomeScreen(onGrantPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Stout & About!",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "To find the best pubs and track your visits, we need to know your location.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onGrantPermission) {
            Text(text = "Grant Location Permission")
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen() {
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
