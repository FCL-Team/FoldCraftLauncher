package com.tungsten.fclcore.download

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import com.tungsten.fcl.R
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.CancellationException
import java.util.function.Consumer
import java.util.function.LongConsumer
import java.util.function.Supplier

/**
 * 安装器进程（:jvm）的统一运行器：负责清理残留进程、启动 ProcessService、
 * 轮询退出码与安装日志，供 Forge / NeoForge / OptiFine 等安装任务复用。
 *
 * 等待过程协程化：轮询循环为挂起循环，每个间隔检查任务的取消供应商，
 * 取消时立即杀掉 :jvm 进程并以 CancellationException 结束，任务取消不再被安装器吞掉。
 */
object InstallerProcessRunner {

    /** 退出码文件名前缀，位于 app 共享 cacheDir 下，主进程与 :jvm 进程均可访问；文件名以会话 ID 结尾 */
    const val EXIT_CODE_FILE_PREFIX = "fcl_process_exit_code_"

    /** 服务启动标记文件名前缀，:jvm 进程进入 onStartCommand 时写入，用于确认服务确实启动；文件名以会话 ID 结尾 */
    const val STARTED_FILE_PREFIX = "fcl_process_started_"

    /** ProcessService 接收会话 ID 的 extra 键，同一会话内主进程与 :jvm 进程读写同名文件 */
    const val EXTRA_SESSION = "installer_session"

    /** 安装器日志文件名，位于 FCLPath.LOG_DIR 下 */
    const val INSTALLER_LOG_FILE = "latest_api_installer.log"

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

    /** 单次尝试内日志无任何新增且未返回退出码的空闲判定上限。仅作为
     *  /proc CPU 进度不可用时的退化判据（个别 ROM 限制读取 procfs） */
    private const val LOG_IDLE_TIMEOUT_MS = 10 * 60 * 1000L

    /** 单次尝试内进程无进展（CPU 时间与日志均无变化）的判定上限，超过即认为进程挂起。
     *  FART 等静默处理器运行中 CPU 时间持续增长，不会被误杀；
     *  被系统 SIGKILL 而退出码未回写的进程也会在此收敛 */
    private const val NO_PROGRESS_TIMEOUT_MS = 3 * 60 * 1000L

    /** 单次尝试的绝对时限：即使进程仍在推进，超过也判为挂起，防止静默死循环 */
    private const val JVM_HARD_TIMEOUT_MS = 15 * 60 * 1000L

    /**
     * 超时类失败（进程无响应 / 未返回退出码 / 未能启动），可自动重试；
     * 其他失败（如退出码内容异常）不可重试。
     */
    private class ProcessTimeoutException(message: String) : IOException(message)

    /**
     * 启动安装器进程并等待其结束（Java 桥接：在调用线程上阻塞运行协程）。
     *
     * @param command 安装器 JVM 参数（-cp … 主类 …）
     * @param java 使用的 JRE 版本（8/11/17/21）
     * @param isCancelled 取消供应商，轮询间隔内检查，返回 true 时杀掉 :jvm 进程并中止
     * @param onLog 增量回调安装器日志（每次轮询新增的完整行，按 \n 拼接）
     * @param onTick 每秒回调当前尝试已运行毫秒数，供任务消息显示耗时
     * @return 安装器退出码
     * @throws IOException 各次尝试均启动失败、超时无响应，或退出码内容异常
     * @throws CancellationException isCancelled 返回 true（任务已取消）
     */
    @JvmStatic
    fun run(
        context: Context,
        command: Array<String>,
        java: Int,
        isCancelled: Supplier<Boolean>,
        onLog: Consumer<String>,
        onTick: LongConsumer,
    ): Int = runBlocking {
        runSuspending(context, command, java, { isCancelled.get() }, onLog, onTick)
    }

    /**
     * 挂起版运行器：语义与 [run] 相同，供协程内直接调用。
     */
    suspend fun runSuspending(
        context: Context,
        command: Array<String>,
        java: Int,
        isCancelled: () -> Boolean,
        onLog: Consumer<String>,
        onTick: LongConsumer,
    ): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 会话 ID 隔离退出码与启动标记文件：取消后立即重试的场景下，
        // 新旧运行不再共用固定名文件，杜绝读到上一次运行的陈旧退出码
        val sessionId = UUID.randomUUID().toString().replace("-", "").take(8)
        val exitCodeFile = File(context.cacheDir, "$EXIT_CODE_FILE_PREFIX$sessionId.txt")
        val startedFile = File(context.cacheDir, "$STARTED_FILE_PREFIX$sessionId.txt")
        val logFile = File(FCLPath.LOG_DIR ?: throw IOException("FCLPath not initialized"), INSTALLER_LOG_FILE)

        try {
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
                    delay(RETRY_DELAY_MS)
                    if (isCancelled()) throw CancellationException("Cancelled by user")
                }
                try {
                    return runOnceSuspending(
                        context, activityManager, exitCodeFile, startedFile, logFile,
                        sessionId, command, java, isCancelled, onLog, onTick
                    )
                } catch (e: ProcessTimeoutException) {
                    lastError = e
                }
            }
            throw requireNotNull(lastError)
        } finally {
            exitCodeFile.delete()
            startedFile.delete()
        }
    }

    /** 单次尝试：清理遗留文件、启动服务并以挂起轮询等待退出码 */
    private suspend fun runOnceSuspending(
        context: Context,
        activityManager: ActivityManager,
        exitCodeFile: File,
        startedFile: File,
        logFile: File,
        sessionId: String,
        command: Array<String>,
        java: Int,
        isCancelled: () -> Boolean,
        onLog: Consumer<String>,
        onTick: LongConsumer,
    ): Int {
        // 清理上一轮遗留的退出码与日志文件
        // 注意：不主动杀 :jvm 进程 —— getRunningAppProcesses 里刚退出进程的条目
        // 有延迟才会移除，此间其 pid 可能已被系统复用给新进程，按旧列表杀进程会误杀
        exitCodeFile.delete()
        startedFile.delete()
        logFile.delete()

        startProcessService(context, command, java, sessionId)

        var consumedChars = 0
        var serviceStarted = false
        var lastLogLength = 0L
        var lastTickSecond = -1L
        val attemptStart = System.currentTimeMillis()
        val startedDeadline = attemptStart + SERVICE_START_TIMEOUT_MS
        val hardDeadline = attemptStart + JVM_HARD_TIMEOUT_MS
        var lastProgress = attemptStart

        // :jvm 进程 CPU 进度检测：启动标记文件内容即 :jvm 的 pid，
        // 读 /proc/<pid>/stat 累计 CPU 时间判断进程是否仍在推进。
        // FART 等处理器全程静默（日志不增长），仅靠日志空闲会误杀慢设备上的健康运行
        var installerPid: Int? = null
        var cpuEverSeen = false
        var lastCpuMillis = -1L

        while (true) {
            val now = System.currentTimeMillis()

            // 取消优先于一切等待：杀掉 :jvm 进程后立即中止，不让任务取消被安装器吞掉
            if (isCancelled()) {
                killRemainingProcesses(activityManager, context.packageName)
                throw CancellationException("Cancelled by user")
            }

            // 增量读取安装日志
            val (lines, newConsumed) = readNewLogLines(logFile, consumedChars)
            if (lines.isNotEmpty()) {
                consumedChars = newConsumed
                onLog.accept(lines.joinToString("\n"))
            }

            // 日志文件长度变化即视为进程活跃（与退出码判断无关）
            val logLength = if (logFile.exists()) logFile.length() else 0L
            if (logLength != lastLogLength) {
                lastLogLength = logLength
                lastProgress = now
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
                if (text.isNotEmpty()) {
                    exitCodeFile.delete()
                    throw IOException(context.getString(R.string.installer_exit_code_invalid, text))
                }
            }

            // 确认 :jvm 服务确实启动（由 ProcessService.onStartCommand 写入启动标记，内容为 :jvm 的 pid）
            if (!serviceStarted) {
                if (startedFile.exists()) {
                    serviceStarted = true
                    installerPid = FileUtils.readText(startedFile).trim().toIntOrNull()
                    Logging.LOG.info("Installer process service started, pid: $installerPid")
                } else if (now > startedDeadline) {
                    killRemainingProcesses(activityManager, context.packageName)
                    throw ProcessTimeoutException(context.getString(R.string.installer_process_failed_to_start) + logTail(logFile, context))
                }
            } else if (installerPid != null) {
                // CPU 时间增长 = 进程仍在推进（静默处理器靠此判定存活）
                val cpuMillis = readProcessCpuMillis(installerPid)
                if (cpuMillis != null) {
                    cpuEverSeen = true
                    if (cpuMillis != lastCpuMillis) {
                        lastCpuMillis = cpuMillis
                        lastProgress = now
                    }
                }
            }

            // 只以退出码文件为准，不依赖 getRunningAppProcesses 判断进程存活
            // （部分系统/ROM 不返回 :jvm 进程，会导致误判安装失败）
            // 无进展判定：CPU 与日志均无变化超过 3 分钟即挂起；
            // procfs 不可用（从未读到 CPU）时退化为 10 分钟日志空闲
            // 超时时按旧列表杀进程可能误杀复用 pid，但此时进程已判挂起，误杀风险可接受
            val noProgressLimit = if (cpuEverSeen) NO_PROGRESS_TIMEOUT_MS else LOG_IDLE_TIMEOUT_MS
            if (now - lastProgress > noProgressLimit) {
                killRemainingProcesses(activityManager, context.packageName)
                throw ProcessTimeoutException(context.getString(R.string.installer_process_no_response) + logTail(logFile, context))
            }
            if (now > hardDeadline) {
                killRemainingProcesses(activityManager, context.packageName)
                throw ProcessTimeoutException(context.getString(R.string.installer_process_no_exit_code) + logTail(logFile, context))
            }

            // 耗时回调按秒去重，避免任务消息每 500ms 无谓刷新
            val elapsedSecond = (now - attemptStart) / 1000
            if (elapsedSecond != lastTickSecond) {
                lastTickSecond = elapsedSecond
                onTick.accept(now - attemptStart)
            }

            delay(POLL_INTERVAL_MS)
        }
    }

    /**
     * 读取 /proc/<pid>/stat 中进程的累计 CPU 时间（utime + stime，毫秒）。
     * :jvm 与主进程同 UID，procfs 可读；进程已死或读取受限时返回 null。
     */
    private fun readProcessCpuMillis(pid: Int): Long? {
        return try {
            val stat = File("/proc/$pid/stat").readText()
            // comm 字段可含空格与括号，取最后一个 ')' 之后的字段：state ppid … utime stime
            val fields = stat.substringAfterLast(')').trim().split(Regex("\\s+"))
            // utime = 第 14 字段、stime = 第 15 字段；从 state（第 3 字段）起索引分别为 11、12
            val utime = fields[11].toLongOrNull() ?: return null
            val stime = fields[12].toLongOrNull() ?: return null
            (utime + stime) * 10L // USER_HZ = 100，每 tick 10ms
        } catch (e: Exception) {
            null
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
    private suspend fun startProcessService(
        context: Context,
        command: Array<String>,
        java: Int,
        sessionId: String
    ) {
        var lastError: Throwable? = null
        for (i in 0 until START_RETRY_TIMES) {
            try {
                val intent = Intent(context, ProcessService::class.java)
                    .putExtra("command", command)
                    .putExtra("java", java)
                    .putExtra(EXTRA_SESSION, sessionId)
                context.startForegroundService(intent)
                return
            } catch (e: Throwable) {
                lastError = e
                if (i < START_RETRY_TIMES - 1) {
                    delay(500L)
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
