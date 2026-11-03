package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ====================================================================
// MANDATED PREMIUM LIGHT GLASSMORPHISM DESIGN SYSTEM COLOR TOKENS
// ====================================================================

// Required Color Palette
val PrimaryLavender = Color(0xFFB8A4F4)
val DeepLavender = Color(0xFF6C42C4)
val ChartreuseAccent = Color(0xFFD6F36A)
val DeepChartreuse = Color(0xFF556B16)
val TomatoRed = Color(0xFFFF6347)
val DeepTomato = Color(0xFFC34630)

// Standard Aliases
val LavenderPrimary = PrimaryLavender
val LavenderDeep = DeepLavender
val ChartreusePrimary = ChartreuseAccent
val ChartreuseDeep = DeepChartreuse
val TomatoPrimary = TomatoRed
val TomatoDeep = DeepTomato

// Background Surfaces
val SoftLavender = Color(0xFFF4EFFF)
val IvoryWhite = Color(0xFFFFFEF8)
val PaleLime = Color(0xFFF4FAD9)
val PureWhite = Color(0xFFFFFFFF)

val LightBgLavender = SoftLavender
val LightBgIvory = IvoryWhite
val LightBgPaleLime = PaleLime

// Glass Translucency Tokens
val GlassSurface = Color(0xF2FFFFFF)
val GlassSurfaceSubtle = Color(0xD9FFFFFF)
val GlassBorder = Color(0xD9FFFFFF)
val GlassBorderLavender = Color(0x33B8A4F4)
val GlassShadow = Color(0x145C4484)

// Typography
val GlassTextPrimary = Color(0xFF251C38)
val GlassTextSecondary = Color(0xFF645B74)
val GlassTextMuted = Color(0xFF746589)

// Backwards-compatible mappings for existing UI elements
val IndigoPrimary = DeepLavender
val IndigoLight = PrimaryLavender
val IndigoDark = Color(0xFF532E9C)

val CyanAccent = ChartreuseAccent
val CyanLight = PaleLime
val CyanDark = DeepChartreuse

val EmeraldSuccess = Color(0xFF22C55E)
val EmeraldLight = Color(0xFF86EFAC)
val AmberWarning = Color(0xFFF59E0B)
val RoseError = TomatoRed

// Canvas and Cards mapped to Light Glassmorphism
val DarkBgCanvas = IvoryWhite
val DarkSidebarBg = SoftLavender
val DarkSurfaceCard = GlassSurface
val DarkSurfaceElevated = GlassSurfaceSubtle
val DarkBorder = GlassBorderLavender
val DarkBorderSubtle = Color(0x1FB8A4F4)

val TextPrimary = GlassTextPrimary
val TextSecondary = GlassTextSecondary
val TextMuted = GlassTextMuted

// Light Tokens
val LightBgCanvas = IvoryWhite
val LightSidebarBg = SoftLavender
val LightSurfaceCard = GlassSurface
val LightSurfaceElevated = GlassSurfaceSubtle
val LightBorder = GlassBorderLavender
val LightBorderSubtle = Color(0x1FB8A4F4)
val LightTextPrimary = GlassTextPrimary
val LightTextSecondary = GlassTextSecondary
val LightTextMuted = GlassTextMuted

// Code Lab Editor
val CodeEditorBg = Color(0xFFFDFBF7)
val CodeKeyword = DeepLavender
val CodeString = Color(0xFF15803D)
val CodeComment = GlassTextMuted
val CodeFunction = Color(0xFF0284C7)
