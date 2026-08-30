package com.mio.download

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 下载保活前台服务：仅承载前台通知与进程优先级，
 * 下载任务本身由 DownloadManager 与全局线程池执行。
 * 任务全部完成后由 DownloadManager 停止本服务。
 */
class DownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val tasks = DownloadManager.tasks.value
        startForeground(DownloadManager.NOTIFICATION_ID, DownloadManager.buildNotification(this, tasks))
        if (tasks.isEmpty()) {
            // 服务启动前任务已全部完成（极小任务快速完成）：立即移除通知并停止
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
        return START_NOT_STICKY
    }
}