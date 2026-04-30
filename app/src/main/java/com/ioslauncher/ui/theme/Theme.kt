package com.ioslauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// iOS Color Palette
object IOSColors {
    val Blue = Color(0xFF007AFF)
    val Green = Color(0xFF34C759)
    val Indigo = Color(0xFF5856D6)
    val Orange = Color(0xFFFF9500)
    val Pink = Color(0xFFFF2D55)
    val Purple = Color(0xFFAF52DE)
    val Red = Color(0xFFFF3B30)
    val Teal = Color(0xFF5AC8FA)
    val Yellow = Color(0xFFFFCC00)
    val Gray = Color(0xFF8E8E93)
    val Gray2 = Color(0xFFAEAEB2)
    val Gray3 = Color(0xFFC7C7CC)
    val Gray4 = Color(0xFFD1D1D6)
    val Gray5 = Color(0xFFE5E5EA)
    val Gray6 = Color(0xFFF2F2F7)

    // Dark mode backgrounds
    val SystemBackground = Color(0x00000000)
    val GlassDark = Color(0x80000000)
    val GlassMedium = Color(0x60000000)
    val GlassLight = Color(0x30FFFFFF)
    val GlassWhite = Color(0x20FFFFFF)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xB3FFFFFF)
    val Separator = Color(0x40FFFFFF)

    // Control Center colors
    val CCBackground = Color(0x99000000)
    val CCTileActive = Color(0xFFFFFFFF)
    val CCTileInactive = Color(0x60FFFFFF)
}

private val DarkColorScheme = darkColorScheme(
    primary = IOSColors.Blue,
    secondary = IOSColors.Teal,
    background = Color.Transparent,
    surface = IOSColors.GlassDark,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun IOSLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = IOSTypography,
        content = content
    )
}
