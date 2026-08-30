package com.mio.download

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mio.util.acquireDownloadWakeLock
import com.mio.util.releaseDownloadWakeLock
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 一条进行中的下载：title 为列表标题，task 提供进度/消息，executor 用于取消。
 * installAction 非空时表示"完成后待手动安装"的条目（整合包场景）：
 * 任务结束后保留在面板中，用户点击条目执行 installAction 并移除，
 * 点击丢弃按钮执行 cleanupAction（清理临时文件）并移除。
 */
data class DownloadTaskInfo(
    val id: Long,
    val title: String,
    val task: Task<*>,
    val executor: TaskExecutor,
    val installAction: Runnable? = null,
    val cleanupAction: Runnable? = null,
    /** 下载成功结束、可以执行安装（仅待安装条目使用） */
    val ready: Boolean = false
)

/**
 * 全局下载管理器：非阻塞下载的注册中心。
 * 简单文件下载不再弹模态对话框，改为提交到此处，主界面面板实时显示进度并可取消；
 * 下载期间启动前台服务（进度通知）保活，后台/锁屏不中断；
 * 任务链结束（成功/失败/取消）自动移除并在全部结束后停止服务。
 */
object DownloadManager {

    internal const val NOTIFICATION_ID = 1301
    private const val CHANNEL_ID = "download"

    private val _tasks = MutableStateFlow<List<DownloadTaskInfo>>(emptyList())
    val tasks: StateFlow<List<DownloadTaskInfo>> = _tasks.asStateFlow()

    /** 全部任务的聚合进度（0..1；-1 表示暂无有效进度） */
    private val _progress = MutableStateFlow(-1f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private var idCounter = 0L

    /** 最近一次"已开始下载"提示，连续提交时取消旧的仅显示最新，避免 Toast 排队刷屏 */
    private var lastStartToast: Toast? = null

    /** 每次添加任务时的轻提示，让用户确认下载已开始 */
    private fun showStartedToast(context: Context, title: String) {
        lastStartToast?.cancel()
        val toast = Toast.makeText(
            context,
            "$title · ${context.getString(R.string.message_downloading)}",
            Toast.LENGTH_SHORT
        )
        lastStartToast = toast
        toast.show()
    }

    @JvmStatic
    fun submit(title: String, task: Task<*>, executor: TaskExecutor): DownloadTaskInfo {
        return submit(title, task, executor, null, null)
    }

    /** 提交一个"完成后待手动安装"的下载（整合包场景），见 [DownloadTaskInfo] */
    @JvmStatic
    fun submit(
        title: String,
        task: Task<*>,
        executor: TaskExecutor,
        installAction: Runnable?,
        cleanupAction: Runnable?
    ): DownloadTaskInfo {
        val info = DownloadTaskInfo(++idCounter, title, task, executor, installAction, cleanupAction)
        val wasEmpty = _tasks.value.isEmpty()
        _tasks.update { it + info }
        showStartedToast(FCLApp.getAppContext(), title)
        if (wasEmpty) {
            // 从无任务变为有任务：启动前台服务保活，保证后台下载不中断
            try {
                ContextCompat.startForegroundService(
                    FCLApp.getAppContext(),
                    Intent(FCLApp.getAppContext(), DownloadService::class.java)
                )
            } catch (_: Exception) {
                // 后台启动前台服务被系统限制时降级：仅持唤醒锁继续下载
            }
        }
        // 进度变化时刷新通知
        val progressListener = ChangeListener<Number> { _, _, _ -> refreshNotification() }
        task.progressProperty().addListener(progressListener)
        acquireDownloadWakeLock()
        executor.addTaskListener(object : TaskListener() {
            override fun onStop(success: Boolean, executor: TaskExecutor) {
                Schedulers.androidUIThread().execute {
                    task.progressProperty().removeListener(progressListener)
                    // 待安装条目（整合包）成功完成后置为 ready 并保留在面板中，由用户手动安装或丢弃；
                    // 失败/取消的条目照旧移除（临时文件由调用方清理）
                    if (info.installAction == null || !success) {
                        _tasks.update { list -> list.filterNot { it.id == info.id } }
                    } else {
                        _tasks.update { list ->
                            list.map { if (it.id == info.id) it.copy(ready = true) else it }
                        }
                    }
                    releaseDownloadWakeLock()
                    // 注意：不要在 onStop 回调里 removeTaskListener——AsyncTaskExecutor
                    // 正并发遍历 listener 列表（非线程安全 ArrayList），多任务取消时触发 CME。
                    // onStop 是任务链的最终回调，executor 随链结束被回收，无需移除。
                    refreshNotification()
                }
            }
        })
        return info
    }

    /** 执行待安装条目的安装动作并从面板移除 */
    @JvmStatic
    fun install(info: DownloadTaskInfo) {
        if (_tasks.value.any { it.id == info.id }) {
            _tasks.update { list -> list.filterNot { it.id == info.id } }
            info.installAction?.run()
        }
    }

    /** 丢弃待安装条目：执行清理动作（删除临时文件等）并从面板移除 */
    @JvmStatic
    fun discard(info: DownloadTaskInfo) {
        if (_tasks.value.any { it.id == info.id }) {
            _tasks.update { list -> list.filterNot { it.id == info.id } }
            info.cleanupAction?.run()
        }
    }

    @JvmStatic
    fun cancel(info: DownloadTaskInfo) {
        info.executor.cancel()
    }

    /** 任务列表变化后刷新前台通知与聚合进度；全部完成后移除通知并停止服务 */
    private fun refreshNotification() {
        val context = FCLApp.getAppContext()
        val tasks = _tasks.value
        _progress.value = aggregateProgress(tasks)
        if (tasks.isEmpty()) {
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
            context.stopService(Intent(context, DownloadService::class.java))
        } else if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, buildNotification(context, tasks))
        }
    }

    /** 聚合进度：忽略尚未开始（-1）的任务，用有效进度平均；全部无效时返回 -1 */
    private fun aggregateProgress(tasks: List<DownloadTaskInfo>): Float {
        // 待安装（已完成）条目按 100% 计入：进度更新有节流，结束时可能停在最后一次上报值
        val validValues = tasks.map {
            if (it.ready) 1.0 else it.task.progressProperty().get()
        }.filter { it >= 0 }
        return if (validValues.isEmpty()) -1f
        else validValues.average().toFloat().coerceIn(0f, 1f)
    }

    /** 构建下载进度通知（服务 startForeground 与任务变化刷新共用） */
    internal fun buildNotification(context: Context, tasks: List<DownloadTaskInfo>): Notification {
        createNotificationChannel(context)
        val aggregate = aggregateProgress(tasks)
        val indeterminate = aggregate < 0
        val percent =
            if (indeterminate) 0 else (aggregate * 100).toInt().coerceIn(0, 100)
        val first = tasks.first()
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_download_24)
            .setContentTitle(context.getString(R.string.download_manager))
            .setContentText("${first.title} $percent%")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, percent, indeterminate)
            .build()
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.download_manager),
            NotificationManager.IMPORTANCE_LOW
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
