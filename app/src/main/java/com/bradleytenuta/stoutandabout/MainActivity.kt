package com.bradleytenuta.stoutandabout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bradleytenuta.stoutandabout.models.PuckModel
import com.bradleytenuta.stoutandabout.pages.welcome.WelcomeScreen
import com.bradleytenuta.stoutandabout.pages.map.MapScreen
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseParchment
import com.bradleytenuta.stoutandabout.ui.theme.StoutAboutTheme
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

class MainActivity : ComponentActivity(), PermissionsListener {
    private lateinit var permissionsManager: PermissionsManager
    private var permissionGranted by mutableStateOf(false)
    private var selectedPuckModel by mutableStateOf(PuckModel.BEER_BOTTLE)
    private var isWelcomeCompleted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionGranted = PermissionsManager.areLocationPermissionsGranted(this)
        if (permissionGranted) {
            isWelcomeCompleted = true
        } else {
            permissionsManager = PermissionsManager(this)
        }

        setContent {
            StoutAboutTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Main Content
                    if (permissionGranted && isWelcomeCompleted) {
                        MapScreen().Content(selectedPuckModel)
                    } else {
                        WelcomeScreen().Content(
                            locationPermissionGranted = permissionGranted,
                            onGrantPermission = {
                                permissionsManager.requestLocationPermissionsFromManifest(this@MainActivity)
                            },
                            onCharacterSelected = { model ->
                                selectedPuckModel = model
                            },
                            onComplete = {
                                isWelcomeCompleted = true
                            }
                        )
                    }

                    // Translucent Status Bar Overlay
                    StatusBarProtection().Content(color = RubberHoseParchment)
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
