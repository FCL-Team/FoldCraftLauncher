package com.tungsten.fcl.ui.download.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.download.common.ModVersionAdapter
import com.tungsten.fcl.ui.download.common.RemoteModDownloadPage
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.EnumMap

/**
 * 远程资源下载确认页（对齐 RemoteModDownloadPage + page_download_addon.xml）：
 * 文件信息 + 依赖分组列表 + 下载/另存为/取消/返回四按钮。
 *
 * 行为对齐（interaction-map §5.4）：
 * - 依赖按类型分组展示（跳过 INCOMPATIBLE/BROKEN，:84-87），失败重试 + Toast（:103-104）；
 * - 依赖项点击 → 再开一层详情页（:132-135）；
 * - 取消 = 一次返回（:219-221）；**"返回"按钮连续 3 次返回弹三层临时页（:222-227），
 *   且点击后禁用防重复**。
 */
class RemoteModDownloadStateHolder(
    private val context: Context,
    val modVersion: RemoteMod.Version,
    private val scope: CoroutineScope,
) {
    var loading by mutableStateOf(true)
    var failed by mutableStateOf(false)
    var dependencies by mutableStateOf<Map<RemoteMod.DependencyType, List<RemoteMod>>>(emptyMap())

    init {
        loadDependencies()
    }

    /** 对齐 loadDependencies :79-107。 */
    fun loadDependencies() {
        loading = true
        failed = false
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val deps = EnumMap<RemoteMod.DependencyType, MutableList<RemoteMod>>(
                        RemoteMod.DependencyType::class.java,
                    )
                    for (dependency in modVersion.dependencies) {
                        if (dependency.type == RemoteMod.DependencyType.INCOMPATIBLE ||
                            dependency.type == RemoteMod.DependencyType.BROKEN
                        ) {
                            continue
                        }
                        deps.getOrPut(dependency.type) { mutableListOf() }.add(dependency.load())
                    }
                    deps as Map<RemoteMod.DependencyType, List<RemoteMod>>
                }
                dependencies = result
                loading = false
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                loading = false
                failed = true
                Toast.makeText(
                    context,
                    context.getString(R.string.download_failed_refresh),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}

@Composable
fun RemoteModDownloadScreen(
    holder: RemoteModDownloadStateHolder,
    repository: RemoteModRepository,
    onDownload: () -> Unit,
    onSaveAs: () -> Unit,
    onOpenDependency: (RemoteMod) -> Unit,
) {
    val context = LocalContext.current
    var backClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // 文件信息头（对齐 name/tag/date）
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = holder.modVersion.name,
                    style = MiuixTheme.textStyles.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = modVersionTag(context, holder.modVersion),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MiuixTheme.colorScheme.primary,
                )
                Text(
                    text = ModVersionAdapter.FORMATTER.format(holder.modVersion.datePublished),
                    fontSize = 11.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        // 依赖区（对齐 dependency_layout）
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                holder.loading -> InfiniteProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MiuixTheme.colorScheme.primary,
                )

                holder.failed -> IconButton(
                    onClick = holder::loadDependencies,
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

                holder.dependencies.isNotEmpty() -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    // EnumMap 迭代顺序 = 枚举声明顺序（对齐遗留 EnumMap keySet 顺序）
                    holder.dependencies.forEach { (type, mods) ->
                        Text(
                            text = AndroidUtils.getLocalizedText(
                                context,
                                RemoteModDownloadPage.STRING_ID_KEY[type],
                            ),
                            style = MiuixTheme.textStyles.body2,
                            color = MiuixTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 6.dp),
                        )
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                mods.forEach { mod ->
                                    DependencyRow(
                                        mod = mod,
                                        repository = repository,
                                        onClick = { onOpenDependency(mod) },
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // 四按钮（对齐 download/save_as/cancel/back）
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onDownload,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_download))
            }
            Button(
                onClick = onSaveAs,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_save_as))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                // 对齐 :219-221：取消 = 一次返回
                onClick = { LegacyBridge.onBackPressed() },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative))
            }
            Button(
                // 对齐 :222-227：返回 = 连续 3 次返回弹三层，点击后禁用
                onClick = {
                    backClicked = true
                    repeat(3) { LegacyBridge.onBackPressed() }
                },
                modifier = Modifier.weight(1f),
                enabled = !backClicked,
            ) {
                Text(text = stringResource(R.string.button_back))
            }
        }
    }
}

/** 依赖项行（对齐 DependencyAdapter：图标前缀文本 + 译名/原名，纯文本行）。 */
@Composable
private fun DependencyRow(
    mod: RemoteMod,
    repository: RemoteModRepository,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val translated = ModTranslations.getTranslationsByRepositoryType(repository.type)
        .getModByCurseForgeId(mod.slug)
    val name =
        if (translated != null && LocaleUtils.isChinese(context)) translated.displayName
        else mod.title
    Text(
        text = "${stringResource(R.string.mods_dependency)}: $name",
        style = MiuixTheme.textStyles.body2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(10.dp),
    )
}
