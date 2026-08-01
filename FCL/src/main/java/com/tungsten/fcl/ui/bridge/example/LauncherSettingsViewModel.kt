package com.tungsten.fcl.ui.bridge.example

import com.tungsten.fcl.setting.ConfigHolder
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fclcore.task.FetchTask

/**
 * 桥接层示例（小步骤 2.3）：启动器设置页 LauncherSettingPage "下载"区域的 Compose 化范式。
 *
 * 对应遗留实现（interaction-map.md §6.2、G4）：
 * - `checkAutoThreads.checkProperty().bindBidirectional(config().autoDownloadThreadsProperty())`
 *   + 勾选自动时强制重置线程数（LauncherSettingPage.java:199-206）；
 * - `threads.progressProperty().bindBidirectional(config().downloadThreadsProperty())`
 *   （SeekBar 双向绑定，LauncherSettingPage.java:207-209，SeekBar 范围 1~128）。
 *
 * 范式要点：
 * - fakefx Property 经 asMutableStateFlow() 双向桥接——写 flow 即写 Config，
 *   ConfigHolder 监听 Config 变更会自动落盘 config.json，无需手动 save；
 * - fakefx 流统一投影进单一 UiState（observeIntoState），Compose 只读 UiState；
 * - 业务规则（勾选自动重置线程数）收在 ViewModel 方法里，不留在 Composable 里；
 * - 一次性副作用（文件选择）走 events，宿主经 LegacyBridge 转交 MainActivity.fileLauncher。
 *
 * 本类仅作迁移范式参考，阶段三正式迁移设置页时按此模式重写并替换本示例。
 * 注意：ConfigHolder.config() 未初始化（Splash 流程完成前）时会抛异常，
 * 真实页面在宿主保证 Config 就绪后再创建 ViewModel。
 */
class LauncherSettingsViewModel : FCLViewModel<LauncherSettingsUiState, LauncherSettingsEvent>(
    LauncherSettingsUiState()
) {

    /** 自动线程数开关：Config fakefx BooleanProperty 的双向承接（写 flow 即持久化）。 */
    private val autoDownloadThreadsFlow =
        ConfigHolder.config().autoDownloadThreadsProperty().asMutableStateFlow()

    /** 线程数：Config fakefx IntegerProperty 的双向承接（JavaFX 惯例，类型实参为 Number）。 */
    private val downloadThreadsFlow =
        ConfigHolder.config().downloadThreadsProperty().asMutableStateFlow()

    init {
        // fakefx 流 → UiState 投影：UiState 是 Compose 的唯一渲染数据源
        autoDownloadThreadsFlow.observeIntoState { copy(autoDownloadThreads = it) }
        downloadThreadsFlow.observeIntoState { copy(downloadThreads = it.toInt()) }
    }

    /** 承接 LauncherSettingPage.java:202-206：勾选自动时强制重置线程数为 DEFAULT_CONCURRENCY。 */
    fun setAutoDownloadThreads(auto: Boolean) {
        if (auto) downloadThreadsFlow.value = FetchTask.DEFAULT_CONCURRENCY
        autoDownloadThreadsFlow.value = auto
    }

    /** 线程数变更（对应 SeekBar 双向绑定，范围对齐布局文件 android:min=1 / android:max=128）。 */
    fun setDownloadThreads(threads: Int) {
        downloadThreadsFlow.value = threads.coerceIn(1, 128)
    }

    /** "选择浅色背景图"点击：遗留实现走 MainActivity.fileLauncher（SAF/自研双路径），改为一次性事件。 */
    fun onPickBackgroundImage() {
        sendEvent(LauncherSettingsEvent.PickBackgroundImage)
    }
}

/** 页面 UI 状态（不可变 data class，Compose 唯一渲染数据源）。 */
data class LauncherSettingsUiState(
    val autoDownloadThreads: Boolean = true,
    val downloadThreads: Int = FetchTask.DEFAULT_CONCURRENCY,
)

/** 一次性事件：由 Compose 侧 collect 后转交 LegacyBridge / 宿主 Activity 处理。 */
sealed interface LauncherSettingsEvent {
    /** 请求选择浅色背景图（宿主用 MainActivity.fileLauncher 单选 png/jpg/jpeg）。 */
    data object PickBackgroundImage : LauncherSettingsEvent
}
