package com.ioslauncher.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ioslauncher.data.AppInfo

@Composable
fun IOSDock(
    apps: List<AppInfo>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
        cornerRadius = 28.dp,
        alpha = 0.25f,
        borderAlpha = 0.3f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            apps.take(4).forEach { app ->
                AppIcon(
                    app = app,
                    showLabel = false,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}
