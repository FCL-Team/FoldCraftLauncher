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
 * 每阶段一轮完整 JVM 生命周期。直启实现（launch 层）每阶段 main 返回后 DestroyJavaVM，
 * HotSpot 支持完整销毁后重建，因此不再需要旧架构"每阶段自杀换新进程"——
 * 这同时消除了 AMS 对同进程名反复创建进程的崩溃计数风险（process is bad）。
 *
 * 文件协议（启动标记/退出码，按会话 ID 命名）与主进程侧 InstallerProcessRunner 保持不变；
 * 若空闲自停与下一个 start 真正撞上，下一 start 会拉起全新进程，协议同样成立。
 */
class ProcessService : Service() {

    /**
     * 阶段串行执行器：主进程协议保证同一会话的阶段严格串行；
     * 异常的并发安装会话也只会在队列上等待，而非同进程内互踩两套 JVM
     */
    private val stageExecutor = Executors.newSingleThreadExecutor()

    private val handler = Handler(Looper.getMainLooper())

    /** 阶段结束后的空闲自停延时：足够主进程读走退出码并投递下一阶段 */
    private val idleStopDelayMs = 60_000L

    private val idleStopRunnable = Runnable { stopSelf() }

    /** 本进程最近一次服务的 JRE 版本；常驻 JVM 只能按创建时的 JRE 跑，版本切换必须换进程 */
    private var lastJava = -1

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
        Logging.LOG.info(
            "Installer process service started, java: $java, command: ${
                command?.joinToString(
                    " "
                )?.take(200)
            }"
        )
        if (lastJava != -1 && java != lastJava) {
            // 常驻 JVM 的字节码能力固定为创建时 JRE，JRE 降级/升级重试链需要全新进程：
            // 立即回收本进程，主进程侧 started 标记超时后会以新进程重试同一命令
            Logging.LOG.warning("JRE version changed ($lastJava -> $java), recycling :jvm process")
            Process.killProcess(Process.myPid())
            return START_NOT_STICKY
        }
        lastJava = java
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
        // launchAPIInstaller 抛出即本阶段启动失败：写退出码让主进程走重试/报错，
        // 进程与服务保留供后续阶段复用（不再自杀换进程）
        val bridge = try {
            FCLauncher.launchAPIInstaller(config)
        } catch (e: Throwable) {
            Logging.LOG.log(Level.WARNING, "Installer JVM failed to launch", e)
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
                // 阶段结束不自杀：JVM 已被完整销毁，进程常驻等待下一阶段复用；
                // 空闲超时后 stopSelf 交还前台服务（进程交系统按空进程策略回收，
                // 或下次 start 直接复用本进程，无需冷启）
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(idleStopRunnable)
        // 不杀进程：服务停止后进程由系统按空进程策略回收，下次 start 可直接复用
        stageExecutor.shutdownNow()
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
