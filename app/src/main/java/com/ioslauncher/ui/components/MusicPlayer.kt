package com.ioslauncher.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioslauncher.ui.theme.IOSColors

// Music Player in Control Center
@Composable
fun MusicPlayerTile(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.35f) }
    val rotation by rememberInfiniteTransition(label = "disc").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "discRot"
    )

    DarkGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        cornerRadius = 18.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Disc art
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .rotate(if (isPlaying) rotation else 0f)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(listOf(
                                Color(0xFF6C3483), Color(0xFF1A5276),
                                Color(0xFF117A65), Color(0xFF6C3483)
                            ))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1C1C1E))
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Music",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = "Now Playing",
                        color = Color.White.copy(0.5f),
                        fontSize = 11.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Prev
                    Text("⏮", fontSize = 18.sp,
                        modifier = Modifier.clickable { })
                    // Play/Pause
                    Text(if (isPlaying) "⏸" else "▶️", fontSize = 20.sp,
                        modifier = Modifier.clickable { isPlaying = !isPlaying })
                    // Next
                    Text("⏭", fontSize = 18.sp,
                        modifier = Modifier.clickable { })
                }
            }

            Spacer(Modifier.height(8.dp))

            // Progress bar
            Slider(
                value = progress,
                onValueChange = { progress = it },
                modifier = Modifier.fillMaxWidth().height(20.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(0.3f)
                )
            )
        }
    }
}

// Full-screen Music Player
@Composable
fun FullMusicPlayer(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.35f) }
    var volume by remember { mutableStateOf(0.7f) }
    val rotation by rememberInfiniteTransition(label = "disc2").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)), label = "r"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(36.dp).height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(0.3f))
                .clickable { onDismiss() }
        )
        Spacer(Modifier.height(12.dp))

        Text("NOW PLAYING", color = Color.White.copy(0.5f), fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)

        Spacer(Modifier.height(32.dp))

        // Album art
        Box(
            modifier = Modifier
                .size(280.dp)
                .rotate(if (isPlaying) rotation else 0f)
                .clip(CircleShape)
                .background(Brush.sweepGradient(listOf(
                    Color(0xFF8E2DE2), Color(0xFF4A00E0),
                    Color(0xFF00C9FF), Color(0xFF8E2DE2)
                ))),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1C1C1E))
            )
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Current Track", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Artist Name", color = Color.White.copy(0.6f), fontSize = 16.sp)
            }
            Text("♡", fontSize = 24.sp, color = IOSColors.Red,
                modifier = Modifier.clickable { })
        }

        Spacer(Modifier.height(16.dp))

        Slider(
            value = progress,
            onValueChange = { progress = it },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(0.25f)
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("1:25", color = Color.White.copy(0.5f), fontSize = 12.sp)
            Text("3:48", color = Color.White.copy(0.5f), fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔀", fontSize = 22.sp, modifier = Modifier.clickable { })
            Text("⏮", fontSize = 32.sp, modifier = Modifier.clickable { })
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Text(if (isPlaying) "⏸" else "▶", fontSize = 28.sp)
            }
            Text("⏭", fontSize = 32.sp, modifier = Modifier.clickable { })
            Text("🔁", fontSize = 22.sp, modifier = Modifier.clickable { })
        }

        Spacer(Modifier.height(24.dp))

        // Volume slider
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("🔈", fontSize = 18.sp)
            Slider(
                value = volume, onValueChange = { volume = it },
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(0.25f)
                )
            )
            Text("🔊", fontSize = 18.sp)
        }
    }
}
