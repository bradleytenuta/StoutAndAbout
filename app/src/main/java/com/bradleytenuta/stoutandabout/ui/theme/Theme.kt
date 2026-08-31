package com.bradleytenuta.stoutandabout.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = RubberHoseWhite,
    secondary = RubberHoseParchment,
    tertiary = RubberHoseBlack,
    background = RubberHoseBlack,
    surface = RubberHoseBlack,
    onPrimary = RubberHoseBlack,
    onSecondary = RubberHoseBlack,
    onTertiary = RubberHoseWhite,
    onBackground = RubberHoseWhite,
    onSurface = RubberHoseWhite,
    primaryContainer = RubberHoseWhite,
    onPrimaryContainer = RubberHoseBlack
)

private val LightColorScheme = lightColorScheme(
    primary = RubberHoseBlack,
    secondary = RubberHoseWhite,
    tertiary = RubberHoseParchment,
    background = RubberHoseParchment,
    surface = RubberHoseParchment,
    onPrimary = RubberHoseWhite,
    onSecondary = RubberHoseBlack,
    onTertiary = RubberHoseBlack,
    onBackground = RubberHoseBlack,
    onSurface = RubberHoseBlack,
    primaryContainer = RubberHoseBlack,
    onPrimaryContainer = RubberHoseWhite
)

@Composable
fun StoutAboutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled by default to maintain the Rubber Hose aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
