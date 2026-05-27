package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CosmicColorScheme = darkColorScheme(
    primary = NeonViolet,
    secondary = NeonTeal,
    tertiary = NeonMint,
    background = ObsidianBlack,
    surface = SlateDark,
    onPrimary = IceWhite,
    onSecondary = ObsidianBlack,
    onBackground = IceWhite,
    onSurface = IceWhite,
    surfaceVariant = SlateCard,
    error = NeonCoral
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark theme by default for immersive cyberpunk OS feel
    content: @Composable () -> Unit
) {
    val colorScheme = CosmicColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
