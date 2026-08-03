package com.tungsten.fcl.ui.download.compose

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.ui.manage.ManageUI
import com.tungsten.fcl.ui.version.Versions
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseAddon
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.ui.FCLTempPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

/**
 * 下载域 Compose 壳层（小步骤 3.4）：沿用 3.3 [com.tungsten.fcl.ui.version.compose.ComposeVersionListPage] 范式，
 * 只承载 ComposeView 的 FCLCommonPage / FCLTempPage 壳。
 *
 * 设计要点：
 * - 页面仍走既有 DownloadPageManager 页面栈/临时页栈（返回链/生命周期透传零改动）；
 * - contentView 是 page_compose_container.xml 的 FrameLayout，ComposeView 经
 *   [LegacyBridge.createComposeView] 创建（自动套 FCLTheme + ViewTree 生命周期销毁策略）；
 * - 基类构造函数内部回调 onCreate()（此时子类构造参数尚未赋值），
 *   因此 ComposeView 延迟到首次 onStart() 才创建（construction-order 安全）。
 */

/** 下载 Tab 主页壳（tab = null 为游戏版本 Tab，其余为远程资源搜索 Tab）。 */
class ComposeDownloadPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val tab: DownloadTab?,
) : FCLCommonPage(context, id, parent, R.layout.page_compose_container),
    ManageUI.VersionLoadable {

    private var composeInstalled = false

    override fun onCreate() {
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
            when (val currentTab = tab) {
                null -> VersionInstallScreen(onOpenInstallInfo = { gameVersion ->
                    DownloadPageManager.instance?.showTempPage(
                        ComposeVersionInstallInfoPage(
                            context,
                            PageManager.PAGE_ID_TEMP,
                            parent,
                            gameVersion,
                        ),
                    )
                })

                else -> RemoteModSearchScreen(
                    tab = currentTab,
                    onOpenModInfo = { mod, repository, isModrinth ->
                        DownloadPageManager.instance?.showTempPage(
                            ComposeRemoteModInfoPage(
                                context,
                                PageManager.PAGE_ID_TEMP,
                                parent,
                                currentTab,
                                repository,
                                mod,
                                isModrinth,
                            ),
                        )
                    },
                    onImportModpack = { Versions.importModpack(context, parent) },
                )
            }
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    /**
     * Profile/版本切换广播（对齐 DownloadPage.loadVersion）。
     * Compose 侧在动作发生时实时读取 Profiles 选中态（DownloadUI 始终传 version=null，
     * 遗留下载回调同样回退 profile.getSelectedVersion()），语义等价，无需落地存储。
     */
    override fun loadVersion(profile: Profile?, version: String?) {
    }

    /**
     * 从管理域 Mod 列表跳转到指定 Mod 的详情页（对齐遗留 DownloadPage.jumpToModPage）：
     * 按数据来源（CurseForge/Modrinth）选择对应仓库，直接开详情临时页。
     */
    fun jumpToModPage(mod: RemoteMod) {
        if (tab != DownloadTab.MOD) return
        val isModrinth = mod.data !is CurseAddon
        val repository =
            if (isModrinth) ModrinthRemoteModRepository.MODS else CurseForgeRemoteModRepository.MODS
        DownloadPageManager.instance?.showTempPage(
            ComposeRemoteModInfoPage(
                context,
                PageManager.PAGE_ID_TEMP,
                parent,
                DownloadTab.MOD,
                repository,
                mod,
                isModrinth,
            ),
        )
    }

    override fun refresh(vararg param: Any?): Task<*>? = null
}

/** 临时页壳基类：首次 onStart 安装 ComposeView。 */
abstract class ComposeTempPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
) : FCLTempPage(context, id, parent, R.layout.page_compose_container) {

    private var composeInstalled = false

    @Composable
    abstract fun Content()

    override fun onStart() {
        installComposeViewIfNeeded()
        super.onStart()
    }

    private fun installComposeViewIfNeeded() {
        if (composeInstalled) return
        composeInstalled = true
        val container = findViewById<FrameLayout>(R.id.compose_container)
        val composeView = LegacyBridge.createComposeView(context) {
            Content()
            LegacyBridge.LegacyDialogHost()
        }
        container.addView(
            composeView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    override fun onRestart() {
    }
}

/** 远程资源详情页（第 1 层临时页）。 */
class ComposeRemoteModInfoPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val tab: DownloadTab,
    private val repository: RemoteModRepository,
    private val addon: RemoteMod,
    private val isModrinth: Boolean,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val holder = remember(tab, repository, addon) {
            RemoteModInfoStateHolder(context, tab, repository, addon, isModrinth, scope)
        }
        RemoteModInfoScreen(
            holder = holder,
            onOpenVersionPage = { versions ->
                DownloadPageManager.instance?.showTempPage(
                    ComposeRemoteModVersionPage(
                        context,
                        PageManager.PAGE_ID_TEMP,
                        parent,
                        tab,
                        repository,
                        Profile.ProfileVersion(Profiles.getSelectedProfile(), null),
                        isModrinth,
                        versions,
                    ),
                )
            },
        )
    }
}

/** 远程资源版本文件列表页（第 2 层临时页）。 */
class ComposeRemoteModVersionPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val tab: DownloadTab,
    private val repository: RemoteModRepository,
    private val version: Profile.ProfileVersion,
    private val isModrinth: Boolean,
    private val versions: List<RemoteMod.Version>,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        RemoteModVersionScreen(
            versions = versions,
            onSelect = { selected ->
                if (tab == DownloadTab.MOD) {
                    // 对齐 RemoteModVersionPage :51-54：仅 Mod 页进入依赖下载页
                    DownloadPageManager.instance?.showTempPage(
                        ComposeRemoteModDownloadPage(
                            context,
                            PageManager.PAGE_ID_TEMP,
                            parent,
                            tab,
                            repository,
                            version,
                            isModrinth,
                            selected,
                        ),
                    )
                } else {
                    RemoteModActions.downloadOrSaveAs(context, parent, tab, version, selected)
                }
            },
        )
    }
}

/** 远程资源下载确认页（第 3 层临时页，仅 Mod Tab）。 */
class ComposeRemoteModDownloadPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val tab: DownloadTab,
    private val repository: RemoteModRepository,
    private val version: Profile.ProfileVersion,
    private val isModrinth: Boolean,
    private val modVersion: RemoteMod.Version,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val holder = remember(modVersion) { RemoteModDownloadStateHolder(context, modVersion, scope) }
        RemoteModDownloadScreen(
            holder = holder,
            repository = repository,
            onDownload = {
                // 对齐 lastPage.download(modVersion)：callback 非空走下载（Mod Tab 恒非空）
                RemoteModActions.downloadOrSaveAs(context, parent, tab, version, modVersion)
            },
            onSaveAs = { RemoteModActions.saveAs(context, modVersion) },
            onOpenDependency = { mod ->
                // 对齐 :132-135：依赖项点击再开一层详情页（同一页面配置）
                DownloadPageManager.instance?.showTempPage(
                    ComposeRemoteModInfoPage(
                        context,
                        PageManager.PAGE_ID_TEMP,
                        parent,
                        tab,
                        repository,
                        mod,
                        isModrinth,
                    ),
                )
            },
        )
    }
}

/** 安装信息页（游戏安装向导第 2 步临时页）。 */
class ComposeVersionInstallInfoPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val gameVersion: String,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember(gameVersion) { VersionInstallInfoStateHolder(context, gameVersion) }
        VersionInstallInfoScreen(
            holder = holder,
            onOpenInstallerList = { libraryId ->
                DownloadPageManager.instance?.showTempPage(
                    ComposeInstallerListPage(
                        context,
                        PageManager.PAGE_ID_TEMP,
                        parent,
                        gameVersion,
                        libraryId,
                        onSelect = { remoteVersion ->
                            // 对齐 :127-130：回填已选版本并关闭选择页
                            holder.onLoaderSelected(libraryId, remoteVersion)
                            DownloadPageManager.instance?.dismissCurrentTempPage()
                        },
                    ),
                )
            },
            onInstallSuccess = {
                DownloadPageManager.instance?.dismissCurrentTempPage()
            },
        )
    }
}

/** 加载器版本选择页（游戏安装向导第 3 步临时页）。 */
class ComposeInstallerListPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val gameVersion: String,
    private val libraryId: String,
    private val onSelect: (RemoteVersion) -> Unit,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember(gameVersion, libraryId) { InstallerListStateHolder(context, gameVersion, libraryId) }
        DisposableEffect(Unit) {
            onDispose { holder.active = false }
        }
        InstallerListScreen(holder = holder, onSelect = onSelect)
    }
}
