package com.ioslauncher

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ioslauncher.ui.screens.HomeScreen
import com.ioslauncher.ui.theme.IOSLauncherTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge, transparent status/nav bars (iOS-style)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        // Keep screen on when launcher
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            IOSLauncherTheme {
                var showControlCenter by remember { mutableStateOf(false) }
                var showAppDrawer by remember { mutableStateOf(false) }

                HomeScreen(
                    showControlCenter = showControlCenter,
                    onShowControlCenter = { showControlCenter = true },
                    onHideControlCenter = { showControlCenter = false },
                    showAppDrawer = showAppDrawer,
                    onShowAppDrawer = { showAppDrawer = true },
                    onHideAppDrawer = { showAppDrawer = false }
                )
            }
        }
    }

    override fun onBackPressed() {
        // iOS-style: back button goes to home (do nothing on home)
    }
}
