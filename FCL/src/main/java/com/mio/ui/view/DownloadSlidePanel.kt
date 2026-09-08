package com.mio.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.download.DownloadTaskInfo
import com.tungsten.fcl.databinding.ViewDownloadPanelBinding
import com.tungsten.fclcore.task.FetchTask
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fcllibrary.util.ConvertUtils
import java.util.function.Consumer

/** 右菜单列内的下载管理内容视图：标题/速度/任务列表；列内容的展开收起动画由持有方（MainActivity）编排 */
class DownloadSlidePanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val binding: ViewDownloadPanelBinding =
        ViewDownloadPanelBinding.inflate(LayoutInflater.from(context), this, true)
    private val closeButton: ImageView = binding.close
    private val speedText = binding.speed

    /** ✕ 点击请求收起（由持有方决定是否/如何收起面板） */
    var onCloseRequest: (() -> Unit)? = null

    /** 全局速度事件处理：weak 注册要求调用方持有强引用，否则监听器会被 GC 回收失效 */
    private val speedHandler = Consumer { event: FetchTask.SpeedEvent ->
        Schedulers.androidUIThread().execute {
            speedText.string = if (event.speed > 0) formatBytes(event.speed.toLong()) + "/s" else ""
        }
    }
    private val adapter = DownloadListAdapter()

    init {
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
        closeButton.setOnClickListener { onCloseRequest?.invoke() }
        // 全局下载速度（每秒聚合一次），仅面板可见时展示
        FetchTask.speedEvent
            .channel(FetchTask.SpeedEvent::class.java)
            .registerWeak(speedHandler)
    }

    fun updateTasks(tasks: List<DownloadTaskInfo>) {
        adapter.submitList(tasks)
    }
}
