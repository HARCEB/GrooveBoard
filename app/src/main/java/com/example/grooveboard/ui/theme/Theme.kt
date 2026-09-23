package com.example.grooveboard.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = GrooveNavy,
    onPrimary = Color.White,
    primaryContainer = GrooveBlue,
    onPrimaryContainer = Color.White,
    secondary = GrooveCyan,
    onSecondary = Color.White,
    secondaryContainer = StatusInProgressContainer,
    onSecondaryContainer = GrooveBlue,
    background = GrooveBackground,
    onBackground = TextPrimary,
    surface = GrooveSurface,
    onSurface = TextPrimary,
    surfaceVariant = GrooveBackground,
    onSurfaceVariant = TextSecondary,
    outline = GrooveCardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = GrooveCyanLight,
    onPrimary = GrooveNavy,
    secondary = GrooveCyan,
    onSecondary = Color.White,
    background = Color(0xFF0B0F19),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF1F5F9),
    outline = Color(0xFF1F2937)
)

@Composable
fun GrooveBoardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}