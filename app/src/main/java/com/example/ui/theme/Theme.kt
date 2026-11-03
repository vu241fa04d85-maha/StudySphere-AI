package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightGlassColorScheme = lightColorScheme(
    primary = DeepLavender,
    onPrimary = Color.White,
    primaryContainer = SoftLavender,
    onPrimaryContainer = DeepLavender,
    
    secondary = DeepChartreuse,
    onSecondary = Color.White,
    secondaryContainer = PaleLime,
    onSecondaryContainer = DeepChartreuse,
    
    tertiary = TomatoRed,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFECE8),
    onTertiaryContainer = DeepTomato,
    
    background = IvoryWhite,
    onBackground = GlassTextPrimary,
    surface = GlassSurface,
    onSurface = GlassTextPrimary,
    surfaceVariant = GlassSurfaceSubtle,
    onSurfaceVariant = GlassTextSecondary,
    
    outline = GlassBorderLavender,
    outlineVariant = Color(0x1FB8A4F4),
    error = TomatoRed,
    onError = Color.White
)

@Composable
fun AIStudyAssistantTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightGlassColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatible alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AIStudyAssistantTheme(darkTheme = darkTheme, content = content)
}
