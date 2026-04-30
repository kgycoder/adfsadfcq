package com.ioslauncher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ioslauncher.data.AppInfo
import com.ioslauncher.data.AppRepository
import com.ioslauncher.ui.components.*
import com.ioslauncher.ui.theme.IOSColors
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    showControlCenter: Boolean,
    onShowControlCenter: () -> Unit,
    onHideControlCenter: () -> Unit,
    showAppDrawer: Boolean,
    onShowAppDrawer: () -> Unit,
    onHideAppDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val allApps = remember { AppRepository.getInstalledApps(context) }
    val dockApps = remember { AppRepository.getDockApps(context) }

    // Split apps into pages of 20 (4x5 iOS grid)
    val pages = remember(allApps) {
        allApps.chunked(20)
    }
    val pagerState = rememberPagerState { pages.size }

    Box(modifier = Modifier.fillMaxSize()) {
        // Wallpaper layer - gradient fallback
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1a1a2e),
                            Color(0xFF16213e),
                            Color(0xFF0f3460),
                            Color(0xFF533483)
                        )
                    )
                )
        )

        // Main content
        Column(modifier = Modifier.fillMaxSize()) {
            // Status bar
            IOSStatusBar()

            // Widget row (page 0 only)
            AnimatedVisibility(visible = pagerState.currentPage == 0) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ClockWidget()
                        CalendarWidget()
                    }
                    Spacer(Modifier.height(12.dp))
                    WeatherWidget()
                    Spacer(Modifier.height(12.dp))
                }
            }

            // Pages of app icons
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                AppGrid(
                    apps = pages.getOrElse(page) { emptyList() },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Page indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { page ->
                    val isSelected = pagerState.currentPage == page
                    val size by animateDpAsState(
                        if (isSelected) 8.dp else 6.dp,
                        label = "dotSize"
                    )
                    val alpha by animateFloatAsState(
                        if (isSelected) 1f else 0.4f,
                        label = "dotAlpha"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(size)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = alpha))
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Dock
            IOSDock(
                apps = dockApps,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(Modifier.height(8.dp))
        }

        // Gesture: swipe up → App Drawer, swipe down from top → Control Center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        change.consume()
                        when {
                            dragAmount < -60 && !showAppDrawer -> onShowAppDrawer()
                            dragAmount > 60 && !showControlCenter -> { /* handled by status bar */ }
                        }
                    }
                }
        )

        // Status bar swipe-down zone → Control Center
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(160.dp)
                .height(50.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 30) onShowControlCenter()
                    }
                }
        )

        // Control Center overlay
        AnimatedVisibility(
            visible = showControlCenter,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            ControlCenter(onDismiss = onHideControlCenter)
        }

        // App Drawer overlay
        AnimatedVisibility(
            visible = showAppDrawer,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            AppDrawerScreen(onDismiss = onHideAppDrawer)
        }
    }
}

@Composable
fun AppGrid(
    apps: List<AppInfo>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Top
    ) {
        apps.chunked(4).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { app ->
                    AppIcon(
                        app = app,
                        showLabel = true,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                // Fill empty spots
                repeat(4 - row.size) {
                    Spacer(Modifier.size(72.dp))
                }
            }
        }
    }
}
