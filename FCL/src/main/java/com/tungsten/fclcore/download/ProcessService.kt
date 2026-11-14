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
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import kotlin.concurrent.thread

/**
 * 安装器宿主服务（:jvm 进程），常驻复用：安装的多个阶段在本进程内排队依次运行，
 * 首个阶段经 VMLauncher.launchJVM 创建 HotSpot 后不再销毁，后续阶段经
 * VMLauncher.runMainInExistingVM 在同一 JVM 里以独立 URLClassLoader 运行。
 * 阶段内出现 System.exit/abort 时 JVM option hook 停车并走 nominal_exit 结束进程，
 * 这属于正常退出路径，不是本服务管理的生命周期。
 *
 * 文件协议（启动标记/退出码，按会话 ID 命名）与主进程侧 InstallerProcessRunner 保持不变；
 * 常驻 JVM 的 JRE 由 [InstallerProcessRunner.JVM_STATE_FILE] 记录，主进程在启动下一阶段前
 * 读该文件预判 JRE 是否一致，不一致时主动回收进程——本服务内的静态校验仅作兜底。
 * 空闲自停时 onDestroy 主动结束进程释放常驻 JVM 内存，空闲期内到达的下一阶段直接复用本进程
 */
class ProcessService : Service() {

    /**
     * 阶段排队执行器：主进程协议保证同一会话的阶段严格串行；执行器只把各阶段的
     * launch 调用排队（真正的 JVM 工作在 bridge 线程上），并发安装会话的隔离
     * 依赖主进程侧协议
     */
    private val stageExecutor = Executors.newSingleThreadExecutor()

    private val handler = Handler(Looper.getMainLooper())

    /** 阶段结束后的空闲自停延时：足够主进程读走退出码并投递下一阶段 */
    private val idleStopDelayMs = 60_000L

    private val idleStopRunnable = Runnable { stopSelf() }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, buildNotification())
        FCLPath.loadPaths(this)
        val command = intent.extras!!.getStringArray("command")
        val java = intent.extras!!.getInt("java")
        val session = intent.extras!!.getString(InstallerProcessRunner.EXTRA_SESSION) ?: ""
        if (session.isEmpty()) {
            Logging.LOG.warning("Installer process service started without session id, ignoring")
            stopSelf()
            return START_NOT_STICKY
        }
        Logging.LOG.info(
            "Installer process service started, java: $java, command: ${
                command?.joinToString(
                    " "
                )?.take(200)
            }"
        )
        if (FCLauncher.hasResidentHotSpot() && FCLauncher.getHotSpotJavaVersion() != java) {
            // 兜底：主进程在 start 前已按状态文件回收过不匹配的常驻进程。这里仍保留
            // 校验，防止主进程预判与本次 start 之间进程状态被外部改动（竞态窗口）
            Logging.LOG.warning(
                "JRE version changed (${FCLauncher.getHotSpotJavaVersion()} -> $java), recycling :jvm process"
            )
            deleteJvmState()
            Process.killProcess(Process.myPid())
            return START_NOT_STICKY
        }
        // 记录启动标记，供主进程确认安装器服务已启动；复用进程时 pid 不变，
        // 主进程的 CPU 进度监控按同一 pid 持续有效
        try {
            FileUtils.writeText(
                File(applicationContext.cacheDir, InstallerProcessRunner.STARTED_FILE_PREFIX + session + ".txt"),
                Process.myPid().toString()
            )
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Can't write installer started marker", e)
        }
        stageExecutor.execute { runStage(session, command, java) }
        return START_NOT_STICKY
    }

    private fun runStage(session: String, command: Array<String>?, java: Int) {
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
        config.javaVersion = java
        // launchAPIInstaller 抛出即本阶段启动失败：写退出码让主进程走重试/报错，
        // 进程与服务保留供后续阶段复用（不再自杀换进程）
        val bridge = try {
            FCLauncher.launchAPIInstaller(config)
        } catch (e: Throwable) {
            Logging.LOG.log(Level.WARNING, "Installer JVM failed to launch", e)
            deleteJvmState()
            writeExitCode(session, -1)
            scheduleIdleStop()
            return
        }
        val firstLog = AtomicBoolean(true)
        val exitCodeWritten = AtomicBoolean(false)
        val callback: FCLBridgeCallback = object : FCLBridgeCallback {
            override fun onCursorModeChange(mode: Int) {
                // Ignore
            }

            override fun onLog(log: String?) {
                if (BuildConfig.DEBUG) {
                    Log.d("FCL Debug", log.toString())
                }
                try {
                    if (firstLog.getAndSet(false)) {
                        FileUtils.writeText(File(bridge.logPath), log)
                    } else {
                        FileUtils.writeTextWithAppendMode(File(bridge.logPath), log)
                    }
                } catch (e: IOException) {
                    Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.message)
                }
            }

            override fun onExit(code: Int) {
                if (exitCodeWritten.getAndSet(true)) {
                    return
                }
                writeExitCode(session, code)
                // 阶段结束不结束进程：JVM 保持常驻，主进程启动下一阶段时若 JRE 一致
                // 直接在本进程内复用。记录常驻 JVM 的 JRE，供主进程在 start 前预判
                if (FCLauncher.hasResidentHotSpot()) {
                    writeJvmState(java)
                } else {
                    deleteJvmState()
                }
                // 空闲超时后 stopSelf 交还前台服务，由 onDestroy 释放常驻 JVM
                scheduleIdleStop()
            }
        }
        handler.postDelayed(1000) {
            try {
                bridge.execute(null, callback)
            } catch (e: Throwable) {
                Logging.LOG.log(Level.WARNING, "Installer JVM failed to start", e)
                if (exitCodeWritten.getAndSet(true)) {
                    return@postDelayed
                }
                deleteJvmState()
                writeExitCode(session, -1)
                scheduleIdleStop()
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
                if (!exitCodeWritten.getAndSet(true)) {
                    Logging.LOG.warning("Installer JVM terminated unexpectedly, writing exit code -1")
                    deleteJvmState()
                    writeExitCode(session, -1)
                    scheduleIdleStop()
                }
            }
        }
    }

    private fun scheduleIdleStop() {
        handler.removeCallbacks(idleStopRunnable)
        handler.postDelayed(idleStopRunnable, idleStopDelayMs)
    }

    private fun writeExitCode(session: String, code: Int) {
        try {
            val cacheDir = applicationContext.cacheDir
            val exitCodeFile = File(cacheDir, InstallerProcessRunner.EXIT_CODE_FILE_PREFIX + session + ".txt")
            // 先写临时文件再改名，保证主进程读到的退出码文件内容完整
            val tmp = File(cacheDir, InstallerProcessRunner.EXIT_CODE_FILE_PREFIX + session + ".txt.tmp")
            FileUtils.writeText(tmp, code.toString())
            if (!tmp.renameTo(exitCodeFile)) {
                FileUtils.writeText(exitCodeFile, code.toString())
            }
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Can't write installer exit code file", e)
        }
    }

    private fun writeJvmState(java: Int) {
        try {
            val stateFile = File(applicationContext.cacheDir, InstallerProcessRunner.JVM_STATE_FILE)
            FileUtils.writeText(stateFile, "${Process.myPid()}:$java")
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Can't write installer JVM state file", e)
        }
    }

    private fun deleteJvmState() {
        File(applicationContext.cacheDir, InstallerProcessRunner.JVM_STATE_FILE).delete()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(idleStopRunnable)
        stageExecutor.shutdownNow()
        // 空闲自停后主动释放常驻 JVM 占用的内存；系统低内存回收时是 SIGKILL，
        // 不会走到这里，下次 start 新进程冷启即可
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
