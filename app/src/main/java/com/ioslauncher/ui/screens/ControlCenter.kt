package com.ioslauncher.ui.screens

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioslauncher.ui.components.DarkGlassCard
import com.ioslauncher.ui.components.MusicPlayerTile
import com.ioslauncher.ui.theme.IOSColors

@Composable
fun ControlCenter(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var wifiEnabled by remember { mutableStateOf(true) }
    var bluetoothEnabled by remember { mutableStateOf(false) }
    var airplaneMode by remember { mutableStateOf(false) }
    var nfcEnabled by remember { mutableStateOf(false) }
    var doNotDisturb by remember { mutableStateOf(false) }
    var flashlightOn by remember { mutableStateOf(false) }
    var brightness by remember { mutableStateOf(0.7f) }
    var volume by remember { mutableStateOf(0.5f) }

    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight(0.88f)
                .fillMaxWidth(0.95f)
                .padding(top = 12.dp, end = 8.dp)
                .clickable(enabled = false) {}
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -50) onDismiss()
                    }
                },
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Connectivity tiles (2x2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Network cluster
                DarkGlassCard(
                    modifier = Modifier.weight(1f).height(130.dp),
                    cornerRadius = 18.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // WiFi
                        CCToggleRow("📶", "Wi-Fi", wifiEnabled) { wifiEnabled = !wifiEnabled }
                        CCDivider()
                        // Bluetooth
                        CCToggleRow("🔵", "Bluetooth", bluetoothEnabled) { bluetoothEnabled = !bluetoothEnabled }
                        CCDivider()
                        // Airplane
                        CCToggleRow("✈️", "Airplane", airplaneMode) { airplaneMode = !airplaneMode }
                        CCDivider()
                        // NFC
                        CCToggleRow("📡", "NFC", nfcEnabled) { nfcEnabled = !nfcEnabled }
                    }
                }

                // Quick tiles column
                Column(
                    modifier = Modifier.weight(0.5f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Flashlight
                    CCTile(
                        emoji = "🔦",
                        label = "Flash",
                        active = flashlightOn,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    ) { flashlightOn = !flashlightOn }

                    // DND
                    CCTile(
                        emoji = "🌙",
                        label = "Focus",
                        active = doNotDisturb,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    ) { doNotDisturb = !doNotDisturb }
                }
            }

            // Brightness slider
            SliderTile(
                emoji = "☀️",
                label = "Brightness",
                value = brightness,
                onValueChange = { brightness = it }
            )

            // Volume slider
            SliderTile(
                emoji = "🔊",
                label = "Volume",
                value = volume,
                onValueChange = {
                    volume = it
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        (it * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt(),
                        0
                    )
                }
            )

            // Music Player tile
            MusicPlayerTile()

            // Quick action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    Triple("⏱️", "Timer") {},
                    Triple("🧮", "Calc") { context.startActivity(launchOrFallback(context, "com.sec.android.app.popupcalculator")) },
                    Triple("📸", "Camera") { context.startActivity(launchOrFallback(context, "com.sec.android.app.camera")) },
                    Triple("🔍", "Search") {}
                ).forEach { (emoji, label, action) ->
                    DarkGlassCard(
                        modifier = Modifier.weight(1f).height(64.dp).clickable { action() },
                        cornerRadius = 14.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(emoji, fontSize = 20.sp)
                            Text(label, color = Color.White.copy(0.7f), fontSize = 9.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CCToggleRow(emoji: String, label: String, active: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(emoji, fontSize = 14.sp)
            Text(label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .size(20.dp, 12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (active) IOSColors.Green else Color.Gray.copy(0.4f))
        )
    }
}

@Composable
private fun CCDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.White.copy(0.12f))
    )
}

@Composable
private fun CCTile(
    emoji: String,
    label: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    DarkGlassCard(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        cornerRadius = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (active) Color.White.copy(0.15f)
                    else Color.Transparent
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(emoji, fontSize = 22.sp)
                Text(
                    label,
                    color = if (active) Color.White else Color.White.copy(0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SliderTile(
    emoji: String,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    DarkGlassCard(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        cornerRadius = 18.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(emoji, fontSize = 18.sp)
            Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(0.2f)
                )
            )
        }
    }
}

private fun launchOrFallback(context: Context, pkg: String): Intent {
    return context.packageManager.getLaunchIntentForPackage(pkg)
        ?: Intent(Settings.ACTION_SETTINGS)
}
