package com.bradleytenuta.stoutandabout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

class StatusBarProtection {
    @Composable
    fun Content(
        color: Color,
    ) {
        val density = LocalDensity.current
        val statusBarHeight = with(density) {
            // Get status bar height and add a small multiplier for coverage
            (WindowInsets.statusBars.getTop(this) * 1.2f).toDp()
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 1f),
                            color.copy(alpha = 0.8f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
