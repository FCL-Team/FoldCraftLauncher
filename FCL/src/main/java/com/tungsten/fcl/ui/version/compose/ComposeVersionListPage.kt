package com.tungsten.fcl.ui.version.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

/**
 * 迁移期 Compose 版本列表页容器（小步骤 3.3）：沿用 3.1 [com.tungsten.fcl.ui.setting.compose.ComposeSettingPage] 范式，
 * 只承载 ComposeView 的 FCLCommonPage 壳。
 *
 * 设计要点：
 * - 页面仍走既有 VersionPageManager 页面栈（构造即把 contentView 挂到父容器并 GONE，
 *   onStart/onStop 切显隐），VersionUI 的返回链/生命周期透传零改动；
 * - contentView 是 page_compose_container.xml 的 FrameLayout，ComposeView 经
 *   [LegacyBridge.createComposeView] 创建（自动套 FCLTheme + ViewTree 生命周期销毁策略），
 *   并安装 [LegacyBridge.LegacyDialogHost] 承接遗留弹窗请求；
 * - FCLCommonPage 构造函数内部会回调 onCreate()（此时子类构造参数尚未赋值），
 *   因此 ComposeView 延迟到首次 onStart() 才创建（construction-order 安全）。
 */
class ComposeVersionListPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
) : FCLCommonPage(context, id, parent, R.layout.page_compose_container) {

    private var composeInstalled = false

    override fun onCreate() {
        // 父类构造期间回调：仅完成 contentView 挂载（父类已实现），
        // ComposeView 推迟到首次 onStart 安装。
        super.onCreate()
    }

    override fun onStart() {
        installComposeViewIfNeeded()
        super.onStart()
    }

    private fun installComposeViewIfNeeded() {
        if (composeInstalled) return
        composeInstalled = true
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            VersionListScreen(
                onEvent = { event -> VersionListScreenHost.handle(context, event) },
            )
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun refresh(vararg param: Any?): Task<*>? = null
}
