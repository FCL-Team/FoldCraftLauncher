package com.tungsten.fcl.ui.manage.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.compose.ShakeState
import com.tungsten.fcl.ui.compose.rememberShakeState
import com.tungsten.fcl.ui.compose.shake
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.flow.StateFlow
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 版本管理页 Compose 界面（小步骤 3.3）：page_manage_version.xml + ManagePage.kt +
 * ManageItemAdapter 的 Miuix 重构。
 *
 * 布局对齐遗留：左右双列卡片（各 50% 宽），左列 9 项（日志/目录浏览），
 * 右列 7 项（更新/重命名/复制/导出/清理）；菜单项图标、文案、顺序与
 * ManagePage.kt :71-147 完全一致。
 * 菜单项行对齐 item_manage.xml：左图标 + 标题 + 右箭头。
 *
 * 状态只有一个 Boolean（当前版本是否为可更新的整合包，对齐
 * ManagePage.currentVersionUpgradable），业务动作全部委托遗留 Versions 静态方法与
 * 文件浏览器，因此不引入 ViewModel：状态流与动作实现由 [ComposeManagePage] 直接持有并传入。
 */
@Composable
fun ManageScreen(
    upgradableFlow: StateFlow<Boolean>,
    actions: ManageActions,
) {
    val upgradable by upgradableFlow.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // ---------- 左列（对齐 ManagePage.kt :71-104 的 9 项） ----------
        // 容器底色对齐遗留 registerEvent 的 ltColor 染色（= primaryContainer，随主色联动）
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(leftEntries) { entry ->
                    ManageEntryRow(
                        entry = entry,
                        onClick = { entry.invoke(actions) },
                    )
                }
            }
        }

        Spacer(Modifier.width(10.dp))

        // ---------- 右列（对齐 ManagePage.kt :106-147 的 7 项） ----------
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rightEntries) { entry ->
                    if (entry.requiresUpgradable) {
                        // 版本更新：不可更新时抖动拒绝（对齐 ManagePage.kt :109-115，
                        // 0→50→-50→0 / 500ms / Overshoot）
                        val shakeState = rememberShakeState()
                        ManageEntryRow(
                            entry = entry,
                            shakeState = shakeState,
                            onClick = {
                                if (!upgradable) {
                                    shakeState.shake()
                                } else {
                                    entry.invoke(actions)
                                }
                            },
                        )
                    } else {
                        ManageEntryRow(
                            entry = entry,
                            onClick = { entry.invoke(actions) },
                        )
                    }
                }
            }
        }
    }
}

/** 管理菜单项点击动作集：由 ComposeManagePage 实现（需要 Context/Activity/父容器）。 */
interface ManageActions {
    fun onUploadLog()
    fun onBrowse(path: String)
    fun onUpdateGame()
    fun onRename()
    fun onDuplicate()
    fun onExport()
    fun onRedownloadAssets()
    fun onClearLibraries()
    fun onClearJunkFiles()
}

/** 菜单项模型（对齐 ManageItem：图标 + 文案 + 动作）。 */
private data class ManageEntry(
    val icon: Int,
    val text: Int,
    val requiresUpgradable: Boolean = false,
    val invoke: ManageActions.() -> Unit,
)

/** 左列 9 项（顺序与 ManagePage.kt :73-103 一致）。 */
private val leftEntries = listOf(
    ManageEntry(R.drawable.ic_baseline_cloud_upload_24, R.string.upload_log) { onUploadLog() },
    ManageEntry(R.drawable.ic_baseline_script_24, R.string.folder_fcl_log) { onBrowse(FCLPath.LOG_DIR) },
    ManageEntry(R.drawable.ic_baseline_videogame_asset_24, R.string.folder_game) { onBrowse("") },
    ManageEntry(R.drawable.ic_outline_extension_24, R.string.folder_mod) { onBrowse("mods") },
    ManageEntry(R.drawable.ic_baseline_settings_24, R.string.folder_config) { onBrowse("config") },
    ManageEntry(R.drawable.ic_baseline_texture_24, R.string.folder_resourcepacks) { onBrowse("resourcepacks") },
    ManageEntry(R.drawable.ic_baseline_application_24, R.string.folder_shaderpacks) { onBrowse("shaderpacks") },
    ManageEntry(R.drawable.ic_baseline_screenshot_24, R.string.folder_screenshots) { onBrowse("screenshots") },
    ManageEntry(R.drawable.ic_baseline_earth_24, R.string.folder_saves) { onBrowse("saves") },
)

/** 右列 7 项（顺序与 ManagePage.kt :109-146 一致）。 */
private val rightEntries = listOf(
    ManageEntry(R.drawable.ic_baseline_update_24, R.string.version_update, requiresUpgradable = true) { onUpdateGame() },
    ManageEntry(R.drawable.ic_baseline_edit_24, R.string.version_manage_rename) { onRename() },
    ManageEntry(R.drawable.ic_baseline_content_copy_24, R.string.version_manage_duplicate) { onDuplicate() },
    ManageEntry(R.drawable.ic_baseline_output_24, R.string.modpack_export) { onExport() },
    ManageEntry(R.drawable.ic_baseline_list_24, R.string.version_manage_redownload_assets_index) { onRedownloadAssets() },
    ManageEntry(R.drawable.ic_baseline_delete_24, R.string.version_manage_remove_libraries) { onClearLibraries() },
    ManageEntry(R.drawable.ic_baseline_delete_24, R.string.version_manage_clean) { onClearJunkFiles() },
)

/** 菜单项行（对齐 item_manage.xml：左图标 + 标题 + 右箭头，上下 15dp 等效内边距）。
 *  染色对齐 item_manage.xml 的 auto_text_tint="true"：文字与左右 drawable 均染
 *  ThemeEngine autoTint（按主色亮度取黑/白）= colorScheme.onPrimary。 */
@Composable
private fun ManageEntryRow(
    entry: ManageEntry,
    onClick: () -> Unit,
    shakeState: ShakeState? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (shakeState != null) Modifier.shake(shakeState, durationMillis = 500, offsets = floatArrayOf(0f, 50f, -50f, 0f)) else Modifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(entry.icon),
            contentDescription = null,
            tint = MiuixTheme.colorScheme.onPrimary,
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(entry.text),
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Icon(
            painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
            contentDescription = null,
            tint = MiuixTheme.colorScheme.onPrimary,
        )
    }
}
