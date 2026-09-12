package com.mydrive.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Copper,
    onPrimary = Ink,
    primaryContainer = CopperDim,
    onPrimaryContainer = Ivory,
    secondary = Sage,
    onSecondary = Ink,
    secondaryContainer = SageDim,
    onSecondaryContainer = Ivory,
    tertiary = Sky,
    onTertiary = Ink,
    background = Ink,
    onBackground = Ivory,
    surface = InkElevated,
    onSurface = Ivory,
    surfaceVariant = Graphite,
    onSurfaceVariant = IvoryMuted,
    surfaceTint = Copper,
    outline = StrokeStrong,
    outlineVariant = Stroke,
    error = Clay,
    onError = Ivory,
    errorContainer = ClayDim,
    onErrorContainer = Ivory,
    inverseSurface = Ivory,
    inverseOnSurface = Ink,
    scrim = Color.Black
)

@Composable
fun MyDriveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = AppTypography,
        content = content
    )
}
