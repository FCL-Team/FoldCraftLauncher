package com.tungsten.fcl.ui.download.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcl.ui.compose.fclCheckboxColors
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.logging.Level
import java.util.stream.Collectors

/**
 * 加载器版本选择页（安装向导第 3 步，对齐 InstallerListPage + page_install_version.xml）。
 *
 * 行为对齐（interaction-map §5.6）：
 * - 3 个 CheckBox（Release/快照/旧版）过滤；无类型分级的库隐藏过滤栏（:56）；
 * - 刷新/失败重刷 + 三态进度；空列表 Toast（:117-118）；过滤结果为空自动全勾（:122-125）；
 * - 版本项点击 → [onSelect] 回调并关闭本页（关闭由宿主完成）；
 * - 愚人节与搜索框在遗留布局中 INVISIBLE（:76-77），Compose 侧不渲染。
 */
class InstallerListStateHolder(
    private val context: Context,
    private val gameVersion: String,
    libraryId: String,
) {
    private val versionList = DownloadProviders.getDownloadProvider().getVersionListById(libraryId)

    /** 过滤栏可见性（对齐 :56）。 */
    val hasType: Boolean = versionList.hasType()

    var loading by mutableStateOf(true)
    var failed by mutableStateOf(false)
    var checkRelease by mutableStateOf(true)
    var checkSnapshot by mutableStateOf(false)
    var checkOld by mutableStateOf(false)
    var items by mutableStateOf<List<RemoteVersion>>(emptyList())

    /** 页面销毁后忽略异步回调（对齐遗留 isShowing 守卫 :112）。 */
    var active = true

    init {
        refreshList()
    }

    /** 对齐 refreshList :105-149。 */
    fun refreshList() {
        loading = true
        failed = false
        versionList.refreshAsync(gameVersion).whenComplete { _, exception ->
            if (!active) return@whenComplete
            Schedulers.androidUIThread().execute {
                if (!active) return@execute
                if (exception == null) {
                    val loaded = loadVersions()
                    if (versionList.getVersions(gameVersion).isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.download_failed_empty),
                            Toast.LENGTH_SHORT,
                        ).show()
                        failed = true
                    } else {
                        if (loaded.isEmpty()) {
                            // 对齐 :122-125：过滤结果为空自动全勾（级联重建列表）
                            checkRelease = true
                            checkSnapshot = true
                            checkOld = true
                            items = loadVersions()
                        } else {
                            items = loaded
                        }
                    }
                    loading = false
                } else {
                    Logging.LOG.log(Level.WARNING, "Failed to fetch versions list", exception)
                    failed = true
                    loading = false
                }
                System.gc()
            }
        }
    }

    /** 勾选变更 → 重建展示列表（对齐 onCheckedChanged :171-174）。 */
    fun refreshDisplayVersions() {
        items = loadVersions()
    }

    /** 对齐 loadVersions :82-97。 */
    private fun loadVersions(): List<RemoteVersion> {
        return versionList.getVersions(gameVersion).stream()
            .filter {
                when (it.versionType) {
                    RemoteVersion.Type.RELEASE -> checkRelease
                    RemoteVersion.Type.SNAPSHOT -> checkSnapshot
                    RemoteVersion.Type.OLD -> checkOld
                    else -> true
                }
            }
            .sorted()
            .collect(Collectors.toList())
    }
}

@Composable
fun InstallerListScreen(
    holder: InstallerListStateHolder,
    onSelect: (RemoteVersion) -> Unit,
) {
    val context = LocalContext.current
    var saveDialogVersion by remember {
        mutableStateOf<RemoteVersion?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        if (holder.hasType) {
            FCLCard(
                // 对齐 page_install_version.xml bar 的 bg_container_white +
                // auto_linear_background_tint（ltColor 染色 = primaryContainer）
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    InstallerTypeCheckbox(
                        label = stringResource(R.string.version_game_release),
                        checked = holder.checkRelease,
                        onCheckedChange = {
                            holder.checkRelease = it
                            holder.refreshDisplayVersions()
                        },
                    )
                    InstallerTypeCheckbox(
                        label = stringResource(R.string.version_game_snapshot),
                        checked = holder.checkSnapshot,
                        onCheckedChange = {
                            holder.checkSnapshot = it
                            holder.refreshDisplayVersions()
                        },
                    )
                    InstallerTypeCheckbox(
                        label = stringResource(R.string.version_game_old),
                        checked = holder.checkOld,
                        onCheckedChange = {
                            holder.checkOld = it
                            holder.refreshDisplayVersions()
                        },
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                holder.loading -> InfiniteProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MiuixTheme.colorScheme.primary,
                )

                holder.failed -> IconButton(
                    onClick = holder::refreshList,
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    // 对齐 failed_refresh（无 auto_tint）：drawable 自带静态灰
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_refresh_24),
                        contentDescription = null,
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(holder.items, key = { it.selfVersion }) { version ->
                        // 入场动画对齐 RemoteVersionListAdapter:108（animationSpeed×30）
                        Row(
                            modifier = fclItemEntryModifier(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RemoteVersionRow(
                                version = version,
                                onClick = { onSelect(version) },
                                modifier = Modifier.weight(1f),
                            )
                            if (remoteVersionShowSave(version)) {
                                // 对齐 item_remote_version.xml save FCLImageButton auto_tint（= onPrimary）
                                IconButton(onClick = { saveDialogVersion = version }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_baseline_jump_24),
                                        contentDescription = null,
                                        tint = MiuixTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    saveDialogVersion?.let { version ->
        RemoteVersionSaveDialog(
            version = version,
            onDismiss = { saveDialogVersion = null },
        )
    }
}

@Composable
private fun InstallerTypeCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp),
    ) {
        Checkbox(
            state = if (checked) ToggleableState.On else ToggleableState.Off,
            onClick = { onCheckedChange(!checked) },
            colors = fclCheckboxColors(),
        )
        // 对齐 FCLCheckBox auto_hint_tint（文本 autoTint = onPrimary，位于 ltColor bar 上）
        Text(
            text = label,
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onPrimary,
        )
    }
}
