package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.logging.Level
import java.util.stream.Collectors

/**
 * 游戏版本安装列表（下载 Tab 0，对齐 VersionInstallPage + page_install_version.xml）。
 *
 * 行为对齐（interaction-map §5.6）：
 * - 4 个 CheckBox（Release/快照/旧版/愚人节）过滤，每次变更重建展示列表（:107）；
 * - 搜索框实时过滤（contains，:103）；刷新/失败重刷 + 进度三态（:113-150）；
 * - 刷新结果为空时自动勾选 Release/快照/旧版（:125-128）；
 * - 版本项点击 → [onOpenInstallInfo]（打开安装信息临时页）。
 */
class VersionInstallStateHolder(
    private val context: Context,
) {
    var loading by mutableStateOf(true)
    var failed by mutableStateOf(false)
    var checkRelease by mutableStateOf(true)
    var checkSnapshot by mutableStateOf(false)
    var checkOld by mutableStateOf(false)
    var checkAprilFools by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var items by mutableStateOf<List<RemoteVersion>>(emptyList())

    private val versionList
        get() = DownloadProviders.getDownloadProvider().getVersionListById("game")

    init {
        refreshList()
    }

    /** 对齐 VersionInstallPage.refreshList（:113-150）。 */
    fun refreshList() {
        loading = true
        failed = false
        searchText = ""
        versionList.refreshAsync("").whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception == null) {
                    val loaded = loadVersions()
                    if (loaded.isEmpty()) {
                        // 对齐 :125-128：结果为空自动全勾（级联重建列表）
                        checkRelease = true
                        checkSnapshot = true
                        checkOld = true
                        items = loadVersions()
                    } else {
                        items = loaded
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

    /** 勾选变更 / 搜索变更 → 重建展示列表（对齐 onCheckedChanged :167-171、:78）。 */
    fun refreshDisplayVersions() {
        items = loadVersions()
    }

    fun onSearchChange(text: String) {
        searchText = text
        refreshDisplayVersions()
    }

    /** 对齐 VersionInstallPage.loadVersions（:85-105）。 */
    private fun loadVersions(): List<RemoteVersion> {
        return versionList.getVersions("").stream()
            .filter {
                when (it.versionType) {
                    RemoteVersion.Type.RELEASE -> checkRelease
                    RemoteVersion.Type.PENDING,
                    RemoteVersion.Type.UNOBFUSCATED,
                    RemoteVersion.Type.SNAPSHOT,
                        ->
                        when {
                            checkSnapshot -> true
                            checkAprilFools ->
                                GameVersionNumber.asGameVersion(it.gameVersion).isAprilFools

                            else -> false
                        }

                    RemoteVersion.Type.OLD -> when {
                        checkOld -> true
                        checkAprilFools ->
                            GameVersionNumber.asGameVersion(it.gameVersion).isAprilFools

                        else -> false
                    }

                    else -> true
                }
            }
            .filter { it.gameVersion.contains(searchText) }
            .sorted()
            .collect(Collectors.toList())
    }
}

@Composable
fun VersionInstallScreen(
    onOpenInstallInfo: (gameVersion: String) -> Unit,
) {
    val context = LocalContext.current
    val holder = androidx.compose.runtime.remember { VersionInstallStateHolder(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // 过滤条 + 搜索（对齐 page_install_version.xml 顶部 bar）
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    VersionTypeCheckbox(
                        label = stringResource(R.string.version_game_release),
                        checked = holder.checkRelease,
                        onCheckedChange = {
                            holder.checkRelease = it
                            holder.refreshDisplayVersions()
                        },
                    )
                    VersionTypeCheckbox(
                        label = stringResource(R.string.version_game_snapshot),
                        checked = holder.checkSnapshot,
                        onCheckedChange = {
                            holder.checkSnapshot = it
                            holder.refreshDisplayVersions()
                        },
                    )
                    VersionTypeCheckbox(
                        label = stringResource(R.string.version_game_old),
                        checked = holder.checkOld,
                        onCheckedChange = {
                            holder.checkOld = it
                            holder.refreshDisplayVersions()
                        },
                    )
                    VersionTypeCheckbox(
                        label = stringResource(R.string.version_game_april_fools),
                        checked = holder.checkAprilFools,
                        onCheckedChange = {
                            holder.checkAprilFools = it
                            holder.refreshDisplayVersions()
                        },
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = holder.searchText,
                        onValueChange = holder::onSearchChange,
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.search),
                        useLabelAsPlaceholder = true,
                        singleLine = true,
                    )
                    Spacer(Modifier.width(6.dp))
                    IconButton(onClick = holder::refreshList, enabled = !holder.loading) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(
                                R.drawable.ic_baseline_refresh_24,
                            ),
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))

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
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            R.drawable.ic_baseline_refresh_24,
                        ),
                        contentDescription = null,
                        tint = MiuixTheme.colorScheme.onSurface,
                    )
                }

                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(holder.items, key = { it.selfVersion }) { version ->
                        RemoteVersionRow(
                            version = version,
                            onClick = { onOpenInstallInfo(version.gameVersion) },
                            // 入场动画对齐 RemoteVersionListAdapter:108（animationSpeed×30）
                            modifier = fclItemEntryModifier(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VersionTypeCheckbox(
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
        )
        Text(text = label, style = MiuixTheme.textStyles.body2)
    }
}
