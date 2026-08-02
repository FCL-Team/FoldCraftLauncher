package com.tungsten.fcl.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.tungsten.fcl.activity.compose.WebScreen
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.component.FCLActivity

/**
 * 通用内置浏览器（3.7 迁移 Compose/Miuix；批 3 开关已固化，旧 activity_web.xml View 路径已删除）。
 *
 * 挂 [WebScreen]（WebView 经 AndroidView 保留原生）。Intent extra "url" 必传契约不变。
 */
class WebActivity : FCLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.extras!!.getString("url")!!
        setContent {
            FCLTheme(this) {
                WebScreen(url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidUtils.clearWebViewCache(this)
    }
}
