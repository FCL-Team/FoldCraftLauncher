package com.tungsten.fcl.ui.manage.compose

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.viewModelScope
import com.mio.manager.RendererManager.getRenderer
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Controllers
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.VersionSetting
import com.tungsten.fcl.ui.bridge.FCLViewModel
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fclauncher.plugins.DriverPlugin
import com.tungsten.fclcore.event.Event
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.flow.FlowSubscriptions
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.platform.MemoryUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.logging.Level

/**
 * 页面加载请求：由 [ComposeVersionSettingPage]（FCLCommonPage 壳）经 StateFlow 推给
 * Compose 侧的 ViewModel（PageManager 的 loadVersion 早于 ComposeView 安装，StateFlow
 * 保留最新值，组合建立后补放）。
 */
data class VersionSettingLoadRequest(
    val profile: Profile,
    val versionId: String?,
)

/**
 * 版本设置页 ViewModel（小步骤 3.3b）：VersionSettingPage.kt 的 Compose 化承接，
 * 覆盖全局（SettingUI Tab0，globalSetting=true）与单版本（ManageUI Tab0）两种形态。
 *
 * 17 组 observable 绑定承接（interaction-map §4.4，VersionSettingPage.kt:286-350）：
 * - 15 组 VersionSetting 属性（4 String + 10 Boolean + maxMemory）：阶段 4a 起
 *   VersionSetting 属性已是 MutableStateFlow，Compose 写 flow.value 即写 VersionSetting
 *   （写即持久化语义不变，FCLGameRepository/Profile 自动落盘），同一 Flow collect
 *   回流刷新 UiState；
 * - 第 16 组 specialSettingSwitch ↔ enableSpecificSettings（局部 BooleanProperty）：
 *   收敛为 UiState 字段 + [setEnableSpecificSettings] 语义化方法（specialize/globalize
 *   联动收在此处）；
 * - 第 17 组 barMemory.progressProperty ↔ maxMemory（局部 IntegerProperty 中转）：
 *   Compose Slider 直连 state.maxMemory，不再需要中转属性。
 *
 * 重绑策略：旧页面 loadVersion 时 FXUtils.unbindXxx/bindXxx 整页重绑；此处每次
 * [loadVersion] 取消上一个 bindJob（collect 协程随之取消）并在新子 scope 上重新
 * collect 全部 Flow，等价于整页重绑。
 *
 * 额外桥接（旧版是"弹窗回调里手动 setText"，新版改为属性流驱动，显示永远与数据一致）：
 * java/controller/graphicsBackend/renderer/driver 5 个显示属性同样以双向流投影进
 * UiState——渲染器/驱动弹窗内部会直接写 VersionSetting 属性，流自动回流刷新文本。
 */
class VersionSettingViewModel(
    private val application: Application,
    private val globalSetting: Boolean,
    /** 对齐遗留 `id == ManagePageManager.PAGE_ID_MANAGE_SETTING`：仅管理页需要
     *  游戏目录隔离变更时联动刷新 Mod/World 页。 */
    private val notifyRunDirectoryChange: Boolean,
) : FCLViewModel<VersionSettingUiState, VersionSettingEvent>(
    VersionSettingUiState(globalSetting = globalSetting),
) {

    private var profile: Profile? = null
    private var versionId: String? = null
    private var versionSetting: VersionSetting? = null
    private var bindJob: Job? = null

    // ---------- 双向桥（每次 loadVersion 重建） ----------

    private var javaArgsFlow: MutableStateFlow<String>? = null
    private var minecraftArgsFlow: MutableStateFlow<String>? = null
    private var uuidFlow: MutableStateFlow<String>? = null
    private var serverIpFlow: MutableStateFlow<String>? = null
    private var autoMemoryFlow: MutableStateFlow<Boolean>? = null
    private var isolateGameDirFlow: MutableStateFlow<Boolean>? = null
    private var pojavBigCoreFlow: MutableStateFlow<Boolean>? = null
    private var notCheckGameFlow: MutableStateFlow<Boolean>? = null
    private var notCheckJVMFlow: MutableStateFlow<Boolean>? = null
    private var notCheckModFlow: MutableStateFlow<Boolean>? = null
    private var debugLogFlow: MutableStateFlow<Boolean>? = null
    private var forceResolutionFlow: MutableStateFlow<Boolean>? = null
    private var beGestureFlow: MutableStateFlow<Boolean>? = null
    private var vkDriverSystemFlow: MutableStateFlow<Boolean>? = null

    /** maxMemory：VersionSetting 的 MutableStateFlow<Int>（阶段 4a）。 */
    private var maxMemoryFlow: MutableStateFlow<Int>? = null

    // 显示属性（弹窗写入后自动回流）
    private var javaFlow: MutableStateFlow<String>? = null
    private var controllerFlow: MutableStateFlow<String>? = null
    private var graphicsBackendFlow: MutableStateFlow<String>? = null
    private var rendererFlow: MutableStateFlow<String>? = null
    private var driverFlow: MutableStateFlow<String>? = null

    init {
        updateState {
            copy(
                totalMemoryMB = MemoryUtils.getTotalDeviceMemory(application),
                usedMemoryMB = MemoryUtils.getUsedDeviceMemory(application),
            )
        }
    }

    /** 对齐 VersionSettingPage.loadVersion(:263-392)：换版本/全局↔实例切换时整体重绑。 */
    fun loadVersion(profile: Profile, newVersionId: String?) {
        this.profile = profile
        this.versionId = newVersionId
        val vs = profile.getVersionSetting(newVersionId)
        versionSetting = vs
        vs.checkController()

        // 驱动校正（对齐 :371-383）：选中的驱动不在插件列表时回退 Turnip
        if (vs.driver != "Turnip") {
            var matched = false
            for (driver in DriverPlugin.driverList) {
                if (driver.driver == vs.driver) {
                    DriverPlugin.selected = driver
                    vs.driver = driver.driver
                    matched = true
                }
            }
            if (!matched) {
                vs.driver = "Turnip"
            }
        }

        // 整页重绑：取消旧 scope（collect 协程随之取消）
        bindJob?.cancel()
        val job = SupervisorJob(viewModelScope.coroutineContext[Job])
        bindJob = job
        val scope = CoroutineScope(viewModelScope.coroutineContext + job)

        updateState {
            copy(
                loaded = true,
                versionId = newVersionId,
                modpack = newVersionId != null && profile.repository.isModpack(newVersionId),
                // 全局页等价遗留 enableSpecificSettings.set(true)（settingLayout 常显）；
                // 单版本页 = !usesGlobal
                enableSpecificSettings = newVersionId == null || !vs.isUsesGlobal,
                usedMemoryMB = MemoryUtils.getUsedDeviceMemory(application),
            )
        }

        // ---- 17 组绑定里的 15 组 VersionSetting 属性（4 String + 10 Boolean + maxMemory） ----
        // 阶段 4a：VersionSetting 属性已是 MutableStateFlow，直连（写 flow.value 即写
        // VersionSetting，FCLGameRepository/Profile 经 addPropertyChangedListener 落盘），
        // 同一 Flow collect 回流刷新 UiState，不再需要 observable 双向桥。
        javaArgsFlow = vs.javaArgsFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(javaArgs = v) } }
        minecraftArgsFlow = vs.minecraftArgsFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(minecraftArgs = v) } }
        uuidFlow = vs.uuidFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(uuid = v) } }
        serverIpFlow = vs.serverIpFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(serverIp = v) } }
        autoMemoryFlow = vs.autoMemoryFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(autoMemory = v) } }
        isolateGameDirFlow = vs.isolateGameDirFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(isolateGameDir = v) } }
        pojavBigCoreFlow = vs.pojavBigCoreFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(pojavBigCore = v) } }
        notCheckGameFlow = vs.notCheckGameFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(notCheckGame = v) } }
        notCheckJVMFlow = vs.notCheckJVMFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(notCheckJVM = v) } }
        notCheckModFlow = vs.notCheckModFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(notCheckMod = v) } }
        debugLogFlow = vs.debugLogFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(debugLog = v) } }
        forceResolutionFlow = vs.forceResolutionFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(forceResolution = v) } }
        beGestureFlow = vs.beGestureFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(beGesture = v) } }
        vkDriverSystemFlow = vs.vkDriverSystemFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(vkDriverSystem = v) } }
        maxMemoryFlow = vs.maxMemoryFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(maxMemory = v) } }

        // ---- 显示属性（旧版弹窗回调里手动 setText，新版属性流驱动） ----
        javaFlow = vs.javaFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(javaName = v) } }
        controllerFlow = vs.controllerFlow
            .also { f ->
                f.collectIntoState(scope) { v ->
                    copy(
                        controllerId = v,
                        controllerName = if (Controllers.isInitialized()) {
                            Controllers.findControllerById(v).name
                        } else {
                            controllerName
                        },
                    )
                }
            }
        graphicsBackendFlow = vs.graphicsBackendFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(graphicsBackend = v) } }
        rendererFlow = vs.rendererFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(rendererDes = getRenderer(v).des) } }
        driverFlow = vs.driverFlow
            .also { f -> f.collectIntoState(scope) { v -> copy(driver = v) } }

        // 控制器名称（对齐 :360-364 的 Controllers.addCallback）
        Controllers.addCallback {
            updateState { copy(controllerName = Controllers.findControllerById(vs.controller).name) }
        }

        // 游戏目录隔离变更联动刷新 Mod/World 页（对齐 :283-289 + :337-339，仅管理页）
        if (notifyRunDirectoryChange) {
            val subscription = FlowSubscriptions.subscribe(vs.isolateGameDirFlow) {
                ManagePageManager.instance?.onRunDirectoryChange(profile, newVersionId)
            }
            job.invokeOnCompletion { subscription.cancel() }
        }

        loadIcon()
    }

    private fun <T> Flow<T>.collectIntoState(
        scope: CoroutineScope,
        reducer: VersionSettingUiState.(T) -> VersionSettingUiState,
    ) {
        scope.launch { collect { v -> updateState { reducer(v) } } }
    }

    /** 对齐 onResume(:258-261)：页面回到前台刷新已用内存（驱动内存文本重组）。 */
    fun refreshMemory() {
        updateState { copy(usedMemoryMB = MemoryUtils.getUsedDeviceMemory(application)) }
    }

    // ---------- 17 组绑定的写入口（写 flow 即写 observable 属性，即持久化） ----------

    fun setJavaArgs(value: String) {
        javaArgsFlow?.value = value
    }

    fun setMinecraftArgs(value: String) {
        minecraftArgsFlow?.value = value
    }

    fun setUuid(value: String) {
        uuidFlow?.value = value
    }

    fun setServerIp(value: String) {
        serverIpFlow?.value = value
    }

    fun setAutoMemory(value: Boolean) {
        autoMemoryFlow?.value = value
    }

    fun setMaxMemory(value: Int) {
        maxMemoryFlow?.value = value.coerceIn(0, currentState.totalMemoryMB)
    }

    fun setIsolateGameDir(value: Boolean) {
        isolateGameDirFlow?.value = value
    }

    fun setPojavBigCore(value: Boolean) {
        pojavBigCoreFlow?.value = value
    }

    fun setNotCheckGame(value: Boolean) {
        notCheckGameFlow?.value = value
    }

    fun setNotCheckJVM(value: Boolean) {
        notCheckJVMFlow?.value = value
    }

    fun setNotCheckMod(value: Boolean) {
        notCheckModFlow?.value = value
    }

    fun setDebugLog(value: Boolean) {
        debugLogFlow?.value = value
    }

    fun setBeGesture(value: Boolean) {
        beGestureFlow?.value = value
    }

    /** 系统 Vulkan 驱动开关（对齐 :192-209）：Adreno GPU 开启时弹 INFO 提示；
     *  驱动行可见性由 state.vkDriverSystem 驱动（等价 driverContainer 显隐）。 */
    fun setVkDriverSystem(value: Boolean) {
        vkDriverSystemFlow?.value = value
        if (value && AndroidUtils.isAdrenoGPU()) {
            sendEvent(VersionSettingEvent.ShowVulkanDriverSystemInfo)
        }
    }

    /** 强制分辨率开关（对齐 :220-252）：开启时弹分辨率输入框，取消则回滚开关。 */
    fun setForceResolution(value: Boolean) {
        forceResolutionFlow?.value = value
        if (value) {
            sendEvent(VersionSettingEvent.EditForceResolution)
        }
    }

    /** 分辨率输入框取消（对齐 EditDialog.onCancelListener → isChecked = false）。 */
    fun onForceResolutionDialogCancel() {
        forceResolutionFlow?.value = false
    }

    /** 专用设置开关（第 16 组，对齐 :183-191 的 enableSpecificSettings ChangeListener）：
     *  specialize/globalize + 整页重载（旧版经 Schedulers.androidUIThread 重载）。 */
    fun setEnableSpecificSettings(enabled: Boolean) {
        val p = profile ?: return
        val vid = versionId ?: return
        if (enabled == currentState.enableSpecificSettings) return
        updateState { copy(enableSpecificSettings = enabled) }
        // 不能写 versionSetting.isUsesGlobal：versionSetting 可能是全局对象
        //（全局对象 usesGlobal 恒为 true），与遗留注释一致。
        if (enabled) p.repository.specializeVersionSetting(vid)
        else p.repository.globalizeVersionSetting(vid)
        loadVersion(p, vid)
    }

    // ---------- 图标（对齐 :394-440；SAF 选图由宿主事件取路径后回调 onIconPicked） ----------

    fun onEditIcon() {
        if (versionId == null) return
        sendEvent(VersionSettingEvent.PickIcon)
    }

    fun onIconPicked(path: String?) {
        val p = profile ?: return
        val vid = versionId ?: return
        path ?: return
        val selectedFile = File(path)
        val iconFile = p.repository.getVersionIconFile(vid)
        try {
            FileUtils.copyFile(selectedFile, iconFile)
            p.repository.onVersionIconChanged.fireEvent(Event(this))
            loadIcon()
        } catch (e: IOException) {
            Logging.LOG.log(Level.SEVERE, "Failed to copy icon file from $selectedFile to $iconFile", e)
        }
    }

    fun onDeleteIcon() {
        val p = profile ?: return
        val vid = versionId ?: return
        val iconFile = p.repository.getVersionIconFile(vid)
        if (iconFile.exists()) iconFile.delete()
        p.repository.onVersionIconChanged.fireEvent(Event(this))
        loadIcon()
    }

    private fun loadIcon() {
        val p = profile ?: return
        val vid = versionId ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val icon = p.repository.getVersionIconImage(vid)
            updateState { copy(iconDrawable = icon) }
        }
    }

    // ---------- 弹窗/跳转入口（业务动作在 Host，VM 只发事件） ----------

    fun onEditJava() = sendEvent(VersionSettingEvent.ShowJavaManage)
    fun onInstallJava() = sendEvent(VersionSettingEvent.ShowInstallJava)

    /** 选择控制器（对齐 :449-461）：未初始化 Toast。 */
    fun onEditController() {
        if (Controllers.isInitialized()) {
            sendEvent(VersionSettingEvent.ShowControllerSelect)
        } else {
            sendEvent(VersionSettingEvent.ToastControllersLoading)
        }
    }

    fun onInstallController() = sendEvent(VersionSettingEvent.JumpToControllerRepo)
    fun onEditGraphicsBackend() = sendEvent(VersionSettingEvent.ShowGraphicsBackendSelect)
    fun onEditRenderer() = sendEvent(VersionSettingEvent.ShowRendererSelect)
    fun onInstallRenderer() = sendEvent(VersionSettingEvent.ShowInstallRenderer)
    fun onEditDriver() = sendEvent(VersionSettingEvent.ShowDriverSelect)
    fun onInstallDriver() = sendEvent(VersionSettingEvent.ShowInstallDriver)
    fun onEditEnv() = sendEvent(VersionSettingEvent.EditEnvVars)

    /** JVM/游戏参数全屏编辑（对齐 :210-215 的长按 FullEditDialog）。 */
    fun onFullEditArgs(target: ArgsTarget) = sendEvent(
        VersionSettingEvent.FullEditArgs(
            target,
            if (target == ArgsTarget.JVM) currentState.javaArgs else currentState.minecraftArgs,
        ),
    )

    // ---------- 弹窗回调（Host 取到结果后回写） ----------

    fun onJavaSelected(name: String) {
        javaFlow?.value = name
    }

    fun onControllerSelected(id: String) {
        controllerFlow?.value = id
    }

    fun onGraphicsBackendSelected(name: String) {
        graphicsBackendFlow?.value = name
    }

    /** 当前控制器 id（ShowControllerSelect 弹窗初始选中项）。 */
    fun currentControllerId(): String = currentState.controllerId
}

/** JVM/游戏参数全屏编辑目标。 */
enum class ArgsTarget { JVM, MINECRAFT }

/** 页面 UI 状态（不可变 data class，Compose 唯一渲染数据源）。 */
data class VersionSettingUiState(
    val globalSetting: Boolean = false,
    val loaded: Boolean = false,
    val versionId: String? = null,
    /** 整合包：禁用游戏目录隔离开关与专用设置开关（对齐 disableProperty().bind(modpack)）。 */
    val modpack: Boolean = false,
    val enableSpecificSettings: Boolean = true,
    val iconDrawable: Drawable? = null,
    // 显示属性（弹窗选择结果）
    val javaName: String = "Auto",
    val controllerId: String = "",
    val controllerName: String = "",
    val graphicsBackend: String = "default",
    val rendererDes: String = "",
    val driver: String = "Turnip",
    // 17 组绑定字段
    val javaArgs: String = "",
    val minecraftArgs: String = "",
    val uuid: String = "",
    val serverIp: String = "",
    val autoMemory: Boolean = true,
    val maxMemory: Int = 0,
    val isolateGameDir: Boolean = true,
    val pojavBigCore: Boolean = false,
    val notCheckGame: Boolean = false,
    val notCheckJVM: Boolean = false,
    val notCheckMod: Boolean = false,
    val debugLog: Boolean = false,
    val forceResolution: Boolean = false,
    val beGesture: Boolean = true,
    val vkDriverSystem: Boolean = false,
    // 内存展示
    val totalMemoryMB: Int = 1,
    val usedMemoryMB: Int = 0,
)

/**
 * 一次性事件：需要 Activity/MainActivity/遗留对话框能力的副作用。
 * 带 ViewModel 回调的事件（PickIcon/ShowJavaManage/ShowControllerSelect/
 * ShowGraphicsBackendSelect/EditForceResolution/FullEditArgs）在 Screen 的
 * events collect 里接线，其余由 [VersionSettingHost.handle] 处理。
 */
sealed interface VersionSettingEvent {
    data object PickIcon : VersionSettingEvent
    data object ShowJavaManage : VersionSettingEvent
    data object ShowInstallJava : VersionSettingEvent
    data object ShowControllerSelect : VersionSettingEvent

    /** 跳转控制器 UI 仓库页（跨 UI 硬编码跳转，interaction-map G11）。 */
    data object JumpToControllerRepo : VersionSettingEvent
    data object ShowGraphicsBackendSelect : VersionSettingEvent
    data object ShowRendererSelect : VersionSettingEvent
    data object ShowInstallRenderer : VersionSettingEvent
    data object ShowDriverSelect : VersionSettingEvent
    data object ShowInstallDriver : VersionSettingEvent
    data object EditEnvVars : VersionSettingEvent
    data object EditForceResolution : VersionSettingEvent
    data class FullEditArgs(val target: ArgsTarget, val current: String) : VersionSettingEvent
    data object ShowVulkanDriverSystemInfo : VersionSettingEvent
    data object ToastControllersLoading : VersionSettingEvent
}
