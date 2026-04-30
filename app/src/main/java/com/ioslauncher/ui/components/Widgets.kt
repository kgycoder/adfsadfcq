package com.ioslauncher.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioslauncher.ui.theme.IOSColors
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

// iOS Analog + Digital Clock Widget
@Composable
fun ClockWidget(modifier: Modifier = Modifier) {
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }
    var timeStr by remember { mutableStateOf("") }
    var dateStr by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            calendar = Calendar.getInstance()
            timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            dateStr = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    GlassCard(
        modifier = modifier.size(160.dp),
        cornerRadius = 22.dp,
        alpha = 0.2f
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CLOCK",
                color = Color.White.copy(0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            AnalogClock(
                calendar = calendar,
                modifier = Modifier.size(90.dp)
            )
            Text(
                text = timeStr,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun AnalogClock(calendar: Calendar, modifier: Modifier = Modifier) {
    val hour = calendar.get(Calendar.HOUR).toFloat()
    val minute = calendar.get(Calendar.MINUTE).toFloat()
    val second = calendar.get(Calendar.SECOND).toFloat()

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2f

        // Clock face
        drawCircle(color = Color.White.copy(0.12f), radius = radius, center = center)
        drawCircle(
            color = Color.White.copy(0.25f), radius = radius,
            style = androidx.compose.ui.graphics.drawscope.Stroke(1f), center = center
        )

        // Hour markers
        for (i in 0..11) {
            val angle = Math.toRadians(i * 30.0)
            val isMain = i % 3 == 0
            val markerLen = if (isMain) radius * 0.15f else radius * 0.08f
            val outerR = radius - 4
            val start = Offset(
                (center.x + outerR * sin(angle)).toFloat(),
                (center.y - outerR * cos(angle)).toFloat()
            )
            val end = Offset(
                (center.x + (outerR - markerLen) * sin(angle)).toFloat(),
                (center.y - (outerR - markerLen) * cos(angle)).toFloat()
            )
            drawLine(Color.White.copy(if (isMain) 0.8f else 0.4f), start, end,
                strokeWidth = if (isMain) 2f else 1f, cap = StrokeCap.Round)
        }

        // Hour hand
        val hourAngle = ((hour + minute / 60f) * 30f)
        rotate(hourAngle, center) {
            drawLine(Color.White, center,
                Offset(center.x, center.y - radius * 0.5f), strokeWidth = 3f, cap = StrokeCap.Round)
        }

        // Minute hand
        val minuteAngle = (minute + second / 60f) * 6f
        rotate(minuteAngle, center) {
            drawLine(Color.White, center,
                Offset(center.x, center.y - radius * 0.7f), strokeWidth = 2f, cap = StrokeCap.Round)
        }

        // Second hand (red)
        val secondAngle = second * 6f
        rotate(secondAngle, center) {
            drawLine(IOSColors.Red, center,
                Offset(center.x, center.y - radius * 0.75f), strokeWidth = 1f, cap = StrokeCap.Round)
            drawLine(IOSColors.Red, center,
                Offset(center.x, center.y + radius * 0.2f), strokeWidth = 1f, cap = StrokeCap.Round)
        }

        // Center dot
        drawCircle(Color.White, radius = 4f, center = center)
        drawCircle(IOSColors.Red, radius = 3f, center = center)
    }
}

// iOS Calendar Widget
@Composable
fun CalendarWidget(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val month = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date())
    val year = calendar.get(Calendar.YEAR)
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())

    GlassCard(
        modifier = modifier.size(160.dp),
        cornerRadius = 22.dp,
        alpha = 0.2f
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header - red like iOS calendar
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = month.uppercase(),
                    color = IOSColors.Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            // Day number big
            Text(
                text = "$today",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 52.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = dayOfWeek,
                color = Color.White.copy(0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Weather Widget placeholder
@Composable
fun WeatherWidget(modifier: Modifier = Modifier) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        cornerRadius = 22.dp,
        alpha = 0.2f
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Seoul", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Text("Partly Cloudy", color = Color.White.copy(0.7f), fontSize = 14.sp)
                Text("H:18° L:9°", color = Color.White.copy(0.6f), fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("⛅", fontSize = 36.sp)
                Text("16°", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Thin)
            }
        }
    }
}
