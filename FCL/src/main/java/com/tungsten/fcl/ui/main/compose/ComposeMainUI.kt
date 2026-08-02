package com.tungsten.fcl.ui.main.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.skin.SkinViewer
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 主页 Compose 一级界面壳（小步骤 3.6）：替换 [com.tungsten.fcl.ui.main.MainUI]
 * （ui_main.xml：公告栏 + 账户皮肤 3D 展示的 Miuix 重构）。
 *
 * 设计要点（对齐 3.5 ComposeAccountUI 模式）：
 * - 主页是一级界面（FCLCommonUI，直接挂在 FCLUILayout 容器，不走 PageManager），
 *   由 UIManager.mainUI 按 [USE_COMPOSE_MAIN_UI] 开关二选一实例化，改 false 整体回滚
 *   到旧 MainUI（ui_main.xml）；类型放宽为 FCLCommonUI，既有反向调用点
 *   （switchUI / currentUI 比较）签名不变；
 * - contentView 复用迁移期通用容器 page_compose_container.xml，ComposeView 经
 *   [LegacyBridge.createComposeView] 创建（自动套 FCLTheme + ViewTree 生命周期销毁）；
 * - GL 皮肤预览生命周期：SkinViewer（GLSurfaceView）经 [activeSkinViewer] 由
 *   onStart/onStop/onPause/onResume 转发暂停/恢复（对齐旧 MainUI 各生命周期方法），
 *   onStart 时经 [textureRefreshHook] 重新上传纹理（对齐 MainUI.onStart 的
 *   renderer.updateTexture 调用）；
 * - 换肤契约承接：[refreshSkin] 承接 AccountListItem.refreshSkinBinding 的反向调用，
 *   通过 [skinRefreshTick] 节拍通知 Compose 侧重建 textureBinding，语义与旧
 *   MainUI.refreshSkin（unbind/rebind 强制刷新）一致。
 */
class ComposeMainUI(
    context: Context,
    parent: FCLUILayout,
) : FCLCommonUI(context, parent, R.layout.page_compose_container) {

    companion object {
        /**
         * 阶段三 3.6 主页迁移开关（对齐 ComposeAccountUI.USE_COMPOSE_ACCOUNT_UI 模式）：
         * true = UIManager.mainUI 实例化 Compose 版，且 MainActivity 右侧栏挂 Compose 层；
         * 改 false 整体回滚到旧 MainUI（ui_main.xml）+ 旧右侧栏 View。
         */
        const val USE_COMPOSE_MAIN_UI = true

        /** 页面内嵌 GL 皮肤预览实例（MainScreen AndroidView 维护），供生命周期转发。 */
        @Volatile
        internal var activeSkinViewer: SkinViewer? = null

        /** 纹理重传回调（MainScreen DisposableEffect 维护），onStart 时调用。 */
        @Volatile
        internal var textureRefreshHook: (() -> Unit)? = null

        /** 皮肤刷新节拍：refreshSkin 反向调用 → +1 → Compose 侧重建 textureBinding。 */
        internal val skinRefreshTick = MutableStateFlow(0)
    }

    override fun onCreate() {
        super.onCreate()
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            MainScreen()
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    /** 对齐 MainUI 各生命周期：close_skin_model 开启时不渲染皮肤模型（Theme.java:212）。 */
    private val skinModelEnabled: Boolean
        get() = !ThemeEngine.getInstance().theme.isCloseSkinModel

    override fun onStart() {
        super.onStart()
        if (skinModelEnabled) {
            activeSkinViewer?.onResume()
            textureRefreshHook?.invoke()
        } else {
            activeSkinViewer?.onPause()
        }
    }

    override fun onStop() {
        super.onStop()
        activeSkinViewer?.onPause()
    }

    override fun onPause() {
        super.onPause()
        activeSkinViewer?.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (isShowing && skinModelEnabled) {
            activeSkinViewer?.onResume()
        }
    }

    /**
     * 承接旧 MainUI.refreshSkin 契约（AccountListItem.java 换肤后反向调用）：
     * 仅当刷新的账户仍是当前选中账户时推进节拍，Compose 侧重建纹理绑定。
     */
    fun refreshSkin(account: Account) {
        Schedulers.androidUIThread().execute {
            if (Accounts.selectedAccountProperty().get() === account) {
                skinRefreshTick.value = skinRefreshTick.value + 1
            }
        }
    }

    override fun refresh(vararg param: Any?): Task<*> = Task.runAsync { }
}
