package com.ioslauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ioslauncher.ui.theme.IOSColors

// iOS-style glass card
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.3f,
    borderAlpha: Float = 0.25f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = alpha + 0.05f),
                        Color.White.copy(alpha = alpha - 0.05f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha + 0.1f),
                        Color.White.copy(alpha = borderAlpha)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}

// Dark glass (Control Center tiles)
@Composable
fun DarkGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C2C2E).copy(alpha = 0.85f),
                        Color(0xFF1C1C1E).copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}

// Frosted overlay background
@Composable
fun FrostedBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0x40FFFFFF),
                        Color(0x20000000)
                    )
                )
            ),
        content = content
    )
}

// iOS separator line
@Composable
fun IOSSeparator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(IOSColors.Separator)
    )
}
