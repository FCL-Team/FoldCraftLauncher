package com.tungsten.fcl.activity

import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.tungsten.fcl.ui.glass.FCLGlassApp
import com.tungsten.fcllibrary.component.FCLActivity
import com.tungsten.fcllibrary.component.view.FCLUILayout

/**
 * FCL 液态玻璃 UI 主入口。
 *
 * 作为 SplashActivity 之后的主 Activity，负责展示 Compose + Backdrop 构建的液态玻璃界面。
 * 以 FCLUILayout 为根容器，方便旧版页面在 Compose UI 上层添加视图。
 */
class GlassMainActivity : FCLActivity() {

    lateinit var layout: FCLUILayout
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        layout = FCLUILayout(this)
        val composeView = ComposeView(this).apply {
            setContent {
                FCLGlassApp()
            }
        }
        layout.addView(
            composeView,
            android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )
        setContentView(layout)
    }
}
