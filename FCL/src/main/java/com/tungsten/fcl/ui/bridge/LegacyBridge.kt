package com.tungsten.fcl.ui.bridge

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tungsten.fcl.FCLApplication
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.theme.FCLTheme
import com.tungsten.fcl.ui.theme.FCLThemeMode
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.component.theme.ThemeEngine
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
     * 主题接入（小步骤 3.1，落实 FCLTheme.kt 的 ThemeEngine 对接 TODO）：
     * - Light/Dark/FollowSystem 读 SharedPreferences "launcher" 的 themeMode
     *   （与 FCLActivity.applySavedNightMode 同一数据源），并监听变更即时重组；
     * - 主色/内容色经 fakefx 属性桥（[collectAsState]）观察 ThemeEngine 当前主题，
     *   取色器修改主题色后 Compose 侧实时联动。
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
            // 主题模式：与遗留 FCLActivity.applySavedNightMode 同源（SP "launcher"/"themeMode"）
            val launcherPrefs = remember {
                context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
            }
            val themeMode = remember { mutableIntStateOf(launcherPrefs.getInt("themeMode", 0)) }
            DisposableEffect(launcherPrefs) {
                val listener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
                    if (key == "themeMode") themeMode.intValue = sp.getInt("themeMode", 0)
                }
                launcherPrefs.registerOnSharedPreferenceChangeListener(listener)
                onDispose { launcherPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
            }
            val mode = when (themeMode.intValue) {
                1 -> FCLThemeMode.Light
                2 -> FCLThemeMode.Dark
                else -> FCLThemeMode.FollowSystem
            }

            // ThemeEngine 主题色：fakefx 属性 → Compose State（引擎未初始化时回落默认 token）
            val engineTheme = remember { ThemeEngine.getInstance().theme }
            val primary = engineTheme?.colorProperty()?.collectAsState()?.value?.toInt()
                ?.let { Color(it) } ?: FCLThemeTokens.BrandPrimary
            val color2 = engineTheme?.color2Property()?.collectAsState()?.value?.toInt()
                ?.let { Color(it) } ?: FCLThemeTokens.Color2LightDefault
            val color2Dark = engineTheme?.color2DarkProperty()?.collectAsState()?.value?.toInt()
                ?.let { Color(it) } ?: FCLThemeTokens.Color2DarkDefault

            FCLTheme(mode = mode, primary = primary, color2 = color2, color2Dark = color2Dark, content = content)
        }
    }

    /**
     * 方向二的 Compose 宿主：订阅 [alertDialogRequest] 并以 Miuix WindowDialog 渲染。
     *
     * 每个 Compose 根（setContent / createComposeView 的内容树）只安装一次，放在页面
     * 内容的最外层即可。选用 window/WindowDialog 而非 overlay/OverlayDialog：
     * WindowDialog 走平台 Dialog window，不依赖 Miuix Scaffold 祖先（OverlayDialog 依赖
     * LocalDialogStates，无 Scaffold 时无法渲染），且独立 window 的形态与遗留 FCLDialog
     * 语义一致（interaction-map G7）。
     *
     * 点遮罩/返回键关闭视为"取消"（onResult 回调 false）。
     */
    @Composable
    fun LegacyDialogHost() {
        val request by alertDialogRequest.collectAsStateWithLifecycle()
        val current = request ?: return
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
}
