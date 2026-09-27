/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.GsonBuilder
import com.mio.JavaManager
import com.mio.data.Renderer
import com.mio.manager.RendererManager
import com.mio.minecraft.ModCheckException
import com.mio.minecraft.ModChecker
import com.mio.plugin.NativeLibPlugin
import com.mio.util.ParseUtil
import com.mio.util.getLocalizedText
import com.mio.util.hasStringId
import com.mio.util.loginStageText
import com.tungsten.fcl.FCLApp
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.JVMActivity
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.control.MenuType
import com.tungsten.fcl.setting.GameOption
import com.tungsten.fcl.setting.MenuSetting
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.util.TaskCancellationAction
import com.tungsten.fclauncher.bridge.FCLBridge
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AuthInfo
import com.tungsten.fclcore.auth.AuthenticationException
import com.tungsten.fclcore.auth.CharacterDeletedException
import com.tungsten.fclcore.auth.CredentialExpiredException
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorDownloadException
import com.tungsten.fclcore.auth.microsoft.MicrosoftAccount
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.MaintainTask
import com.tungsten.fclcore.download.game.GameAssetIndexDownloadTask
import com.tungsten.fclcore.download.game.GameVerificationFixTask
import com.tungsten.fclcore.download.game.LibraryDownloadException
import com.tungsten.fclcore.game.JavaVersion
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModpackCompletionException
import com.tungsten.fclcore.mod.ModpackConfiguration
import com.tungsten.fclcore.task.DownloadException
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.LibFilter
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.ResponseCodeException
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import com.tungsten.fclcore.util.versioning.VersionNumber
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLButton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.lwjgl.glfw.CallbackBridge
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.SocketTimeoutException
import java.nio.file.AccessDeniedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Level
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 启动流程编排：以协程串联版本解析、mod 扫描、依赖补全、登录与启动前检查，
 * 替代原先的 Task 任务链；弹窗标题实时显示当前阶段，fclcore 子任务
 * （依赖补全等）仍经 [runTracked] 接入 TaskExecutor 以复用进度展示。
 */
class LauncherHelper(
    private val context: Context,
    private val profile: Profile,
    private val account: Account,
    private val selectedVersion: String,
) {

    private val setting: VersionSetting = profile.getVersionSetting(selectedVersion)
    private val launchingStepsPane = TaskDialog(context, TaskCancellationAction.NORMAL).apply {
        title = context.getString(R.string.version_launch)
    }
    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var scaleFactor = 1.0
    private var launchJob: Job? = null

    /** 启动前确认框的按钮选择：positive 取消启动，negative 继续，neutral 由调用方处理 */
    private enum class Choice { CONTINUE, NEUTRAL, CANCEL }

    /** 登录异常弹窗的按钮选择 */
    private enum class LoginChoice { RETRY, SKIP_OFFLINE, CANCEL }

    fun launch() {
        LOG.info("Launching game version: $selectedVersion")
        launchingStepsPane.show()
        // 取消按钮点击时同时取消启动协程
        launchingStepsPane.setCancel(TaskCancellationAction(Runnable { launchJob?.cancel() }))
        launchJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                launch0()
                withContext(Dispatchers.Main) { launchingStepsPane.dismiss() }
            } catch (_: CancellationException) {
                // 用户主动取消：弹窗已随取消按钮关闭，无需提示
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    launchingStepsPane.dismiss()
                    showLaunchErrorMessage(e)
                }
            }
        }
    }

    private suspend fun launch0() {
        val repository = profile.repository
        val dependencyManager = profile.getDependency()

        setStage(R.string.launch_state_version)
        // 版本维护/解析较重，协程已在后台线程执行
        val versionRef = AtomicReference(
            MaintainTask.maintain(repository, repository.getResolvedVersion(selectedVersion))
        )

        setStage(R.string.launch_state_mods)
        // 全量 mod 扫描一次，供 lwjgl3ify 补丁、modloader 与渲染器检查共用
        val scannedMods = scanMods(repository, selectedVersion)
        // GTNH/lwjgl3ify 兼容：mods 目录存在 lwjgl3ify 时改写版本 JSON，以 RFB + Java17+ 启动
        Lwjgl3ifyPatcher.patchIfNeeded(repository, selectedVersion, scannedMods, versionRef)
        val gameVersion = repository.getGameVersion(versionRef.get())
        val integrityCheck = repository.unmarkVersionLaunchedAbnormally(selectedVersion)

        setStage(R.string.launch_state_java)
        val javaVersion = checkGameState(versionRef.get())
        val version = LibFilter.filter(versionRef.get(), false)

        if (!setting.isNotCheckGame) {
            setStage(R.string.launch_state_dependencies)
            // 游戏文件补全与整合包依赖补全（若存在）在同一执行器下并行，进度展示在弹窗任务列表
            val tasks = mutableListOf<Task<*>>(
                dependencyManager.checkGameCompletionAsync(version, integrityCheck)
            )
            try {
                val configuration: ModpackConfiguration<*> =
                    ModpackHelper.readModpackConfiguration(
                        repository.getModpackConfiguration(
                            selectedVersion
                        )
                    )
                ModpackHelper.getProviderByType(configuration.type)
                    ?.let { tasks.add(it.createCompletionTask(dependencyManager, selectedVersion)) }
            } catch (_: IOException) {
            }
            runTracked(Task.allOf(*tasks.toTypedArray()))
        }

        // 解包内置补丁 jar
        unpackJar("/assets/game/MioLibPatcher.jar", FCLPath.LIB_PATCHER_PATH, "MioLibFixer.jar")
        unpackJar(
            "/assets/game/MioLaunchWrapper.jar",
            FCLPath.MIO_LAUNCH_WRAPPER,
            "MioLaunchWrapper.jar"
        )

        if (gameVersion.isPresent) {
            runTracked(GameVerificationFixTask(dependencyManager, gameVersion.get(), version))
        }

        setStage(R.string.launch_state_logging_in)
        val authInfo = logIn()

        setStage(R.string.launch_state_waiting_launching)
        try {
            val menuSetting = GsonBuilder().setPrettyPrinting().create()
                .fromJson(
                    FileUtils.readText(File(FCLPath.FILES_DIR + "/menu_setting.json")),
                    MenuSetting::class.java
                )
            scaleFactor = menuSetting?.windowScale ?: 1.0
        } catch (_: Throwable) {
            scaleFactor = 1.0
        }
        val launchOptions =
            repository.getLaunchOptions(selectedVersion, javaVersion, profile.gameDir, scaleFactor)
        val launcher = FCLGameLauncher(context, repository, version, authInfo, launchOptions)

        var jnaVersion: String? = null
        var lwjglVersion: String? = null
        var lwjglMax: VersionNumber? = null
        for (library in version.libraries) {
            val name = library.name
            if (name.startsWith("net.java.dev.jna:jna:")) {
                jnaVersion = library.version
            } else if (name.startsWith("org.lwjgl.lwjgl:lwjgl:") || name.startsWith("org.lwjgl:lwjgl:")) {
                // 新旧 LWJGL 声明可能并存(如 lwj3ify 的 3.x 与基础版本的 2.x),取最高版本
                val current = VersionNumber.asVersion(library.version)
                if (lwjglMax == null || current > lwjglMax) {
                    lwjglMax = current
                    lwjglVersion = library.version
                }
            }
        }
        jnaVersion?.let(launcher::setJnaVersion)
        lwjglVersion?.let(launcher::setLwjglVersion)

        val fclBridge = launcher.launch()

        checkPathValid(repository)
        val renderer = RendererManager.getRenderer(setting.renderer)
        fclBridge.renderer = renderer.name
        checkRenderer(
            renderer,
            repository.getGameVersion(selectedVersion).orElse(""),
            scannedMods
        )
        checkNativeLibPlugin(repository.getGameVersion(selectedVersion).orElse(""))
        if (!setting.isNotCheckMod) {
            checkModLoader(repository, scannedMods)
        }
        checkMod(fclBridge, repository.getGameVersion(selectedVersion).orElse(""), scannedMods)

        val gameOption = GameOption(repository.getRunDirectory(selectedVersion).absolutePath)
        gameOption.set("preferredGraphicsBackend", setting.graphicsBackend)
        gameOption.set("startedCleanly", "true")
        gameOption.save()

        withContext(Dispatchers.Main) {
            CallbackBridge.nativeSetUseInputStackQueue(version.arguments.isPresent)
            val intent = Intent(context, JVMActivity::class.java)
            fclBridge.scaleFactor = scaleFactor
            fclBridge.controller = setting.controller
            fclBridge.gameDir = repository.getRunDirectory(selectedVersion).absolutePath
            fclBridge.java = javaVersion.getVersion().toString()
            JVMActivity.setFCLBridge(fclBridge, MenuType.GAME)
            val bundle = Bundle()
            bundle.putString("controller", setting.controller)
            bundle.putString("TERRACOTTA_PLAYER", account.username)
            intent.putExtras(bundle)
            LOG.log(Level.INFO, "Start JVMActivity!")
            context.startActivity(intent)
            if (MainActivity.getInstance().shouldPlayVideo()) {
                MainActivity.getInstance().mediaPlayer = null
                MainActivity.getInstance().binding.videoView.stopPlayback()
            }
            if (context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
                    .getBoolean("autoExitLauncher", false)
            ) {
                FCLApp.getActivity()?.finish()
            }
        }
    }

    /** 弹窗标题切换为当前启动阶段 */
    private suspend fun setStage(stageRes: Int) {
        withContext(Dispatchers.Main) { launchingStepsPane.title = context.getString(stageRes) }
    }

    /**
     * 在独立 TaskExecutor 中执行 fclcore 任务（依赖补全、资源修复等），协程挂起至完成；
     * 执行器绑定到启动弹窗以复用下载进度展示，协程取消会级联取消任务
     */
    private suspend fun runTracked(task: Task<*>) {
        val executor = task.executor()
        withContext(Dispatchers.Main) { launchingStepsPane.setExecutor(executor, false) }
        suspendCancellableCoroutine { cont ->
            executor.addTaskListener(object : TaskListener() {
                override fun onStop(success: Boolean, executor: TaskExecutor) {
                    if (success) {
                        cont.resume(Unit)
                    } else {
                        // 用户取消时 exception 为 null，按协程取消处理
                        executor.exception?.let { cont.resumeWithException(it) } ?: cont.cancel()
                    }
                }
            })
            cont.invokeOnCancellation { executor.cancel() }
            executor.start()
        }
    }

    /** 弹出不可关闭的确认框并挂起等待选择，取消启动返回 [Choice.CANCEL] */
    private suspend fun awaitChoice(
        message: String,
        positiveText: String = context.getString(R.string.button_cancel),
        negativeText: String = context.getString(R.string.mod_check_continue),
        neutralText: String? = null,
    ): Choice = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { cont ->
            val builder = FCLAlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setMessage(message)
            builder.setPositiveButton(positiveText) { cont.resume(Choice.CANCEL) }
            builder.setNegativeButton(negativeText) { cont.resume(Choice.CONTINUE) }
            if (neutralText != null) builder.setNeutralButton(neutralText) { cont.resume(Choice.NEUTRAL) }
            builder.create().show()
        }
    }

    private suspend fun checkPathValid(repository: FCLGameRepository) {
        try {
            val path = repository.getVersionJar(selectedVersion).absolutePath
            if (!ParseUtil.isValidCharacters(path)) {
                if (awaitChoice(
                        context.getString(
                            R.string.message_check_path_valid,
                            path
                        )
                    ) == Choice.CANCEL
                )
                    throw CancellationException()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // 检查失败时按路径有效处理，不阻断启动
        }
    }

    /** 全量扫描 mod 列表，失败返回 null（调用方按需回退） */
    private fun scanMods(repository: FCLGameRepository, versionId: String): List<LocalModFile>? =
        try {
            repository.getModManager(versionId).mods
        } catch (e: Exception) {
            LOG.log(Level.WARNING, "Failed to scan mods before launch", e)
            null
        }

    private suspend fun checkRenderer(
        renderer: Renderer,
        version: String,
        mods: List<LocalModFile>?
    ) {
        if (Lwjgl3ifyPatcher.shouldSkipRendererCheck(mods)) {
            LOG.log(Level.INFO, "Angelica + lwjgl3ify detected, skip renderer version warning")
            return
        }
        try {
            if (version.isNotEmpty()) {
                if (renderer.minMCver.isNotEmpty() && GameVersionNumber.compare(
                        version,
                        renderer.minMCver
                    ) < 0
                ) {
                    if (awaitChoice(
                            context.getString(
                                R.string.message_check_renderer,
                                renderer.name
                            )
                        ) == Choice.CANCEL
                    )
                        throw CancellationException()
                }
                if (renderer.maxMCver.isNotEmpty() && GameVersionNumber.compare(
                        version,
                        renderer.maxMCver
                    ) > 0
                ) {
                    if (awaitChoice(
                            context.getString(
                                R.string.message_check_renderer,
                                renderer.name
                            )
                        ) == Choice.CANCEL
                    )
                        throw CancellationException()
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            LOG.log(Level.WARNING, "checkRenderer() failed", e)
        }
    }

    private suspend fun checkNativeLibPlugin(version: String) {
        try {
            val unsupportedPlugins = NativeLibPlugin.pluginList
                .filter { plugin ->
                    (plugin.minMCVer.isNotEmpty() && GameVersionNumber.compare(
                        version,
                        plugin.minMCVer
                    ) < 0)
                            || (plugin.maxMCVer.isNotEmpty() && GameVersionNumber.compare(
                        version,
                        plugin.maxMCVer
                    ) > 0)
                }.map { it.appName }
            if (unsupportedPlugins.isNotEmpty()) {
                val fullString = unsupportedPlugins.joinToString(", ")
                if (awaitChoice(
                        context.getString(
                            R.string.message_check_plugin,
                            fullString
                        )
                    ) == Choice.CANCEL
                )
                    throw CancellationException()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            LOG.log(Level.WARNING, "checkNativeLibPlugin() failed", e)
        }
    }

    private suspend fun checkModLoader(
        repository: FCLGameRepository,
        mods: List<LocalModFile>?
    ) {
        try {
            val modded =
                LibraryAnalyzer.isModded(repository, repository.getVersion(selectedVersion))
            if (!mods.isNullOrEmpty() && !modded) {
                when (awaitChoice(
                    context.getString(R.string.message_check_has_modloader),
                    neutralText = context.getString(R.string.button_install)
                )) {
                    Choice.CONTINUE -> {}
                    Choice.NEUTRAL -> {
                        // 跳转版本管理页的“游戏”标签页（索引 2）引导安装 modloader，随后取消本次启动
                        MainActivity.getInstance().binding.manage.isSelected = true
                        val tabLayout = UIManager.instance.manageUI.tabLayout
                        tabLayout.selectTab(tabLayout.getTabAt(2))
                        throw CancellationException()
                    }

                    Choice.CANCEL -> throw CancellationException()
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            LOG.log(Level.WARNING, "checkModLoader() failed", e)
        }
    }

    private suspend fun checkMod(bridge: FCLBridge, version: String, mods: List<LocalModFile>?) {
        try {
            var modList = mods
            if (modList == null) {
                // 启动链扫描失败时回退为自行获取（getModManager 每次返回新实例，此处为全量重扫）
                modList = Profiles.getSelectedProfile().repository
                    .getModManager(Profiles.getSelectedVersion()).mods
            }
            val modCheckerInfo = StringBuilder()
            val modSummary = StringBuilder()
            val modChecker = ModChecker(context, version)
            var count = 0
            for (mod in modList) {
                if (!mod.isActive) {
                    continue
                }
                modSummary.append(mod.fileName).append(" | ")
                    .append(mod.id).append(" | ")
                    .append(mod.version).append(" | ")
                    .append(mod.modLoaderType).append('\n')
                try {
                    modChecker.check(bridge, mod)
                } catch (e: ModCheckException) {
                    count++
                    modCheckerInfo.append(count).append('.').append(e.reason).append("\n\n")
                }
            }
            bridge.modSummary = modSummary.toString()
            if (!setting.isNotCheckMod && modCheckerInfo.toString().trim().isNotEmpty()) {
                if (awaitChoice(modCheckerInfo.toString()) == Choice.CANCEL)
                    throw CancellationException()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            LOG.log(Level.WARNING, "CheckMod() failed", e)
        }
    }

    private suspend fun checkGameState(version: Version): JavaVersion {
        val analyzer = LibraryAnalyzer.analyze(version, null)
        val isCleanroom = analyzer.has(LibraryAnalyzer.LibraryType.CLEANROOM)
        val cleanroomVersion: VersionNumber? = if (isCleanroom) {
            analyzer.getVersion(LibraryAnalyzer.LibraryType.CLEANROOM)
                .map { s -> VersionNumber.asVersion(StringUtils.removeSuffix(s, "-alpha")) }
                .orElse(null)
        } else null

        fun suggestedJavaVersion(): JavaVersion =
            if (isCleanroom) {
                if ((cleanroomVersion != null) && (cleanroomVersion >= "0.5.0"))
                    JavaManager.getSuitableJavaVersion(25)
                else
                    JavaManager.getJavaFromVersionName("jre21")
            } else {
                JavaManager.getSuitableJavaVersion(version)
            }

        // JavaManager 维护的 Java 列表需在主线程读取
        val javaVersion = withContext(Dispatchers.Main) {
            if (setting.java == "Auto") {
                suggestedJavaVersion()
            } else {
                JavaManager.getJavaFromVersionName(setting.java)
            }
        }
        if (setting.isNotCheckJVM) return javaVersion

        return withContext(Dispatchers.Main) {
            val suggested = suggestedJavaVersion()
            if (suggested.getVersion() != -1 &&
                (setting.java == "Auto" || javaVersion.getVersion() == suggested.getVersion())
            ) {
                return@withContext if (setting.java == "Auto") suggested else javaVersion
            }

            // 当前 Java 不匹配：让用户选择自动切换 / 继续使用 / 不再检查
            when (awaitChoice(
                context.getString(R.string.launch_error_java),
                positiveText = context.getString(R.string.launch_error_java_auto),
                negativeText = context.getString(R.string.launch_error_java_continue),
                neutralText = context.getString(R.string.launch_error_java_continue_disable)
            )) {
                Choice.CANCEL -> {
                    // “自动选择”：切回 Auto 并采用建议版本
                    setting.java = JavaVersion.JAVA_AUTO.name
                    if (suggested == JavaManager.NO_JAVA_FOUND)
                        throw IllegalArgumentException("Failed to find a suitable Java!")
                    suggested
                }

                Choice.NEUTRAL -> {
                    setting.isNotCheckJVM = true
                    javaVersion
                }

                Choice.CONTINUE -> javaVersion
            }
        }
    }

    /**
     * 登录账户；微软账户登录期间将认证阶段文字实时写入启动弹窗日志区
     */
    private suspend fun logIn(): AuthInfo {
        val withProgress = account is MicrosoftAccount
        if (withProgress) {
            // 进度回调在后台登录线程触发，需切回主线程写弹窗日志
            account.setProgressCallback { stage ->
                mainScope.launch { launchingStepsPane.appendLog(loginStageText(context, stage)) }
            }
        }
        try {
            while (true) {
                try {
                    return account.logIn()
                } catch (e: CredentialExpiredException) {
                    LOG.log(Level.INFO, "Credential has expired", e)
                    // 凭证过期：跳过则离线进入，确认则取消启动
                    if (awaitLoginChoice { callback ->
                            TipReLoginLoginDialog(
                                context,
                                callback
                            )
                        } == LoginChoice.CANCEL)
                        throw CancellationException()
                    return account.playOffline()
                } catch (e: AuthenticationException) {
                    LOG.log(Level.WARNING, "Authentication failed, try skipping refresh", e)
                    when (awaitLoginChoice { callback -> SkipLoginDialog(context, callback) }) {
                        LoginChoice.RETRY -> {} // 继续循环重试登录
                        LoginChoice.SKIP_OFFLINE -> return account.playOffline()
                        LoginChoice.CANCEL -> throw CancellationException()
                    }
                }
            }
        } finally {
            if (withProgress) {
                account.setProgressCallback(null)
            }
        }
    }

    /** 弹出登录异常处理弹窗并挂起等待选择 */
    private suspend fun awaitLoginChoice(create: ((LoginChoice) -> Unit) -> FCLDialog): LoginChoice =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { cont ->
                create { choice -> cont.resume(choice) }.show()
            }
        }

    /** 解包内置补丁 jar 到目标路径，失败仅记录警告不阻断启动 */
    private fun unpackJar(resourcePath: String, targetPath: String, logName: String) {
        try {
            LauncherHelper::class.java.getResourceAsStream(resourcePath).use { input ->
                Files.copy(input, File(targetPath).toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            LOG.log(Level.WARNING, "Unable to unpack $logName", e)
        }
    }

    /** 主线程展示启动失败原因（按异常类型整理文案） */
    private fun showLaunchErrorMessage(ex: Exception) {
        val message: String = when (ex) {
            is ModpackCompletionException ->
                if (ex.cause is FileNotFoundException)
                    context.getString(R.string.modpack_type_curse_not_found)
                else
                    context.getString(R.string.modpack_type_curse_error)

            is LibraryDownloadException -> {
                val info = StringBuilder(
                    context.getString(R.string.launch_failed_download_library, ex.library.name)
                ).append('\n')
                when (val cause = ex.cause) {
                    is ResponseCodeException ->
                        if (cause.responseCode == 404)
                            info.append(context.getString(R.string.download_code_404, cause.url))
                        else
                            info.append(
                                context.getString(
                                    R.string.download_failed,
                                    cause.url,
                                    cause.responseCode
                                )
                            )

                    else -> info.append(StringUtils.getStackTrace(ex.cause))
                }
                info.toString()
            }

            is DownloadException -> {
                val url = ex.url
                when (val cause = ex.cause) {
                    is SocketTimeoutException -> context.getString(
                        R.string.install_failed_downloading_timeout,
                        url
                    )

                    is ResponseCodeException -> {
                        val key = "download_code_${cause.responseCode}"
                        if (hasStringId(context, key))
                            getLocalizedText(context, key, url)
                        else
                            context.getString(
                                R.string.install_failed_downloading_detail,
                                url
                            ) + "\n" +
                                    StringUtils.getStackTrace(ex.cause)
                    }

                    else -> context.getString(
                        R.string.install_failed_downloading_detail,
                        url
                    ) + "\n" +
                            StringUtils.getStackTrace(ex.cause)
                }
            }

            is GameAssetIndexDownloadTask.GameAssetIndexMalformedException -> context.getString(R.string.assets_index_malformed)
            is AuthlibInjectorDownloadException -> context.getString(R.string.account_failed_injector_download_failure)
            is CharacterDeletedException -> context.getString(R.string.account_failed_character_deleted)
            is ResponseCodeException ->
                if (ex.responseCode == 404)
                    context.getString(R.string.download_code_404, ex.url)
                else
                    context.getString(R.string.download_failed, ex.url, ex.responseCode)

            is AccessDeniedException -> context.getString(R.string.exception_access_denied, ex.file)
            is ModCheckException -> ex.reason
            is IllegalArgumentException -> context.getString(R.string.exception_no_suitable_java)
            else -> StringUtils.getStackTrace(ex)
        }

        val builder = FCLAlertDialog.Builder(context)
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        builder.setCancelable(false)
        builder.setTitle(context.getString(R.string.launch_failed))
        builder.setMessage(message)
        builder.setNegativeButton(context.getString(R.string.dialog_positive), null)
        builder.create().show()
    }

    /** 认证失败弹窗：重试登录 / 跳过离线进入 / 取消启动 */
    private class SkipLoginDialog(
        context: Context,
        private val onChoice: (LoginChoice) -> Unit,
    ) : FCLDialog(context), View.OnClickListener {

        private val retry: FCLButton
        private val skip: FCLButton
        private val cancel: FCLButton

        init {
            setContentView(R.layout.dialog_skip_login)
            setCancelable(false)
            retry = findViewById(R.id.retry)!!
            skip = findViewById(R.id.skip)!!
            cancel = findViewById(R.id.cancel)!!
            retry.setOnClickListener(this)
            skip.setOnClickListener(this)
            cancel.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view) {
                retry -> onChoice(LoginChoice.RETRY)
                skip -> onChoice(LoginChoice.SKIP_OFFLINE)
                cancel -> onChoice(LoginChoice.CANCEL)
            }
            dismiss()
        }
    }

    /** 凭证过期弹窗：跳过离线进入 / 取消启动 */
    private class TipReLoginLoginDialog(
        context: Context,
        private val onChoice: (LoginChoice) -> Unit,
    ) : FCLDialog(context), View.OnClickListener {

        private val skip: FCLButton
        private val ok: FCLButton

        init {
            setContentView(R.layout.dialog_tip_relogin)
            setCancelable(false)
            skip = findViewById(R.id.skip)!!
            ok = findViewById(R.id.ok)!!
            skip.setOnClickListener(this)
            ok.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view) {
                skip -> onChoice(LoginChoice.SKIP_OFFLINE)
                ok -> onChoice(LoginChoice.CANCEL)
            }
            dismiss()
        }
    }
}
