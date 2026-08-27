package com.mio.download

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

/** 一条进行中的下载：title 为列表标题，task 提供进度/消息，executor 用于取消 */
data class DownloadTaskInfo(
    val id: Long,
    val title: String,
    val task: Task<*>,
    val executor: TaskExecutor
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

    private var idCounter = 0L

    @JvmStatic
    fun submit(title: String, task: Task<*>, executor: TaskExecutor): DownloadTaskInfo {
        val info = DownloadTaskInfo(++idCounter, title, task, executor)
        val wasEmpty = _tasks.value.isEmpty()
        _tasks.update { it + info }
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
                    _tasks.update { list -> list.filterNot { it.id == info.id } }
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

    @JvmStatic
    fun cancel(info: DownloadTaskInfo) {
        info.executor.cancel()
    }

    /** 任务列表变化后刷新前台通知；全部完成后移除通知并停止服务 */
    private fun refreshNotification() {
        val context = FCLApp.getAppContext()
        val tasks = _tasks.value
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

    /** 构建下载进度通知（服务 startForeground 与任务变化刷新共用） */
    internal fun buildNotification(context: Context, tasks: List<DownloadTaskInfo>): Notification {
        createNotificationChannel(context)
        // 聚合进度：所有任务 progress 的平均值
        val progressValues = tasks.map { it.task.progressProperty().get() }
        val indeterminate = progressValues.any { it < 0 }
        val percent =
            if (indeterminate) 0 else (progressValues.average() * 100).toInt().coerceIn(0, 100)
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
