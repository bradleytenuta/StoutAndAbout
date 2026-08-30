package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.bradleytenuta.stoutandabout.pages.effects.SnackbarHost
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.launch

class MapScreen {
    @OptIn(MapboxExperimental::class)
    @Composable
    fun Content() {
        val mapViewportState = rememberMapViewportState()
        var isFreeRoam by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()) {
            StoutAndAboutMap(
                mapViewportState = mapViewportState,
                isFreeRoam = isFreeRoam
            )

            // Gesture interceptor: Only active when NOT in free roam
            if (!isFreeRoam) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    isFreeRoam = true
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar("Entering free roam")
                                    }
                                }
                            )
                        }
                )
            }

            FreeRoamButton(
                visible = isFreeRoam,
                onClick = {
                    isFreeRoam = false
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar("Entering follow")
                    }
                }
            )

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
