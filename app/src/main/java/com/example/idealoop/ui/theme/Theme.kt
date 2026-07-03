package com.example.idealoop.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val IdeaLoopColorScheme = lightColorScheme(
    primary = IdeaLoopIndigo500,
    onPrimary = IdeaLoopWhite,
    primaryContainer = IdeaLoopIndigo300,
    onPrimaryContainer = IdeaLoopBlue900,
    secondary = IdeaLoopPurple500,
    onSecondary = IdeaLoopWhite,
    tertiary = IdeaLoopBlue400,
    background = IdeaLoopBackgroundMiddle,
    onBackground = IdeaLoopSlate800,
    surface = IdeaLoopSurface,
    onSurface = IdeaLoopSlate800,
    surfaceVariant = IdeaLoopWhite.copy(alpha = 0.70f),
    onSurfaceVariant = IdeaLoopSlate600,
    outline = IdeaLoopWhite.copy(alpha = 0.60f),
)

@Composable
fun IdeaLoopTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IdeaLoopColorScheme,
        typography = IdeaLoopTypography,
        shapes = IdeaLoopShapes,
        content = content
    )
}
