package com.tungsten.fcl.ui.bridge

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tungsten.fcl.FCLApplication
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLTaskDialogContent
import com.tungsten.fcl.ui.compose.FCLTaskDialogState
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.TaskExecutor
import com.tungsten.fclcore.task.TaskListener
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.ui.FCLTempPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.window.WindowDialog
import java.util.function.Consumer
import java.util.logging.Level

/**
 * 迁移期桥接层（小步骤 2.3）：Compose 界面与遗留逻辑层（UIManager / PageManager / 静态单例）
 * 之间的双向通信入口。
 *
 * 设计约束（interaction-map.md 的全局事实）：
 * - G1：双层自研导航（UIManager.switchUI + PageManager 页面栈/临时页栈），无 NavController；
 * - G2：返回键被 MainActivity.onKeyDown 拦截后走 UIManager.onBackPressed 链，不经
 *   OnBackPressedDispatcher；
 * - G10：静态单例反向调用遍布（UIManager.instance、各 PageManager.instance）。
 *
 * 因此迁移期内不重建导航体系，桥接层只做两件事：
 * - 方向一（Compose → 遗留）：把"切 UI / 压临时页 / 关临时页 / 触发返回链"封装成对
 *   UIManager/PageManager 单例的安全调用，供已迁移的 Compose 页面使用；
 * - 方向二（遗留 → Compose）：遗留代码（多为 Java 静态逻辑）经 [requestAlertDialog] 投递
 *   弹窗请求，由 Compose 根部的 [LegacyDialogHost] 以 Miuix WindowDialog 渲染。
 *
 * 本对象是迁移期临时设施：全部页面迁移完成后应替换为 Navigation 事件 + ViewModel，
 * 届时本文件整体删除。使用指引见 docs/migration/bridge-api.md。
 */
object LegacyBridge {

    private fun logNotReady(api: String) {
        Logging.LOG.log(Level.WARNING, "LegacyBridge.$api: UIManager 尚未初始化，调用被忽略")
    }

    // ---------- 方向一：Compose → 遗留导航/行为 ----------

    /** 安全获取 UIManager 单例（@JvmStatic lateinit，MainActivity 初始化完成前为 null）。 */
    @JvmStatic
    fun uiManager(): UIManager? = runCatching { UIManager.instance }.getOrNull()

    /**
     * 当前前台 Activity（FCLApplication 的 ActivityLifecycleCallbacks 维护，可能为 null）。
     * 供需要 Activity 的遗留 API 使用（如 MainActivity.fileLauncher 文件选择、UpdateChecker）。
     */
    @JvmStatic
    fun currentActivity(): Activity? = FCLApplication.getCurrentActivity()

    /**
     * 切换顶层 UI（等价于左侧菜单点击，interaction-map G1）。
     * Compose 侧取目标 UI 实例的方式：`LegacyBridge.uiManager()?.settingUI` 等。
     * @return UIManager 未就绪时返回 false（调用被忽略）。
     */
    @JvmStatic
    fun switchUI(ui: FCLCommonUI): Boolean {
        val manager = uiManager() ?: run {
            logNotReady("switchUI")
            return false
        }
        manager.switchUI(ui)
        return true
    }

    /** 向指定 PageManager 压入临时页（对应 PageManager.showTempPage，interaction-map G1）。 */
    @JvmStatic
    fun showTempPage(pageManager: PageManager, page: FCLTempPage) {
        pageManager.showTempPage(page)
    }

    /**
     * 关闭指定 PageManager 的栈顶临时页（对应 PageManager.dismissCurrentTempPage）。
     * @return 是否确实有临时页被关闭；可直接作为 Compose BackHandler 的"是否消费返回键"依据。
     */
    @JvmStatic
    fun dismissCurrentTempPage(pageManager: PageManager): Boolean {
        if (!pageManager.canReturn()) return false
        pageManager.dismissCurrentTempPage()
        return true
    }

    /**
     * 触发完整遗留返回链：当前 UI 的 onBackPressed（内部含 PageManager 临时页栈回退）
     * → 未消费时走 MainActivity 注册的默认返回事件（主页退出 / 非主页回主页，interaction-map G2）。
     * 等价于用户按物理返回键。
     */
    @JvmStatic
    fun onBackPressed(): Boolean {
        val manager = uiManager() ?: run {
            logNotReady("onBackPressed")
            return false
        }
        manager.onBackPressed()
        return true
    }

    // ---------- 方向二：遗留代码 → Compose 弹窗 ----------

    /**
     * Compose Alert 弹窗请求（对应遗留 FCLAlertDialog 的 title/message/正负按钮形态）。
     *
     * 按钮回调用 [Consumer]&lt;Boolean&gt;（true=点了确定，false=取消/关闭），
     * 便于 Java 遗留代码直接传入；回调保证在主线程执行（由 Compose 侧触发）。
     */
    class AlertDialogRequest(
        val title: String?,
        val message: String?,
        val positiveText: String?,
        val negativeText: String?,
        val onResult: Consumer<Boolean>?,
    )

    private val _alertDialogRequest = MutableStateFlow<AlertDialogRequest?>(null)

    /** 当前待处理的弹窗请求；由 Compose 根部的 [LegacyDialogHost] 订阅。 */
    val alertDialogRequest: StateFlow<AlertDialogRequest?> = _alertDialogRequest.asStateFlow()

    /**
     * 遗留代码请求弹出一个 Miuix Alert 弹窗。可从任意线程调用。
     *
     * 单槽位语义（对齐 FCLAlertDialog 同一时刻只弹一个的实际用法）：若已有未处理的请求，
     * 本调用返回 false，调用方应回退到遗留 FCLAlertDialog，避免请求被静默覆盖。
     * 队列化（多弹窗排队）在确有需要时再扩展。
     *
     * 注意：请求只有被 [LegacyDialogHost] 消费才会真正显示——宿主未安装（页面未迁移
     * 或宿主不在前台）时请求会一直挂着。遗留代码在纯 View 页面里应继续直接使用
     * FCLAlertDialog，本通道仅服务于"弹窗触发时前台是 Compose 页面"的场景。
     */
    @JvmStatic
    fun requestAlertDialog(
        title: String?,
        message: String?,
        positiveText: String?,
        negativeText: String?,
        onResult: Consumer<Boolean>?,
    ): Boolean {
        val request = AlertDialogRequest(title, message, positiveText, negativeText, onResult)
        return _alertDialogRequest.compareAndSet(null, request)
    }

    /** 由 [LegacyDialogHost] 在用户做出选择后调用：清空槽位并回调结果（主线程）。 */
    internal fun resolveAlertDialog(positive: Boolean) {
        val request = _alertDialogRequest.value ?: return
        _alertDialogRequest.value = null
        request.onResult?.accept(positive)
    }

    /**
     * Compose 任务进度弹窗请求（小步骤 3.2，对应遗留 TaskDialog 形态，interaction-map G5）。
     *
     * @param executor 已构造未 start 的任务执行器；进度/阶段/消息经 [FCLTaskDialogState] 订阅。
     * @param cancelAction 取消按钮附加动作（executor.cancel() 与关闭由宿主自动完成）；
     *                     传 null = 取消按钮置灰（对应遗留 setCancel(null)）。
     * @param autoClose 任务结束（onStop）自动关闭弹窗。
     */
    class TaskDialogRequest(
        val title: String?,
        val executor: TaskExecutor,
        val cancelAction: Runnable?,
        val autoClose: Boolean,
    )

    private val _taskDialogRequest = MutableStateFlow<TaskDialogRequest?>(null)

    /** 当前待处理的任务弹窗请求；由 Compose 根部的 [LegacyDialogHost] 订阅。 */
    val taskDialogRequest: StateFlow<TaskDialogRequest?> = _taskDialogRequest.asStateFlow()

    /**
     * 遗留代码请求弹出一个 Miuix 任务进度弹窗。可从任意线程调用。
     *
     * 单槽位语义与 [requestAlertDialog] 一致：已有未处理请求时返回 false，调用方应回退
     * 遗留 TaskDialog（或改用 ui/compose/MiuixTaskDialog 命令式封装）。
     *
     * 注意：请求只有被 [LegacyDialogHost] 消费才会真正显示。本通道服务于"弹窗触发时前台
     * 是 Compose 页面"的场景；纯 View 页面里的任务弹窗建议直接用
     * ui/compose/MiuixTaskDialog（自带平台 window，无需宿主）。
     */
    @JvmStatic
    @JvmOverloads
    fun requestTaskDialog(
        title: String?,
        executor: TaskExecutor,
        cancelAction: Runnable?,
        autoClose: Boolean = true,
    ): Boolean {
        val request = TaskDialogRequest(title, executor, cancelAction, autoClose)
        return _taskDialogRequest.compareAndSet(null, request)
    }

    /** 由 [LegacyDialogHost] 在任务弹窗关闭（autoClose/取消）后调用：清空槽位（主线程）。 */
    internal fun resolveTaskDialog() {
        _taskDialogRequest.value = null
    }

    // ---------- Compose 侧宿主 API ----------

    /**
     * 把一段 Compose 内容包装成可嵌入遗留 View 体系的 [ComposeView]
     * （对应"Compose 页面内嵌在现有 Activity 体系"的落点，例如替换 FCLCommonPage 的
     * contentView、或压入自定义 FCLTempPage）：
     * - 自动套 [FCLTheme]（Miuix 主题，见 theme-mapping.md）；
     * - 组合销毁策略为 [ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed]：
     *   跟随宿主 Activity 的 ViewTree 生命周期，页面 View 被移除/销毁时组合自动释放，
     *   与 FCLCommonPage 的 View 体系生命周期对齐。
     *
     * 主题接入（小步骤 3.1 落地、3.2 抽取复用）：主题模式/ThemeEngine 主题色的解析
     * 已抽为 `FCLTheme(context)` 环境自解析重载（ui/theme/FCLTheme.kt），本方法与
     * ui/compose/FCLComposeDialog 共用同一份实现。
     *
     * 用法：
     * ```kotlin
     * val view = LegacyBridge.createComposeView(context) { MyMigratedPage() }
     * parent.addView(view)
     * ```
     */
    @JvmStatic
    fun createComposeView(
        context: Context,
        content: @Composable () -> Unit,
    ): ComposeView = ComposeView(context).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            FCLTheme(context, content = content)
        }
    }

    /**
     * 方向二的 Compose 宿主：订阅 [alertDialogRequest] 与 [taskDialogRequest] 并以
     * Miuix WindowDialog 渲染。
     *
     * 每个 Compose 根（setContent / createComposeView 的内容树）只安装一次，放在页面
     * 内容的最外层即可。选用 window/WindowDialog 而非 overlay/OverlayDialog：
     * WindowDialog 走平台 Dialog window，不依赖 Miuix Scaffold 祖先（OverlayDialog 依赖
     * LocalDialogStates，无 Scaffold 时无法渲染），且独立 window 的形态与遗留 FCLDialog
     * 语义一致（interaction-map G7）。
     *
     * Alert：点遮罩/返回键关闭视为"取消"（onResult 回调 false）。
     * Task：不可取消（onDismissRequest = null），autoClose 任务结束自动关闭；
     * 取消按钮 = executor.cancel() + cancelAction + 关闭。
     */
    @Composable
    fun LegacyDialogHost() {
        val request by alertDialogRequest.collectAsStateWithLifecycle()
        val current = request
        if (current != null) {
            WindowDialog(
                show = true,
                title = current.title,
                summary = current.message,
                onDismissRequest = { resolveAlertDialog(false) },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    current.negativeText?.let { negative ->
                        TextButton(text = negative, onClick = { resolveAlertDialog(false) })
                        Spacer(Modifier.width(8.dp))
                    }
                    TextButton(
                        text = current.positiveText ?: "OK",
                        onClick = { resolveAlertDialog(true) },
                    )
                }
            }
        }

        val taskRequest by taskDialogRequest.collectAsStateWithLifecycle()
        taskRequest?.let { task ->
            val context = LocalContext.current
            val state = remember(task) { FCLTaskDialogState(context, task.executor) }
            DisposableEffect(state) {
                state.attach()
                val active = java.util.concurrent.atomic.AtomicBoolean(true)
                if (task.autoClose) {
                    task.executor.addTaskListener(object : TaskListener() {
                        override fun onStop(success: Boolean, executor: TaskExecutor) {
                            if (active.get()) {
                                Schedulers.androidUIThread().execute { resolveTaskDialog() }
                            }
                        }
                    })
                }
                onDispose {
                    active.set(false)
                    state.dispose()
                }
            }
            FCLDialog(
                show = true,
                onDismissRequest = null,
                title = task.title,
            ) {
                FCLTaskDialogContent(
                    state = state,
                    cancelText = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                    onCancel = task.cancelAction?.let { action ->
                        {
                            task.executor.cancel()
                            action.run()
                            resolveTaskDialog()
                        }
                    },
                )
            }
        }
    }
}
