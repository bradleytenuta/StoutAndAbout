package com.bradleytenuta.stoutandabout.pages.map

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseParchment
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseWhite

@Composable
fun BoxScope.FreeRoamButton(
    isFreeRoam: Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = if (isFreeRoam) RubberHoseWhite else RubberHoseParchment,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .padding(bottom = 32.dp) // Offset from Mapbox logo/attribution
    ) {
        Icon(
            imageVector = if (isFreeRoam) Icons.Default.MyLocation else Icons.Default.Explore,
            contentDescription = if (isFreeRoam) "Reset to Follow Mode" else "Enter Free Roam"
        )
    }
}
