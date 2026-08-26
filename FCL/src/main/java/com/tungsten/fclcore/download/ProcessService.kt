package com.tungsten.fclcore.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.postDelayed
import com.mio.data.Renderer
import com.tungsten.fcl.BuildConfig
import com.tungsten.fcl.R
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.FCLauncher
import com.tungsten.fclauncher.bridge.FCLBridgeCallback
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.logging.Level
import kotlin.concurrent.thread

class ProcessService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, buildNotification())
        FCLPath.loadPaths(this)
        val command = intent.extras!!.getStringArray("command")
        val java = intent.extras!!.getInt("java")
        Logging.LOG.info(
            "Installer process service started, java: $java, command: ${
                command?.joinToString(
                    " "
                )?.take(200)
            }"
        )
        // 记录启动标记，供主进程确认安装器服务已启动
        try {
            FileUtils.writeText(
                File(applicationContext.cacheDir, InstallerProcessRunner.STARTED_FILE),
                Process.myPid().toString()
            )
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Can't write installer started marker", e)
        }
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
            command ?: emptyArray()
        )
        startProcess(config)
        return START_NOT_STICKY
    }

    private var firstLog = true

    /** 是否已通过 onExit 写入退出码，供守护线程判断 JVM 是否异常终止 */
    @Volatile
    private var exitCodeWritten = false

    fun startProcess(config: FCLConfig) {
        val bridge = FCLauncher.launchAPIInstaller(config)
        val callback: FCLBridgeCallback = object : FCLBridgeCallback {
            override fun onCursorModeChange(mode: Int) {
                // Ignore
            }

            override fun onLog(log: String?) {
                if (BuildConfig.DEBUG) {
                    Log.d("FCL Debug", log.toString())
                }
                try {
                    if (firstLog) {
                        FileUtils.writeText(File(bridge.logPath), log)
                        firstLog = false
                    } else {
                        FileUtils.writeTextWithAppendMode(File(bridge.logPath), log)
                    }
                } catch (e: IOException) {
                    Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.message)
                }
            }

            override fun onExit(code: Int) {
                exitCodeWritten = true
                writeExitCode(code)
                // 写完退出码后立即自杀：进程必须马上回收，否则下一个 startForegroundService
                // 会被系统分发给本进程里存活的 service 实例（而非新建进程），
                // 延迟自杀还会误杀复用后的新任务。
                // 不能调 stopSelf()：其 stop 请求可能与主进程的下一个 start 请求在系统侧乱序。
                Process.killProcess(Process.myPid())
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(1000) {
            try {
                bridge.execute(null, callback)
            } catch (e: Throwable) {
                Logging.LOG.log(Level.WARNING, "Installer JVM failed to start", e)
                writeExitCode(-1)
                Process.killProcess(Process.myPid())
                return@postDelayed
            }
            // 安装器 JVM 在 bridge 的 launch 线程上运行；该线程结束而退出码尚未写入时，
            // 说明 JVM 异常终止（FCLauncher 启动失败只 printStackTrace），补写退出码避免主进程干等
            thread {
                try {
                    bridge.thread?.join()
                } catch (e: InterruptedException) {
                    // ignore
                }
                if (!exitCodeWritten) {
                    Logging.LOG.warning("Installer JVM terminated unexpectedly, writing exit code -1")
                    writeExitCode(-1)
                    // 随后自杀，避免 :jvm 残留被下一个 startForegroundService 复用
                    Process.killProcess(Process.myPid())
                }
            }
        }
    }

    private fun writeExitCode(code: Int) {
        try {
            val cacheDir = applicationContext.cacheDir
            val exitCodeFile = File(cacheDir, InstallerProcessRunner.EXIT_CODE_FILE)
            // 先写临时文件再改名，保证主进程读到的退出码文件内容完整
            val tmp = File(cacheDir, InstallerProcessRunner.EXIT_CODE_FILE + ".tmp")
            FileUtils.writeText(tmp, code.toString())
            if (!tmp.renameTo(exitCodeFile)) {
                FileUtils.writeText(exitCodeFile, code.toString())
            }
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Can't write installer exit code file", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Process.killProcess(Process.myPid())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "fcl_process",
            "FCL Process",
            NotificationManager.IMPORTANCE_HIGH
        )
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "fcl_process")
            .setContentTitle(getString(R.string.notification_title))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}
