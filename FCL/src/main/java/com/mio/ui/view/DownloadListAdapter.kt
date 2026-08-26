package com.mio.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.mio.download.DownloadManager
import com.mio.download.DownloadTaskInfo
import com.tungsten.fcl.R
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.task.FileDownloadTask
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLProgressBar
import com.tungsten.fcllibrary.component.view.FCLTextView
import java.util.Locale
import kotlin.math.roundToInt

/** 下载管理面板的列表适配器：每行一个下载任务（文件名/进度文字/进度条/取消） */
class DownloadListAdapter(private val context: Context) : BaseAdapter() {

    private var items: List<DownloadTaskInfo> = emptyList()

    fun setItems(items: List<DownloadTaskInfo>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getCount() = items.size

    override fun getItem(position: Int): DownloadTaskInfo = items[position]

    override fun getItemId(position: Int): Long = items[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_download_task, parent, false)
        val holder = (view.tag as? ViewHolder) ?: ViewHolder(view).also { view.tag = it }
        holder.bind(items[position])
        return view
    }

    /** 进度文字：有字节信息的文件下载显示"已下载/总量 (百分比)"，其余显示百分比 */
    private fun progressText(task: Task<*>, value: Double): String {
        val percentText = if (value >= 0) "${(value * 100).roundToInt()}%" else "..."
        if (task is FileDownloadTask && task.totalBytes > 0) {
            return "${formatBytes(task.downloadedBytes)} / ${formatBytes(task.totalBytes)} ($percentText)"
        }
        return percentText
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024L * 1024 * 1024 -> String.format(Locale.US, "%.2f GB", bytes / 1024.0 / 1024.0 / 1024.0)
            bytes >= 1024L * 1024 -> String.format(Locale.US, "%.1f MB", bytes / 1024.0 / 1024.0)
            bytes >= 1024L -> String.format(Locale.US, "%.0f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }

    private inner class ViewHolder(view: View) {
        private val title: FCLTextView = view.findViewById(R.id.title)
        private val percent: FCLTextView = view.findViewById(R.id.percent)
        private val progress: FCLProgressBar = view.findViewById(R.id.progress)
        private val state: FCLTextView = view.findViewById(R.id.state)
        private val cancel: ImageView = view.findViewById(R.id.cancel)

        private var current: DownloadTaskInfo? = null
        private val progressListener = ChangeListener<Number> { _, _, newValue ->
            val value = newValue.toDouble()
            // 进度条仅作视觉展示，准确进度以文字为准
            progress.percentProgressProperty().set(value)
            current?.let { percent.string = progressText(it.task, value) }
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
            title.string = info.title
            val value = info.task.progressProperty().get()
            progress.percentProgressProperty().set(value)
            percent.string = progressText(info.task, value)
            val message = info.task.messageProperty().get()
            if (message.isNullOrEmpty()) {
                state.visibilityValue = false
            } else {
                state.string = message
                state.visibilityValue = true
            }
            cancel.setColorFilter(ThemeEngine.getInstance().getTheme().getColor2())
            cancel.setOnClickListener { DownloadManager.cancel(info) }
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
}