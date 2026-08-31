package com.bradleytenuta.stoutandabout.pages.map

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
import com.bradleytenuta.stoutandabout.domain.PuckModel
import com.bradleytenuta.stoutandabout.effects.SnackbarHost
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.launch

class MapScreen {
    @OptIn(MapboxExperimental::class)
    @Composable
    fun Content(puckModel: PuckModel = PuckModel.BEER_BOTTLE) {
        val mapViewportState = rememberMapViewportState()
        var isFreeRoam by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()) {
            StoutAndAboutMap(
                mapViewportState = mapViewportState,
                isFreeRoam = isFreeRoam,
                puckModel = puckModel
            )

            FreeRoamButton(
                isFreeRoam = isFreeRoam,
                onClick = {
                    isFreeRoam = !isFreeRoam
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val message = if (isFreeRoam) "Entering free roam" else "Entering follow"
                        snackbarHostState.showSnackbar(message)
                    }
                }
            )

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
