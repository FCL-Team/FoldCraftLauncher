package com.tungsten.fcl.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.compose.setContent
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.compose.ComposeActivities
import com.tungsten.fcl.activity.compose.WebScreen
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcllibrary.component.FCLActivity

/**
 * 通用内置浏览器（3.7 迁移：Compose/Miuix 重写，带开关回滚）。
 *
 * [ComposeActivities.USE_COMPOSE_WEB] = true 时挂 [WebScreen]（WebView 经 AndroidView 保留原生）；
 * false 回滚遗留 activity_web.xml 路径。Intent extra "url" 必传契约不变。
 */
class WebActivity : FCLActivity() {

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.extras!!.getString("url")!!
        if (ComposeActivities.USE_COMPOSE_WEB) {
            setContent {
                FCLTheme(this) {
                    WebScreen(url)
                }
            }
        } else {
            setContentView(R.layout.activity_web)
            progressBar = findViewById(R.id.progress)
            webView = findViewById<WebView>(R.id.web_view).apply {
                webViewClient = WebViewTrackClient()
                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                loadUrl(url)
            }
        }
    }

    /** 遗留路径的进度回调（Compose 路径由 WebScreen 内部维护）。 */
    inner class WebViewTrackClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            progressBar?.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            progressBar?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidUtils.clearWebViewCache(this)
    }
}
