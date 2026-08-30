package com.bradleytenuta.stoutandabout.pages.effects

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bradleytenuta.stoutandabout.ui.theme.RubberHoseWhite

@Composable
fun BoxScope.SnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 96.dp) // Position above the FAB area
    ) { snackbarData ->
        Snackbar(
            snackbarData = snackbarData,
            containerColor = RubberHoseWhite,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
