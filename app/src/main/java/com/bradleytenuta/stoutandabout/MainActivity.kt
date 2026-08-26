package com.bradleytenuta.stoutandabout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bradleytenuta.stoutandabout.ui.theme.StoutAboutTheme
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.gestures

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoutAboutTheme {
                MapScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                // Disable manual navigation gestures
                gestures.scrollEnabled = false
                gestures.pinchToZoomEnabled = false
                gestures.rotateEnabled = false
                gestures.pitchEnabled = false
                gestures.doubleTapToZoomInEnabled = false
                gestures.quickZoomEnabled = false
            }
        }
    )
}
