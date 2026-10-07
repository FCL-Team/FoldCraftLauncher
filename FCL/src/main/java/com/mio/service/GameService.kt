package com.mio.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.JVMActivity

/**
 * 游戏会话保活前台服务：仅承载前台通知与进程优先级，
 * 避免游戏切后台后进程降为缓存级、因内存占用过高被系统优先回收。
 */
class GameService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundCompat()
        return START_NOT_STICKY
    }

    private fun startForegroundCompat() {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_videogame_asset_24)
            .setContentTitle(getString(R.string.game_service_notification_title))
            .setContentText(getString(R.string.game_service_notification_text))
            .setContentIntent(buildContentIntent())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
        // specialUse 类型仅 API 34+ 可识别，低版本传 0 走无类型前台服务
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else {
            0
        }
        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, type)
    }

    private fun buildContentIntent(): PendingIntent {
        val intent = Intent(this, JVMActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.game_service_channel),
            NotificationManager.IMPORTANCE_LOW
        )
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 1302
        private const val CHANNEL_ID = "game_session"

        /** 游戏界面创建时启动保活服务；须在应用处于前台时调用（Android 12+ 前台服务启动限制） */
        @JvmStatic
        fun start(context: Context) {
            context.startForegroundService(Intent(context, GameService::class.java))
        }

        /** 游戏界面销毁时停止服务并移除通知 */
        @JvmStatic
        fun stop(context: Context) {
            context.stopService(Intent(context, GameService::class.java))
        }
    }
}
