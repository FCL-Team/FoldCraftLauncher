package com.mio.ui.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import com.mio.download.DownloadTaskInfo
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.ConvertUtils

/** 下载管理卡片：从右侧滑入的圆角卡片，无遮罩，展示全局下载任务进度，后续可容纳登录等其他全局状态 */
class DownloadSlidePanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val panel: View
    private val closeButton: ImageView
    private val adapter = DownloadListAdapter(context)
    var isOpen = false
        private set

    /** 面板开关状态变化回调（用于同步左菜单开关按钮的高亮） */
    var onOpenChanged: ((Boolean) -> Unit)? = null

    init {
        // 容器自身 elevation 需高于左右菜单（100dp），否则面板会被菜单盖住
        elevation = ConvertUtils.dip2px(context, 130f).toFloat()
        inflate(context, R.layout.view_download_panel, this)
        panel = findViewById(R.id.panel)
        // 卡片：宽度约占 30% 屏宽，高度铺满（四周留边）
        val margin = ConvertUtils.dip2px(context, 12f)
        panel.layoutParams = LayoutParams(
            (getScreenWidth() * 0.3f).toInt().coerceAtLeast(320),
            LayoutParams.MATCH_PARENT,
            Gravity.END
        ).apply {
            leftMargin = margin
            topMargin = margin
            rightMargin = margin
            bottomMargin = margin
        }
        findViewById<ListView>(R.id.list).adapter = adapter
        closeButton = findViewById(R.id.close)
        closeButton.setOnClickListener { close() }
        // 初始移出屏幕外，避免首次 layout 闪现
        post { panel.translationX = width.toFloat() }
        visibility = GONE
        ThemeEngine.getInstance().registerEvent(this, ::refreshTheme)
        refreshTheme()
    }

    private fun refreshTheme() {
        panel.background = GradientDrawable().apply {
            cornerRadius = ConvertUtils.dip2px(context, 16f).toFloat()
            setColor(ThemeEngine.getInstance().getTheme().ltColor)
        }
    }

    fun updateTasks(tasks: List<DownloadTaskInfo>) {
        adapter.setItems(tasks)
    }

    fun toggle() {
        if (isOpen) close() else open()
    }

    fun open() {
        if (isOpen) return
        isOpen = true
        onOpenChanged?.invoke(true)
        visibility = VISIBLE
        panel.translationX = width.toFloat()
        panel.animate().translationX(0f).setDuration(200).start()
    }

    fun close() {
        if (!isOpen) return
        isOpen = false
        onOpenChanged?.invoke(false)
        panel.animate().translationX(width.toFloat()).setDuration(200).withEndAction {
            visibility = GONE
            panel.translationX = width.toFloat()
        }.start()
    }
}