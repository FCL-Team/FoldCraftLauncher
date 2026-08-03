package com.tungsten.fcl.ui.download.modpack.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fclcore.mod.server.ServerModpackManifest
import com.tungsten.fcllibrary.component.view.FCLUILayout
import java.io.File

/**
 * 整合包安装向导 Compose 壳层（对齐 ComposeDownloadShells 范式）：
 * 页面仍走既有 DownloadPageManager/ManagePageManager 临时页栈，
 * 仅内容区换 ComposeView。旧类（ModpackSelectionPage/LocalModpackPage/
 * RemoteModpackPage）保留不删，回滚时把调用点指回旧类即可。
 */

/** 入口页（本地/远程两入口），对齐 ModpackSelectionPage。 */
class ComposeModpackSelectionPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val profile: Profile,
    private val updateVersion: String?,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember { ModpackSelectionStateHolder(context, activity, parent, profile, updateVersion) }
        ModpackSelectionScreen(holder)
    }
}

/** 本地整合包信息确认页，对齐 LocalModpackPage。 */
class ComposeLocalModpackPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val profile: Profile,
    private val updateVersion: String?,
    private val modpackFile: File,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember { LocalModpackStateHolder(context, profile, updateVersion, modpackFile) }
        // 对齐旧页 onStart 时机：进入页面即开始解析清单
        LaunchedEffect(Unit) { holder.load() }
        ModpackInfoScreen(holder)
    }
}

/** 远程整合包信息确认页，对齐 RemoteModpackPage。 */
class ComposeRemoteModpackPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val profile: Profile,
    private val updateVersion: String?,
    private val serverManifest: ServerModpackManifest,
) : ComposeTempPage(context, id, parent) {

    @Composable
    override fun Content() {
        val holder = remember { RemoteModpackStateHolder(context, profile, updateVersion, serverManifest) }
        LaunchedEffect(Unit) { holder.load() }
        ModpackInfoScreen(holder)
    }
}
