package com.tungsten.fcl.vr

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.mio.data.Renderer
import com.mio.plugin.DriverPlugin
import com.tungsten.fcl.activity.JVMActivity
import com.tungsten.fcl.activity.QuestVrLoadingActivity
import com.tungsten.fcl.control.MenuType
import com.tungsten.fclauncher.FCLConfig
import com.tungsten.fclauncher.FCLauncher
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.utils.FCLPath
import com.mio.manager.RendererManager
import org.lwjgl.glfw.CallbackBridge
import java.io.File
import java.io.IOException
import java.io.Reader
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.UUID
import java.util.function.Consumer

/**
 * VR 游戏跨进程启动通道：主进程把 FCLBridge 的启动参数序列化到 token 文件，
 * `:game` 进程的启动面板凭 token 还原并直接启动 JVM（Intent extras 无法承载大二进制参数）。
 */
object QuestGameBootstrap {
    private const val EXTRA_TOKEN = "com.tungsten.fcl.quest.LAUNCH_TOKEN"
    private const val DIRECTORY = "quest-game-launch"
    private val GSON = Gson()

    @JvmStatic
    var launcherTaskId = -1
        private set

    @Volatile
    private var gameProcess = false

    /** 当前进程是否为承载 VR 游戏的 `:game` 进程 */
    @JvmStatic
    fun isGameProcess(): Boolean {
        if (gameProcess) return true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName().endsWith(":game")
        }
        return false
    }

    private fun runningGamePid(context: Context): Int {
        val manager = context.getSystemService(ActivityManager::class.java)
        val processes = manager?.runningAppProcesses ?: return -1
        for (process in processes) {
            if ((context.packageName + ":game") == process.processName) return process.pid
        }
        return -1
    }

    private fun isReturning(context: Context, pid: Int): Boolean {
        return try {
            File(context.cacheDir, "quest-game-exiting").readText(StandardCharsets.UTF_8)
                .equals(pid.toString())
        } catch (ignored: IOException) {
            false
        }
    }

    /** 游戏进程写入退出标记，让主进程的重复启动请求改道为「回到运行中的游戏」 */
    internal fun markReturning(context: Context) {
        try {
            File(context.cacheDir, "quest-game-exiting")
                .writeText(Process.myPid().toString(), StandardCharsets.UTF_8)
        } catch (ignored: IOException) {
        }
    }

    /** 若游戏仍在运行且未在退出中，跳转到启动面板恢复它；返回是否已改道 */
    @JvmStatic
    fun resumeRunningGame(context: Context): Boolean {
        val pid = runningGamePid(context)
        if (pid == -1 || isReturning(context, pid)) return false
        context.startActivity(Intent(context, QuestVrLoadingActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
        return true
    }

    /** 主进程启动入口：等旧进程退净后再发起新启动（或改道恢复运行中的游戏） */
    @JvmStatic
    fun startLaunch(context: Context, intent: Intent, failure: Consumer<Exception>) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            private var remaining = 160

            override fun run() {
                val pid = runningGamePid(context)
                if (pid != -1 && isReturning(context, pid) && remaining-- > 0) {
                    handler.postDelayed(this, 100)
                    return
                }
                try {
                    if (pid != -1 && isReturning(context, pid)) {
                        throw IOException("The previous game process is still stopping")
                    }
                    if (resumeRunningGame(context)) discardLaunch(context, intent)
                    else context.startActivity(intent)
                } catch (error: Exception) {
                    discardLaunch(context, intent)
                    failure.accept(error)
                }
            }
        })
    }

    /** 把 FCLBridge 当前启动参数序列化到 token 文件并挂到 Intent 上 */
    @JvmStatic
    @Throws(IOException::class)
    fun attachLaunch(context: Context, intent: Intent, bridge: FCLBridge, useInputStack: Boolean) {
        val config = bridge.launchConfig
            ?: throw IOException("Missing VR launch configuration")
        require(config.isVrEnabled) { "Missing VR launch configuration" }
        val launch = Launch().apply {
            logDir = config.logDir
            javaPath = config.javaPath
            workingDir = config.workingDir
            renderer = config.renderer
            args = config.args
            useVKDriverSystem = config.useVKDriverSystem
            installedModLoaders = config.installedModLoaders
            lwjglVersion = config.lwjglVersion
            driver = DriverPlugin.selected
            scaleFactor = bridge.scaleFactor
            controller = bridge.controller
            javaVersion = bridge.java
            modSummary = bridge.modSummary
            this.useInputStack = useInputStack
            forceResolution = FCLBridge.FORCE_RESOLUTION
            forceScale = FCLBridge.FORCE_RESOLUTION_SCALE
            forceWidth = FCLBridge.FORCE_RESOLUTION_WIDTH
            forceHeight = FCLBridge.FORCE_RESOLUTION_HEIGHT
            forceStartSize = FCLBridge.FORCE_RESOLUTION_START_SIZE
            launcherTaskIdValue = if (context is Activity) context.taskId else -1
        }
        val token = UUID.randomUUID().toString()
        val file = launchFile(context, token)
        try {
            Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8).use { writer: Writer ->
                GSON.toJson(launch, writer)
            }
        } catch (failure: IOException) {
            Files.deleteIfExists(file.toPath())
            throw failure
        } catch (failure: RuntimeException) {
            Files.deleteIfExists(file.toPath())
            throw failure
        }
        intent.putExtra(EXTRA_TOKEN, token)
    }

    @JvmStatic
    fun discardLaunch(context: Context, intent: Intent) {
        val token = intent.getStringExtra(EXTRA_TOKEN) ?: return
        try {
            launchFile(context, token).delete()
        } catch (ignored: IOException) {
        }
    }

    /** `:game` 进程还原启动参数并启动 JVM（此进程内 FCLBridge 尚不存在） */
    @JvmStatic
    @Throws(IOException::class)
    fun restore(activity: Activity, intent: Intent): FCLBridge {
        check(isGameProcess()) { "VR must run in its isolated game process" }
        JVMActivity.getPreparedBridge()?.let { existing ->
            if (gameProcess) {
                discardLaunch(activity, intent)
                intent.removeExtra(EXTRA_TOKEN)
                return existing
            }
        }
        val token = intent.getStringExtra(EXTRA_TOKEN)
            ?: throw IOException("The game session is no longer available")
        val file = launchFile(activity, token)
        val launch: Launch?
        try {
            Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8).use { reader: Reader ->
                launch = GSON.fromJson(reader, Launch::class.java)
            }
        } catch (failure: JsonParseException) {
            throw IOException("Invalid VR launch configuration", failure)
        } finally {
            Files.deleteIfExists(file.toPath())
            intent.removeExtra(EXTRA_TOKEN)
        }
        val data = launch ?: throw IOException("Incomplete VR launch configuration")
        val renderer = data.renderer ?: throw IOException("Incomplete VR launch configuration")
        val args = data.args ?: throw IOException("Incomplete VR launch configuration")
        val javaPath = data.javaPath ?: throw IOException("Incomplete VR launch configuration")
        val workingDir = data.workingDir ?: throw IOException("Incomplete VR launch configuration")
        val logDir = data.logDir ?: throw IOException("Incomplete VR launch configuration")
        FCLPath.loadPaths(activity)
        RendererManager.init(activity)
        data.driver?.let { DriverPlugin.selected = it }
        FCLBridge.FORCE_RESOLUTION = data.forceResolution
        FCLBridge.FORCE_RESOLUTION_SCALE = data.forceScale
        FCLBridge.FORCE_RESOLUTION_WIDTH = data.forceWidth
        FCLBridge.FORCE_RESOLUTION_HEIGHT = data.forceHeight
        FCLBridge.FORCE_RESOLUTION_START_SIZE = data.forceStartSize
        CallbackBridge.nativeSetUseInputStackQueue(data.useInputStack)
        val config = FCLConfig(activity.applicationContext, logDir,
            javaPath, workingDir, renderer, args)
        config.isVrEnabled = true
        config.useVKDriverSystem = data.useVKDriverSystem
        config.installedModLoaders = data.installedModLoaders
        config.lwjglVersion = data.lwjglVersion ?: QuestVrRuntime.LWJGL_VERSION
        val bridge = FCLauncher.launchMinecraft(config)
        bridge.setScaleFactor(data.scaleFactor)
        bridge.setController(data.controller)
        bridge.setGameDir(workingDir)
        bridge.setJava(data.javaVersion)
        bridge.setRenderer(renderer.name)
        bridge.setModSummary(data.modSummary)
        launcherTaskId = data.launcherTaskIdValue
        gameProcess = true
        File(activity.cacheDir, "quest-game-exiting").delete()
        JVMActivity.setFCLBridge(bridge, MenuType.GAME)
        return bridge
    }

    @Throws(IOException::class)
    private fun launchFile(context: Context, token: String): File {
        require(TOKEN_PATTERN.matches(token)) { "Invalid VR launch token" }
        val directory = File(context.cacheDir, DIRECTORY)
        if (!directory.isDirectory && !directory.mkdirs()) {
            throw IOException("Cannot create private launch directory")
        }
        return File(directory, "$token.json")
    }

    private val TOKEN_PATTERN = Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")

    /** Gson 反序列化目标：字段可空（Gson 绕过 Kotlin 构造器与默认值），restore 中统一校验 */
    private class Launch {
        var logDir: String? = null
        var javaPath: String? = null
        var workingDir: String? = null
        var lwjglVersion: String? = null
        var renderer: Renderer? = null
        var args: Array<String>? = null
        var useVKDriverSystem = false
        var useInputStack = false
        var forceResolution = false
        var installedModLoaders: FCLConfig.InstalledModLoaders? = null
        var driver: DriverPlugin.Driver? = null
        var scaleFactor = 0.0
        var controller: String? = null
        var javaVersion: String? = null
        var modSummary: String? = null
        var forceScale = 0f
        var forceWidth = 0
        var forceHeight = 0
        var forceStartSize = 0
        var launcherTaskIdValue = 0
    }
}
