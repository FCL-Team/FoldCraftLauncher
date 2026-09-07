package com.mio.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.download.DownloadTaskInfo
import com.tungsten.fclcore.task.FetchTask
import com.tungsten.fclcore.task.Schedulers
import com.mio.util.getScreenWidth
import com.tungsten.fcl.databinding.ViewDownloadPanelBinding
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

/** 下载管理卡片：从右侧滑入的圆角卡片，无遮罩，展示全局下载任务进度，后续可容纳登录等其他全局状态 */
class DownloadSlidePanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding: ViewDownloadPanelBinding =
        ViewDownloadPanelBinding.inflate(LayoutInflater.from(context), this, true)
    private val panel: View = binding.panel
    private val closeButton: ImageView = binding.close

    /** 全局速度事件处理：weak 注册要求调用方持有强引用，否则监听器会被 GC 回收失效 */
    private val speedHandler = Consumer { event: FetchTask.SpeedEvent ->
        Schedulers.androidUIThread().execute {
            speedText.setString(if (event.speed > 0) formatBytes(event.speed.toLong()) + "/s" else "")
        }
    }
    private val speedText = binding.speed
    private val adapter = DownloadListAdapter()
    var isOpen = false
        private set


    init {
        // 容器自身 elevation 需高于左右菜单（100dp），否则面板会被菜单盖住
        elevation = ConvertUtils.dip2px(context, 130f).toFloat()
        // 卡片：宽度约占 50% 屏宽、水平居中，高度铺满（四周留边）
        val margin = ConvertUtils.dip2px(context, 12f)
        panel.layoutParams = LayoutParams(
            (getScreenWidth() * 0.5f).toInt().coerceAtLeast(320),
            LayoutParams.MATCH_PARENT,
            Gravity.CENTER_HORIZONTAL or Gravity.TOP
        ).apply {
            leftMargin = margin
            topMargin = margin
            rightMargin = margin
            bottomMargin = margin
        }
        val list = binding.list
        list.layoutManager = LinearLayoutManager(context)
        // item 之间的垂直间隔
        val itemGap = ConvertUtils.dip2px(context, 8f)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: android.graphics.Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = itemGap
            }
        })
        list.adapter = adapter
        closeButton.setOnClickListener { close() }
        // 初始移出屏幕外，避免首次 layout 闪现
        post { panel.translationY = -height.toFloat() }
        visibility = GONE
        // 全局下载速度（每秒聚合一次），仅在面板打开时可见
        FetchTask.speedEvent
            .channel(FetchTask.SpeedEvent::class.java)
            .registerWeak(speedHandler)
        ThemeEngine.getInstance().registerEvent(this, ::refreshTheme)
        refreshTheme()
    }

    private fun refreshTheme() {
        panel.background = GradientDrawable().apply {
            cornerRadius = ConvertUtils.dip2px(context, 16f).toFloat()
            // 背景随主题模式：亮色取白色、深色取黑色
            setColor(if (ThemeEngine.isNightMode(context)) Color.BLACK else Color.WHITE)
        }
    }

    fun updateTasks(tasks: List<DownloadTaskInfo>) {
        adapter.submitList(tasks)
    }

    fun toggle() {
        if (isOpen) close() else open()
    }

    fun open() {
        if (isOpen) return
        isOpen = true
        visibility = VISIBLE
        panel.translationY = -height.toFloat()
        panel.animate().translationY(0f).setDuration(200).start()
    }

    fun close() {
        if (!isOpen) return
        isOpen = false
        panel.animate().translationY(-height.toFloat()).setDuration(200).withEndAction {
            visibility = GONE
            panel.translationY = -height.toFloat()
        }.start()
    }
}