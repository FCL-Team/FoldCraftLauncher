package com.tungsten.fclcore.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Process
import androidx.core.app.NotificationCompat
import com.mio.data.Renderer
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.FCLauncher
import com.tungsten.fclauncher.bridge.FCLBridgeCallback
import com.tungsten.fclcore.R
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.logging.Level

class ProcessService : Service() {
    companion object {
        const val PROCESS_SERVICE_PORT: Int = 29118
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, buildNotification())
        val command = intent.extras!!.getStringArray("command")
        val java = intent.extras!!.getInt("java")
        val jre = "jre$java"
        val config = FCLConfig(
            applicationContext,
            Environment.getExternalStorageDirectory().absolutePath + "/FCL/log",
            applicationContext.getDir("runtime", 0).absolutePath + "/java/" + jre,
            applicationContext.cacheDir.toString() + "/fclauncher",
            Renderer(
                "Holy-GL4ES",
                "",
                "libgl4es_114.so",
                "libEGL.so",
                "",
                null,
                null,
                Renderer.ID_GL4ES,
                "",
                ""
            ),
            command
        )
        startProcess(config)
        return START_NOT_STICKY
    }

    private var firstLog = true

    fun startProcess(config: FCLConfig) {
        val bridge = FCLauncher.launchAPIInstaller(config)
        val callback: FCLBridgeCallback = object : FCLBridgeCallback {
            override fun onCursorModeChange(mode: Int) {
                // Ignore
            }

            override fun onHitResultTypeChange(type: Int) {
                // Ignore
            }

            override fun onLog(log: String?) {
                try {
                    if (firstLog) {
                        FileUtils.writeText(File(bridge.logPath), log + "\n")
                        firstLog = false
                    } else {
                        FileUtils.writeTextWithAppendMode(File(bridge.logPath), log + "\n")
                    }
                } catch (e: IOException) {
                    Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.message)
                }
            }

            override fun onExit(code: Int) {
                sendCode(code)
            }
        }
        val handler = Handler()
        handler.postDelayed(Runnable { bridge.execute(null, callback) }, 1000)
    }

    private fun sendCode(code: Int) {
        try {
            val socket = DatagramSocket()
            socket.connect(InetSocketAddress("127.0.0.1", PROCESS_SERVICE_PORT))
            val data = (code.toString() + "").toByteArray()
            val packet = DatagramPacket(data, data.size)
            socket.send(packet)
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Process.killProcess(Process.myPid())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "fcl-process",
            "FCL Process",
            NotificationManager.IMPORTANCE_HIGH
        )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "fcl-process")
            .setContentTitle(getString(R.string.notification_title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}
