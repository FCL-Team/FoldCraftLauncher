package com.tungsten.fcl.ui.setting.compose

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.task.FetchTask
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fcllibrary.component.theme.Theme
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

/**
 * 启动器设置页 ViewModel（小步骤 3.1）：LauncherSettingPage.java 的 Compose 化承接。
 *
 * 承接原则（bridge-api.md §5）：
 * - 数据层：Config/Theme 字段已 StateFlow 化（阶段 4a）——写 setter 即持久化
 *   （ConfigHolder 自动落盘 / Theme.saveTheme），Flow 单向投影进 UiState；
 *   Theme/ThemeEngine 沿用遗留单例；
 *   SharedPreferences "launcher" 读写键名/默认值与遗留完全一致；
 * - 业务规则全部收在本类：勾选自动线程重置线程数、动画速度变更自动 saveTheme、
 *   打开页面静默清理超 3 天缓存、Palette 背景取色、导出日志等；
 * - 需要 Activity/MainActivity 能力的副作用（文件选择、权限、Window flags、
 *   MainActivity.binding.background / setupLiveBackground / setLiveBackgroundVolume、
 *   弹窗）一律发一次性事件，由 [LauncherSettingHost] 在宿主侧处理；
 * - 遗留两个"死按钮"（resetTheme2Dark / fetchBackgroundColor2Dark 在
 *   LauncherSettingPage.java:85-105 漏注册 OnClickListener，onClick 里却有处理代码），
 *   迁移后按其 onClick 既有逻辑接通（深色内容色重置为 #000000 / 背景取色），见迁移报告。
 */
class LauncherSettingViewModel(
    private val application: Application,
) : FCLViewModel<LauncherSettingUiState, LauncherSettingEvent>(LauncherSettingUiState()) {

    private val prefs = application.getSharedPreferences("launcher", Context.MODE_PRIVATE)
    private val engine = ThemeEngine.getInstance()
    private val theme = engine.theme

    // ---------- StateFlow 直连（阶段 4a：写 setter 即持久化，Flow 回流刷新 UI） ----------

    init {
        // SharedPreferences / Theme 初值投影
        updateState {
            copy(
                languageIndex = LocaleUtils.getLanguage(application),
                autoExitLauncher = prefs.getBoolean("autoExitLauncher", false),
                themeModeIndex = prefs.getInt("themeMode", 0),
                videoBackgroundVolume = prefs.getInt("videoBackgroundVolume", 100),
                closeSkinModel = theme.isCloseSkinModel,
                vibrationDuration = prefs.getInt("vibrationDuration", 100),
                disableFullscreenInput = prefs.getBoolean("disableFullscreenInput", true),
                customLauncherName = prefs.getString(
                    "custom_launcher_name",
                    application.getString(R.string.app_name),
                ) ?: application.getString(R.string.app_name),
                allowScreenshots = prefs.getBoolean("allowScreenshots", false),
            )
        }

        // StateFlow 单向投影（主题色板/开关/滑杆值/下载设置）
        theme.colorFlow().observeIntoState { copy(themeColor = it) }
        theme.color2Flow().observeIntoState { copy(themeColor2 = it) }
        theme.color2DarkFlow().observeIntoState { copy(themeColor2Dark = it) }
        theme.animationSpeedFlow().observeIntoState { copy(animationSpeed = it) }
        theme.fullscreenFlow().observeIntoState { copy(ignoreNotch = it) }
        ConfigHolder.config().autoChooseDownloadTypeFlow()
            .observeIntoState { copy(autoChooseDownloadType = it) }
        ConfigHolder.config().versionListSourceFlow()
            .observeIntoState { copy(versionListSourceIndex = indexOfSource(AUTO_SOURCE_KEYS, it)) }
        ConfigHolder.config().downloadTypeFlow()
            .observeIntoState { copy(downloadTypeIndex = indexOfSource(RAW_SOURCE_KEYS, it)) }
        ConfigHolder.config().autoDownloadThreadsFlow()
            .observeIntoState { copy(autoDownloadThreads = it) }
        ConfigHolder.config().downloadThreadsFlow()
            .observeIntoState { copy(downloadThreads = it) }

        // 动画速度变更自动保存（对齐 LauncherSettingPage.java:157 的属性监听；
        // subscribe 跳过当前值，对齐 addListener 语义）
        FlowSubscriptions.subscribe(theme.animationSpeedFlow()) { Theme.saveTheme(application, theme) }

        // 打开页面静默清理超 3 天缓存（对齐 LauncherSettingPage.java:211-214，无 UI 反馈）
        cleanExpiredCacheIfNeeded()
    }

    private fun cleanExpiredCacheIfNeeded() {
        val last = prefs.getLong("clear_cache", 0L)
        if (System.currentTimeMillis() - last >= 3 * ONE_DAY) {
            FileUtils.cleanDirectoryQuietly(File(FCLPath.CACHE_DIR).parentFile)
            prefs.edit().putLong("clear_cache", System.currentTimeMillis()).apply()
        }
    }

    // ---------- 通用 ----------

    /** 语言切换（9 项）：写 LocaleUtils 配置后发重启提示事件；setLanguage 需 Activity
     *  上下文即时切换资源配置（遗留 :524-525 用页面 Activity context），由宿主执行。 */
    fun setLanguage(index: Int) {
        if (index == currentState.languageIndex) return
        updateState { copy(languageIndex = index) }
        LocaleUtils.changeLanguage(application, index)
        sendEvent(LauncherSettingEvent.ShowRestartHint)
    }

    /** 检查更新：isChecking 防重入在宿主侧（对齐 :244-245）。 */
    fun onCheckUpdate() = sendEvent(LauncherSettingEvent.CheckUpdate)

    /** 清除缓存：直接删目录，无任何反馈（对齐 :256-258）。 */
    fun onClearCache() {
        FileUtils.cleanDirectoryQuietly(File(FCLPath.CACHE_DIR).parentFile)
    }

    /** 导出日志：后台写文件，成功/失败弹窗（对齐 :259-286）。 */
    fun onExportLog() {
        viewModelScope.launch(Dispatchers.IO) {
            val logFile = File(
                File(FCLPath.SHARED_COMMON_DIR).parent,
                "fcl-exported-logs-" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss")) + ".log",
            ).toPath().toAbsolutePath()
            Logging.LOG.info("Exporting logs to $logFile")
            try {
                Files.write(logFile, Logging.getRawLogs())
            } catch (e: IOException) {
                Logging.LOG.log(Level.WARNING, "Failed to export logs", e)
                sendEvent(
                    LauncherSettingEvent.ShowAlert(
                        application.getString(R.string.settings_launcher_launcher_log_export_failed) + "\n" + e,
                        isError = true,
                    ),
                )
                return@launch
            }
            sendEvent(
                LauncherSettingEvent.ShowAlert(
                    AndroidUtils.getLocalizedText(
                        application,
                        "settings_launcher_launcher_log_export_success",
                        logFile,
                    ),
                ),
            )
        }
    }

    /** 申请录音权限：宿主走 MainActivity.permissionResultLauncher（对齐 :287-302）。 */
    fun onRequestAudioPermission() = sendEvent(LauncherSettingEvent.RequestAudioPermission)

    fun setAutoExitLauncher(enabled: Boolean) {
        prefs.edit().putBoolean("autoExitLauncher", enabled).apply()
        updateState { copy(autoExitLauncher = enabled) }
    }

    // ---------- 外观 ----------

    /** 主题模式（跟随系统/浅色/深色）：写 SP + setDefaultNightMode（对齐 :538-545）。 */
    fun setThemeMode(index: Int) {
        prefs.edit().putInt("themeMode", index).apply()
        updateState { copy(themeModeIndex = index) }
        val mode = when (index) {
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    // ---- 取色器（承接 FCLColorPickerDialog 三回调：拖动实时预览 / 确定保存 / 取消还原） ----

    /** 打开取色器：记录初始色用于取消还原（对齐 :303-359）。 */
    fun openColorPicker(target: ColorTarget) {
        val initColor = currentColorOf(target)
        updateState { copy(colorPicker = ColorPickerState(target, initColor, initColor)) }
    }

    /** 拖动实时预览：仅 apply 不 save（对齐 FCLColorPickerDialog.onColorChanged）。 */
    fun onColorPicking(color: Int) {
        val picker = currentState.colorPicker ?: return
        when (picker.target) {
            ColorTarget.PRIMARY -> engine.applyColor(color)
            ColorTarget.COLOR2 -> engine.applyColor2(color)
            ColorTarget.COLOR2_DARK -> engine.applyColor2Dark(color)
        }
        updateState { copy(colorPicker = picker.copy(currentColor = color)) }
    }

    /** 确定：applyAndSave 落盘（对齐 Listener.onPositive）。 */
    fun confirmColorPicker() {
        val picker = currentState.colorPicker ?: return
        when (picker.target) {
            ColorTarget.PRIMARY -> engine.applyAndSave(application, picker.currentColor)
            ColorTarget.COLOR2 -> engine.applyAndSave2(application, picker.currentColor)
            ColorTarget.COLOR2_DARK -> engine.applyAndSave2Dark(application, picker.currentColor)
        }
        updateState { copy(colorPicker = null) }
    }

    /** 取消/关闭：还原初始色（对齐 Listener.onNegative）。 */
    fun dismissColorPicker() {
        val picker = currentState.colorPicker ?: return
        when (picker.target) {
            ColorTarget.PRIMARY -> engine.applyColor(picker.initColor)
            ColorTarget.COLOR2 -> engine.applyColor2(picker.initColor)
            ColorTarget.COLOR2_DARK -> engine.applyColor2Dark(picker.initColor)
        }
        updateState { copy(colorPicker = null) }
    }

    /** 重置主题色（对齐 :439-447；注意 color2/color2Dark 重置值均为 #000000，与遗留一致）。 */
    fun resetThemeColor(target: ColorTarget) {
        when (target) {
            ColorTarget.PRIMARY -> engine.applyAndSave(application, application.getColor(R.color.default_theme_color))
            ColorTarget.COLOR2 -> engine.applyAndSave2(application, Color.parseColor("#000000"))
            ColorTarget.COLOR2_DARK -> engine.applyAndSave2Dark(application, Color.parseColor("#000000"))
        }
    }

    /** 从当前背景图 Palette 取色（对齐 :455-478，无背景图时静默忽略）。 */
    fun fetchColorFromBackground(target: ColorTarget) {
        val isDarkMode =
            (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val bitmap = (if (isDarkMode) theme.backgroundDk else theme.backgroundLt)?.bitmap ?: return
        val palette = Palette.from(bitmap).generate()
        val dominantColor = palette.getDominantColor(application.getColor(R.color.default_theme_color))
        when (target) {
            ColorTarget.PRIMARY -> {
                var color = palette.getMutedColor(dominantColor)
                if (theme.color == color) {
                    color = palette.getLightVibrantColor(dominantColor)
                }
                engine.applyAndSave(application, color)
            }

            ColorTarget.COLOR2 -> engine.applyAndSave2(application, palette.getVibrantColor(dominantColor))
            ColorTarget.COLOR2_DARK -> engine.applyAndSave2Dark(application, palette.getVibrantColor(dominantColor))
        }
    }

    // ---- 背景 / 指针 / 菜单图标（文件选择走宿主事件，结果处理在 LauncherSettingHost） ----

    fun onPickBackgroundLight() = sendEvent(LauncherSettingEvent.PickBackgroundLight)
    fun onPickBackgroundDark() = sendEvent(LauncherSettingEvent.PickBackgroundDark)
    fun onResetBackground(light: Boolean) = sendEvent(LauncherSettingEvent.ResetBackgroundImage(light))
    fun onPickLiveBackground() = sendEvent(LauncherSettingEvent.PickLiveBackground)
    fun onResetLiveBackground() = sendEvent(LauncherSettingEvent.ResetLiveBackground)
    fun onPickCursor() = sendEvent(LauncherSettingEvent.PickCursor)
    fun onPickMenuIcon() = sendEvent(LauncherSettingEvent.PickMenuIcon)

    /** 重置指针图：删除 cursor.png/gif（对齐 :512-519）。 */
    fun onResetCursor() {
        try {
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "cursor.png"))
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "cursor.gif"))
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Failed to delete cursor", e)
        }
    }

    /** 重置菜单图标：删除 menu_icon.png/gif（对齐 :503-510）。 */
    fun onResetMenuIcon() {
        try {
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "menu_icon.png"))
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "menu_icon.gif"))
        } catch (e: IOException) {
            Logging.LOG.log(Level.WARNING, "Failed to delete menu icon", e)
        }
    }

    /** 视频背景音量：实时写 SP + 通知宿主调 MainActivity.setLiveBackgroundVolume（对齐 :572-577）。 */
    fun setVideoBackgroundVolume(volume: Int) {
        prefs.edit().putInt("videoBackgroundVolume", volume).apply()
        updateState { copy(videoBackgroundVolume = volume) }
        sendEvent(LauncherSettingEvent.SyncLiveBackgroundVolume)
    }

    /** 忽略刘海：Window flags 需 Activity Window，走宿主事件；状态经 fullscreenFlow 回流。 */
    fun setIgnoreNotch(enabled: Boolean) = sendEvent(LauncherSettingEvent.ApplyIgnoreNotch(enabled))

    /** 关闭皮肤模型预览（对齐 :559-561；注意 Theme.ignoreSkinContainerProperty() 有 bug
     *  错误返回 fullscreen 属性，故不走属性桥，直读 isCloseSkinModel + 手动保存）。 */
    fun setCloseSkinModel(enabled: Boolean) {
        theme.setiIgnoreSkinContainer(enabled)
        Theme.saveTheme(application, theme)
        updateState { copy(closeSkinModel = enabled) }
    }

    /** 动画速度：写 setter 即持久化（init 里注册的订阅自动 saveTheme）。 */
    fun setAnimationSpeed(speed: Int) {
        theme.animationSpeed = speed.coerceIn(1, 20)
    }

    /** 震动时长：写 SP（对齐 :159-161）。 */
    fun setVibrationDuration(duration: Int) {
        prefs.edit().putInt("vibrationDuration", duration).apply()
        updateState { copy(vibrationDuration = duration) }
    }

    fun setDisableFullscreenInput(enabled: Boolean) {
        prefs.edit().putBoolean("disableFullscreenInput", enabled).apply()
        updateState { copy(disableFullscreenInput = enabled) }
    }

    /** 自定义启动器名称：TextWatcher 等价，逐键保存（对齐 :165-170）。 */
    fun setCustomLauncherName(name: String) {
        prefs.edit().putString("custom_launcher_name", name).apply()
        updateState { copy(customLauncherName = name) }
    }

    fun setAllowScreenshots(enabled: Boolean) {
        prefs.edit().putBoolean("allowScreenshots", enabled).apply()
        updateState { copy(allowScreenshots = enabled) }
    }

    // ---------- 下载 ----------

    fun setAutoChooseDownloadType(auto: Boolean) {
        ConfigHolder.config().setAutoChooseDownloadType(auto)
    }

    /** 版本列表源 Spinner：position → provider id 回写 config（对齐 FXUtils.bindSelection
     *  双向绑定 :180-189；写 setter 即持久化，Flow 回流刷新 UI）。 */
    fun setVersionListSource(index: Int) {
        AUTO_SOURCE_KEYS.getOrNull(index)?.let { ConfigHolder.config().setVersionListSource(it) }
    }

    /** 下载源 Spinner：position → provider id 回写 config（对齐 :190-198）。 */
    fun setDownloadType(index: Int) {
        RAW_SOURCE_KEYS.getOrNull(index)?.let { ConfigHolder.config().setDownloadType(it) }
    }

    /** 自动线程数：勾选时强制重置线程数为 DEFAULT_CONCURRENCY（对齐 :202-206）。 */
    fun setAutoDownloadThreads(auto: Boolean) {
        if (auto) ConfigHolder.config().setDownloadThreads(FetchTask.DEFAULT_CONCURRENCY)
        ConfigHolder.config().setAutoDownloadThreads(auto)
    }

    /** 线程数（SeekBar 双向绑定等价，范围 1~128，对齐 :207-209）。 */
    fun setDownloadThreads(threads: Int) {
        ConfigHolder.config().setDownloadThreads(threads.coerceIn(1, 128))
    }

    private fun currentColorOf(target: ColorTarget): Int = when (target) {
        ColorTarget.PRIMARY -> theme.color
        ColorTarget.COLOR2 -> theme._getColor2()
        ColorTarget.COLOR2_DARK -> theme.color2Dark
    }

    companion object {
        private const val ONE_DAY = 1000L * 60 * 60 * 24

        /** 版本列表源（自动模式）provider id 列表，顺序与 DownloadProviders.providersById 一致。 */
        val AUTO_SOURCE_KEYS: List<String> = ArrayList(DownloadProviders.providersById.keys)

        /** 下载源（手动模式）provider id 列表，顺序与 DownloadProviders.rawProviders 一致。 */
        val RAW_SOURCE_KEYS: List<String> = ArrayList(DownloadProviders.rawProviders.keys)

        private fun indexOfSource(keys: List<String>, value: String): Int =
            keys.indexOf(value).coerceAtLeast(0)
    }
}

/** 取色目标：主题色 / 内容色（浅色）/ 内容色（深色）。 */
enum class ColorTarget {
    PRIMARY,
    COLOR2,
    COLOR2_DARK,
}

/** 取色器弹窗状态：null 表示未打开。 */
data class ColorPickerState(
    val target: ColorTarget,
    val initColor: Int,
    val currentColor: Int,
)

/** 页面 UI 状态（不可变 data class，Compose 唯一渲染数据源）。 */
data class LauncherSettingUiState(
    // 通用
    val languageIndex: Int = 0,
    val autoExitLauncher: Boolean = false,
    // 外观
    val themeModeIndex: Int = 0,
    val themeColor: Int = 0xFF7797CF.toInt(),
    val themeColor2: Int = 0xFF000000.toInt(),
    val themeColor2Dark: Int = 0xFFFFFFFF.toInt(),
    val colorPicker: ColorPickerState? = null,
    val videoBackgroundVolume: Int = 100,
    val ignoreNotch: Boolean = false,
    val closeSkinModel: Boolean = false,
    val animationSpeed: Int = 8,
    val vibrationDuration: Int = 100,
    val disableFullscreenInput: Boolean = true,
    val customLauncherName: String = "",
    val allowScreenshots: Boolean = false,
    // 下载
    val autoChooseDownloadType: Boolean = true,
    val versionListSourceIndex: Int = 1,
    val downloadTypeIndex: Int = 1,
    val autoDownloadThreads: Boolean = true,
    val downloadThreads: Int = FetchTask.DEFAULT_CONCURRENCY,
)

/**
 * 一次性事件：需要 Activity/MainActivity 能力的副作用，由 Compose 侧 collect 后
 * 转交 [LauncherSettingHost] 处理（bridge-api.md §5.7）。
 */
sealed interface LauncherSettingEvent {
    /** 检查更新（宿主：UpdateChecker.checkManually + 失败弹窗）。 */
    data object CheckUpdate : LauncherSettingEvent

    /** 申请录音权限（宿主：permissionResultLauncher / 跳应用详情页）。 */
    data object RequestAudioPermission : LauncherSettingEvent

    /** 选择浅色背景图（png/jpg/jpeg）。 */
    data object PickBackgroundLight : LauncherSettingEvent

    /** 选择深色背景图（png/jpg/jpeg）。 */
    data object PickBackgroundDark : LauncherSettingEvent

    /** 重置浅/深背景图（删文件 + applyAndSave(view)，需 MainActivity.binding.background）。 */
    data class ResetBackgroundImage(val light: Boolean) : LauncherSettingEvent

    /** 选择视频背景（mp4，拷贝后 setupLiveBackground）。 */
    data object PickLiveBackground : LauncherSettingEvent

    /** 重置视频背景（删文件 + setupLiveBackground）。 */
    data object ResetLiveBackground : LauncherSettingEvent

    /** 选择指针图（png/gif）。 */
    data object PickCursor : LauncherSettingEvent

    /** 选择菜单图标（png/gif）。 */
    data object PickMenuIcon : LauncherSettingEvent

    /** 忽略刘海开关（需 Activity Window 改 flags + applyAndSave）。 */
    data class ApplyIgnoreNotch(val enabled: Boolean) : LauncherSettingEvent

    /** 视频背景音量变更（宿主调 MainActivity.setLiveBackgroundVolume）。 */
    data object SyncLiveBackgroundVolume : LauncherSettingEvent

    /** 通用提示弹窗（成功/失败信息，单按钮；isError 影响遗留回退弹窗的 AlertLevel）。 */
    data class ShowAlert(val message: String, val isError: Boolean = false) : LauncherSettingEvent

    /** 语言切换后的"重启生效"提示。 */
    data object ShowRestartHint : LauncherSettingEvent
}
