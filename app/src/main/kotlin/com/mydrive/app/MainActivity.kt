package com.mydrive.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.mydrive.app.ui.navigation.AppNavHost
import com.mydrive.app.ui.theme.MyDriveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        val app = application as MyDriveApp
        setContent {
            MyDriveTheme {
                AppNavHost(
                    repository = app.mediaRepository,
                    localMediaRepository = app.localMediaRepository
                )
            }
        }
    }
}
