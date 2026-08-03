package com.tungsten.fcl.ui.account.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonUI
import com.tungsten.fcllibrary.component.view.FCLUILayout

/**
 * 账户域 Compose 一级界面壳（小步骤 3.5）：替换 [com.tungsten.fcl.ui.account.AccountUI]
 * （ui_account.xml + AccountListAdapter + ServerListAdapter 的 Miuix 重构）。
 *
 * 设计要点：
 * - 账户 UI 是一级界面（FCLCommonUI，直接挂在 FCLUILayout 容器，不走 PageManager），
 *   由 UIManager.accountUI 固定实例化（批 3 起迁移开关已全部固化删除，旧 AccountUI
 *   回滚分支已移除）；
 * - contentView 复用迁移期通用容器 page_compose_container.xml，ComposeView 经
 *   [LegacyBridge.createComposeView] 创建（自动套 FCLTheme + ViewTree 生命周期销毁），
 *   并安装 [LegacyBridge.LegacyDialogHost] 承接遗留弹窗请求；
 * - FCLCommonUI 经 AsyncLayoutInflater 异步 inflate 完成后才回调 onCreate（区别于
 *   FCLCommonPage 构造期同步回调），此处安装 ComposeView 无 construction-order 问题；
 * - 刷新契约承接：遗留反向调用点（CreateAccountDialog.java:198、
 *   MiuixCreateAccountDialog.kt:421、旧 AccountListAdapter）均调
 *   `UIManager.accountUI.refresh().start()`，本类经 [refreshHook] 转发给 Compose 侧
 *   ViewModel 的列表重建，契约语义与旧 AccountUI.refresh 一致。
 *
 * PR #1714 review：页内 3D 皮肤预览已移除（显示空间不足），原 SkinViewer 的 GL
 * 生命周期转发（activeSkinViewer + onStart/onStop/onPause/onResume）一并删除。
 */
class ComposeAccountUI(
    context: Context,
    parent: FCLUILayout,
) : FCLCommonUI(context, parent, R.layout.page_compose_container) {

    companion object {
        // 批 3：迁移开关（含已被批 2 删除的 USE_COMPOSE_VERSION_PAGES /
        // USE_COMPOSE_DOWNLOAD_PAGES）已全部固化删除，Compose 版为唯一实现。

        /** Compose 侧注册的刷新回调（AccountScreen DisposableEffect 维护），承接 refresh() 契约。 */
        @Volatile
        internal var refreshHook: (() -> Unit)? = null
    }

    override fun onCreate() {
        super.onCreate()
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            AccountScreen(
                onEvent = { event -> AccountScreenHost.handle(context, event) },
            )
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun onStart() {
        super.onStart()
        // 对齐旧 AccountUI.onStart：每次切入账户页都触发一次列表刷新
        refreshHook?.invoke()
    }

    override fun refresh(vararg param: Any?): Task<*> {
        refreshHook?.invoke()
        return Task.runAsync { }
    }
}
