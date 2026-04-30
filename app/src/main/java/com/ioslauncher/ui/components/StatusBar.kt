package com.ioslauncher.ui.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioslauncher.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IOSStatusBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var currentTime by remember { mutableStateOf("") }
    var batteryLevel by remember { mutableStateOf(100) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                    val level = intent.getIntExtra("level", 0)
                    val scale = intent.getIntExtra("scale", 100)
                    batteryLevel = (level * 100 / scale)
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)
        onDispose { context.unregisterReceiver(receiver) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time (left)
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Right icons: signal, wifi, battery
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Signal bars (simplified)
            SignalIcon()
            // Wifi icon
            WifiIcon()
            // Battery
            BatteryIcon(level = batteryLevel)
        }
    }
}

@Composable
fun SignalIcon() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        listOf(4, 6, 8, 10).forEachIndexed { i, height ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .also {
                        if (i < 3) it.padding(bottom = 0.dp)
                    }
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(
                        color = Color.White,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun WifiIcon() {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(15.dp, 12.dp)) {
        val centerX = size.width / 2
        val baseY = size.height
        for (i in 1..3) {
            val radius = i * (size.width / 6)
            val sweep = 140f
            val start = 180f + (180f - sweep) / 2
            drawArc(
                color = Color.White,
                startAngle = start - 90,
                sweepAngle = sweep,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
            )
        }
    }
}

@Composable
fun BatteryIcon(level: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$level%",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(3.dp))
        // Battery shell
        androidx.compose.foundation.Canvas(modifier = Modifier.size(25.dp, 12.dp)) {
            val capW = 2f
            val bodyW = size.width - capW
            drawRoundRect(
                color = Color.White.copy(alpha = 0.35f),
                size = androidx.compose.ui.geometry.Size(bodyW, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
            )
            val fillWidth = (bodyW - 4) * (level / 100f)
            val fillColor = when {
                level > 20 -> Color.White
                else -> Color(0xFFFF3B30)
            }
            drawRoundRect(
                color = fillColor,
                topLeft = androidx.compose.ui.geometry.Offset(2f, 2f),
                size = androidx.compose.ui.geometry.Size(fillWidth, size.height - 4),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(1f)
            )
            // Cap
            drawRoundRect(
                color = Color.White.copy(alpha = 0.5f),
                topLeft = androidx.compose.ui.geometry.Offset(bodyW + 1, size.height / 4),
                size = androidx.compose.ui.geometry.Size(capW, size.height / 2),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(1f)
            )
        }
    }
}
