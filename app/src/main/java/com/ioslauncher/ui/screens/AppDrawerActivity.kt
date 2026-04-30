package com.ioslauncher.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ioslauncher.ui.theme.IOSLauncherTheme

class AppDrawerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IOSLauncherTheme {
                AppDrawerScreen(onDismiss = { finish() })
            }
        }
    }
}
