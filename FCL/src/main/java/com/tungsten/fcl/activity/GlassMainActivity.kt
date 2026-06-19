package com.tungsten.fcl.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.tungsten.fcl.ui.glass.FCLGlassApp

/**
 * FCL 液态玻璃 UI 主入口。
 *
 * 作为 SplashActivity 之后的主 Activity，负责展示 Compose + Backdrop 构建的液态玻璃界面。
 */
class GlassMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FCLGlassApp()
        }
    }
}
