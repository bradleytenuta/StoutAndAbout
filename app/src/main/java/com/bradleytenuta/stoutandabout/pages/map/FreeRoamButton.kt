package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.FreeRoamButton(
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .padding(bottom = 32.dp) // Offset from Mapbox logo/attribution
    ) {
        FloatingActionButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Reset to Follow Mode"
            )
        }
    }
}
