package com.tungsten.fcl.ui.manage.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 迁移期 Compose 版本设置页容器（小步骤 3.3b）：VersionSettingPage.kt 的 Compose 化承接，
 * 沿用 3.1 [com.tungsten.fcl.ui.setting.compose.ComposeSettingPage] / 3.3a
 * [ComposeManagePage] 范式的 FCLCommonPage 壳，同时服务两种形态：
 * - ManageUI Tab0（PAGE_ID_MANAGE_SETTING，globalSetting=false，单版本设置）；
 * - SettingUI Tab0（PAGE_ID_SETTING_GAME，globalSetting=true，全局设置）。
 *
 * 设计要点：
 * - 页面仍走既有 PageManager 页面栈，ManageUI/SettingUI 的 Tab 切换与返回链零改动；
 * - [loadVersion] 可能在 ComposeView 安装前被 PageManager 调用（ManagePageManager.init
 *   后即 loadVersion），因此经 [loadRequests] StateFlow 中转：StateFlow 保留最新值，
 *   Compose 侧组合建立后补放（对齐 VersionSettingPage 的整页重绑语义）；
 * - [onResume] 经 [memoryTicks] 通知 VM 刷新已用内存（对齐 VersionSettingPage.onResume）；
 * - 开关 [com.tungsten.fcl.ui.version.compose.ComposeVersionPages.USE_COMPOSE_VERSION_SETTING]
 *   为 false 时整体回滚旧 View 页面（VersionSettingPage + page_version_setting.xml 保留未删）。
 */
class ComposeVersionSettingPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val globalSetting: Boolean,
) : FCLCommonPage(context, id, parent, R.layout.page_compose_container), VersionLoadable {

    private val loadRequests = MutableStateFlow<VersionSettingLoadRequest?>(null)
    private val memoryTicks = MutableStateFlow(0)

    private var composeInstalled = false

    override fun onCreate() {
        // 父类构造期间回调：仅完成 contentView 挂载，ComposeView 推迟到首次 onStart 安装。
        super.onCreate()
    }

    override fun onStart() {
        installComposeViewIfNeeded()
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        // 对齐 VersionSettingPage.onResume(:258-261)：回前台刷新已用内存
        memoryTicks.value += 1
    }

    override fun loadVersion(profile: Profile, version: String?) {
        loadRequests.value = VersionSettingLoadRequest(profile, version)
    }

    private fun installComposeViewIfNeeded() {
        if (composeInstalled) return
        composeInstalled = true
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            VersionSettingScreen(
                globalSetting = globalSetting,
                // 对齐遗留 `id == ManagePageManager.PAGE_ID_MANAGE_SETTING`：
                // 仅管理页注册游戏目录隔离变更联动（刷新 Mod/World 页）
                notifyRunDirectoryChange = id == ManagePageManager.PAGE_ID_MANAGE_SETTING,
                loadRequests = loadRequests,
                memoryTicks = memoryTicks,
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
