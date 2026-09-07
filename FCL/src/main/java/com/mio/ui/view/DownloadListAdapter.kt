package com.mio.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mio.download.DownloadManager
import com.mio.download.DownloadTaskInfo
import com.tungsten.fcl.R
import com.tungsten.fcl.databinding.ItemDownloadTaskBinding
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Task
import java.util.Locale
import kotlin.math.roundToInt

/** 下载管理面板的列表适配器：每行一个下载任务（文件名/进度文字/进度条/取消） */
class DownloadListAdapter : ListAdapter<DownloadTaskInfo, DownloadListAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDownloadTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /** 进度文字：有字节信息的文件下载显示"已下载/总量 (百分比)"，其余显示百分比 */
    private fun progressText(task: Task<*>, value: Double): String {
        val percentText = if (value >= 0) "${(value * 100).roundToInt()}%" else "..."
        if (task is FileDownloadTask && task.totalBytes > 0) {
            return "${formatBytes(task.downloadedBytes)} / ${formatBytes(task.totalBytes)} ($percentText)"
        }
        return percentText
    }

    inner class ViewHolder(
        private val binding: ItemDownloadTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.title
        private val percent = binding.percent
        private val progress = binding.progress
        private val state = binding.state
        private val cancel = binding.cancel

        private var current: DownloadTaskInfo? = null
        private val progressListener = ChangeListener<Number> { _, _, newValue ->
            val value = newValue.toDouble()
            // 进度条仅作视觉展示，准确进度以文字为准
            progress.percentProgressProperty().set(value)
            val info = current ?: return@ChangeListener
            // 下载完成后进入待安装态，不再显示进度文字
            if (!info.ready)
                percent.string = progressText(info.task, value)
        }
        private val messageListener = ChangeListener<String> { _, _, newValue ->
            if (newValue.isNullOrEmpty()) {
                state.visibilityValue = false
            } else {
                state.string = newValue
                state.visibilityValue = true
            }
        }

        fun bind(info: DownloadTaskInfo) {
            unbind()
            current = info
            val root = binding.root
            title.string = info.title
            // 仅在任务成功结束后才进入"待安装"态，下载中仍显示进度
            val ready = info.ready
            if (ready) {
                // 待安装：条目点击执行安装，✕ 执行丢弃（清理临时文件）
                progress.percentProgressProperty().set(1.0)
                percent.string = root.context.getString(R.string.download_ready_to_install)
                cancel.setOnClickListener { DownloadManager.discard(info) }
                root.setOnClickListener { DownloadManager.install(info) }
            } else {
                val value = info.task.progressProperty().get()
                progress.percentProgressProperty().set(value)
                percent.string = progressText(info.task, value)
                cancel.setOnClickListener { DownloadManager.cancel(info) }
                root.setOnClickListener(null)
            }
            val message = info.task.messageProperty().get()
            if (message.isNullOrEmpty()) {
                state.visibilityValue = false
            } else {
                state.string = message
                state.visibilityValue = true
            }
            info.task.progressProperty().addListener(progressListener)
            info.task.messageProperty().addListener(messageListener)
        }

        /** 条目被复用前移除旧任务监听，防止更新被回收的视图 */
        private fun unbind() {
            current?.task?.progressProperty()?.removeListener(progressListener)
            current?.task?.messageProperty()?.removeListener(messageListener)
            current = null
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<DownloadTaskInfo>() {
            override fun areItemsTheSame(
                oldItem: DownloadTaskInfo,
                newItem: DownloadTaskInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DownloadTaskInfo,
                newItem: DownloadTaskInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

/** 字节数格式化为可读速度/大小文本 */
internal fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1024L * 1024 * 1024 -> String.format(Locale.US, "%.2f GB", bytes / 1024.0 / 1024.0 / 1024.0)
        bytes >= 1024L * 1024 -> String.format(Locale.US, "%.1f MB", bytes / 1024.0 / 1024.0)
        bytes >= 1024L -> String.format(Locale.US, "%.0f KB", bytes / 1024.0)
        else -> "$bytes B"
    }
}
