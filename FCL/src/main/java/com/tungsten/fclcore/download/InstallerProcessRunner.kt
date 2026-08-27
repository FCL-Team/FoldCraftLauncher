package com.tungsten.fclcore.download

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import com.tungsten.fcl.R
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.function.Consumer

/**
 * 安装器进程（:jvm）的统一运行器：负责清理残留进程、启动 ProcessService、
 * 轮询退出码与安装日志，供 Forge / NeoForge / OptiFine 等安装任务复用。
 */
object InstallerProcessRunner {

    /** 退出码文件名，位于 app 共享 cacheDir 下，主进程与 :jvm 进程均可访问 */
    const val EXIT_CODE_FILE = "fcl_process_exit_code.txt"

    /** 服务启动标记文件名，:jvm 进程进入 onStartCommand 时写入，用于确认服务确实启动 */
    const val STARTED_FILE = "fcl_process_started.txt"

    /** 安装器日志文件名，位于 FCLPath.LOG_DIR 下 */
    const val INSTALLER_LOG_FILE = "latest_api_installer.log"

    /** 单次安装器 JVM 运行的超时上限 */
    private const val JVM_TIMEOUT_MS = 5 * 60 * 1000L

    /** 等待 :jvm 服务启动标记出现的上限 */
    private const val SERVICE_START_TIMEOUT_MS = 15 * 1000L

    /** 退出码与日志的轮询间隔 */
    private const val POLL_INTERVAL_MS = 500L

    /** 服务启动重试次数 */
    private const val START_RETRY_TIMES = 5

    /** 进程级自动重试次数（首次正常运行之外）：超时后重启 :jvm 进程重跑同一命令 */
    private const val PROCESS_MAX_RETRIES = 1

    /** 两次尝试之间等待系统回收旧进程的时间 */
    private const val RETRY_DELAY_MS = 2 * 1000L

    /**
     * 超时类失败（进程无响应 / 未返回退出码 / 未能启动），可自动重试；
     * 其他失败（如退出码内容异常）不可重试。
     */
    private class ProcessTimeoutException(message: String) : IOException(message)

    /**
     * 启动安装器进程并等待其结束。
     *
     * @param command 安装器 JVM 参数（-cp … 主类 …）
     * @param java 使用的 JRE 版本（8/11/17/21）
     * @param onLog 增量回调安装器日志（每次轮询新增的完整行，按 \n 拼接）
     * @return 安装器退出码
     * @throws IOException 各次尝试均启动失败、超时无响应，或退出码内容异常
     */
    @JvmStatic
    @Throws(IOException::class)
    fun run(context: Context, command: Array<String>, java: Int, onLog: Consumer<String>): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val exitCodeFile = File(context.cacheDir, EXIT_CODE_FILE)
        val startedFile = File(context.cacheDir, STARTED_FILE)
        val logFile = File(FCLPath.LOG_DIR ?: throw IOException("FCLPath not initialized"), INSTALLER_LOG_FILE)

        var lastError: IOException? = null
        for (attempt in 0..PROCESS_MAX_RETRIES) {
            if (attempt > 0) {
                // 结束上一轮可能残留的安装器进程，等待系统回收后再重试
                killRemainingProcesses(activityManager, context.packageName)
                onLog.accept(
                    context.getString(
                        R.string.installer_process_retrying,
                        attempt + 1,
                        PROCESS_MAX_RETRIES + 1
                    )
                )
                Thread.sleep(RETRY_DELAY_MS)
            }
            try {
                return runOnce(context, activityManager, exitCodeFile, startedFile, logFile, command, java, onLog)
            } catch (e: ProcessTimeoutException) {
                lastError = e
            }
        }
        throw requireNotNull(lastError)
    }

    /** 单次尝试：清理遗留文件、启动服务并轮询退出码 */
    private fun runOnce(
        context: Context,
        activityManager: ActivityManager,
        exitCodeFile: File,
        startedFile: File,
        logFile: File,
        command: Array<String>,
        java: Int,
        onLog: Consumer<String>
    ): Int {
        // 清理上一轮遗留的退出码与日志文件
        // 注意：不主动杀 :jvm 进程 —— getRunningAppProcesses 里刚退出进程的条目
        // 有延迟才会移除，此间其 pid 可能已被系统复用给新进程，按旧列表杀进程会误杀
        exitCodeFile.delete()
        startedFile.delete()
        logFile.delete()

        startProcessService(context, command, java)

        var consumedChars = 0
        var serviceStarted = false
        val startedDeadline = System.currentTimeMillis() + SERVICE_START_TIMEOUT_MS
        val deadline = System.currentTimeMillis() + JVM_TIMEOUT_MS
        while (true) {
            // 增量读取安装日志
            val (lines, newConsumed) = readNewLogLines(logFile, consumedChars)
            if (lines.isNotEmpty()) {
                consumedChars = newConsumed
                onLog.accept(lines.joinToString("\n"))
            }

            // 退出码文件出现即结束（内容为空时说明刚创建、尚未写完，继续等待）
            if (exitCodeFile.exists()) {
                val text = FileUtils.readText(exitCodeFile).trim()
                val code = text.toIntOrNull()
                if (code != null) {
                    exitCodeFile.delete()
                    Logging.LOG.info("Installer process exited with code $code")
                    return code
                }
                if (text.isEmpty()) {
                    Thread.sleep(POLL_INTERVAL_MS)
                    continue
                }
                exitCodeFile.delete()
                throw IOException(context.getString(R.string.installer_exit_code_invalid, text))
            }

            // 确认 :jvm 服务确实启动（由 ProcessService.onStartCommand 写入启动标记）
            if (!serviceStarted) {
                if (startedFile.exists()) {
                    serviceStarted = true
                    Logging.LOG.info("Installer process service started")
                } else if (System.currentTimeMillis() > startedDeadline) {
                    throw ProcessTimeoutException(context.getString(R.string.installer_process_failed_to_start) + logTail(logFile, context))
                }
            }

            // 只以退出码文件为准，不依赖 getRunningAppProcesses 判断进程存活
            // （部分系统/ROM 不返回 :jvm 进程，会导致误判安装失败）
            if (System.currentTimeMillis() > deadline) {
                if (isJvmProcessAlive(activityManager, context.packageName)) {
                    killRemainingProcesses(activityManager, context.packageName)
                    throw ProcessTimeoutException(context.getString(R.string.installer_process_no_response) + logTail(logFile, context))
                }
                throw ProcessTimeoutException(context.getString(R.string.installer_process_no_exit_code) + logTail(logFile, context))
            }
            Thread.sleep(POLL_INTERVAL_MS)
        }
    }

    /** 读取日志文件末尾几行，附在超时错误消息中便于排查 */
    private fun logTail(logFile: File, context: Context): String {
        return try {
            val tail = FileUtils.readText(logFile).lines().takeLast(5).joinToString("\n")
            if (tail.isBlank()) "" else "\n" + context.getString(R.string.installer_recent_logs) + "\n$tail"
        } catch (e: Exception) {
            ""
        }
    }

    /** 启动 ProcessService，失败时短暂等待后重试 */
    private fun startProcessService(
        context: Context,
        command: Array<String>,
        java: Int
    ) {
        var lastError: Throwable? = null
        for (i in 0 until START_RETRY_TIMES) {
            try {
                val intent = Intent(context, ProcessService::class.java)
                    .putExtra("command", command)
                    .putExtra("java", java)
                context.startForegroundService(intent)
                return
            } catch (e: Throwable) {
                lastError = e
                if (i < START_RETRY_TIMES - 1) {
                    Thread.sleep(500L)
                }
            }
        }
        throw IOException(context.getString(R.string.installer_process_start_error), lastError)
    }

    /** 杀死本应用残留的 :jvm / :crash / 非主进程（不影响其他应用） */
    private fun killRemainingProcesses(activityManager: ActivityManager, packageName: String) {
        val selfPid = Process.myPid()
        activityManager.runningAppProcesses?.forEach { info ->
            if (info.pid != selfPid &&
                (info.processName == packageName || info.processName.startsWith("$packageName:"))
            ) {
                Process.killProcess(info.pid)
            }
        }
    }

    /** 判断 :jvm 安装器进程是否存活（仅用于超时后的处置决定，不作为失败依据） */
    private fun isJvmProcessAlive(activityManager: ActivityManager, packageName: String): Boolean {
        val processes = activityManager.runningAppProcesses ?: return false // 无法获取时视为不在，避免误杀
        return processes.any { it.processName == "$packageName:jvm" }
    }

    /**
     * 增量读取日志文件中新增的完整行。
     * 末尾无换行的半行暂不消费，等补齐后再读，避免截断 UTF-8 字符。
     */
    private fun readNewLogLines(logFile: File, consumedChars: Int): Pair<List<String>, Int> {
        if (!logFile.exists()) return emptyList<String>() to consumedChars
        val text = FileUtils.readText(logFile)
        val end = text.lastIndexOf('\n')
        if (end < 0 || end < consumedChars) return emptyList<String>() to consumedChars
        val lines = text.substring(consumedChars, end)
            .split('\n')
            .map { it.removeSuffix("\r") }
            .filter { it.isNotEmpty() }
        return lines to (end + 1)
    }
}