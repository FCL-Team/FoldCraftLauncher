package com.tungsten.fcl.activity.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * Miuix 版内置浏览器页（3.7，对应 WebActivity + activity_web.xml）。
 *
 * 行为对齐（interaction-map §1.4）：
 * - WebView 本体保留原生（AndroidView 包装），JS 开启、LOAD_NO_CACHE 一致；
 * - onPageStarted 显示 / onPageFinished 隐藏居中进度（原 ProgressBar → CircularProgressIndicator）；
 * - 清 WebView 缓存仍在 Activity.onDestroy（AndroidUtils.clearWebViewCache），两条路径共用。
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebScreen(url: String) {
    var loading by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            loading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            loading = false
                        }
                    }
                    settings.javaScriptEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    loadUrl(url)
                }
            },
            onRelease = { it.destroy() },
        )
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
