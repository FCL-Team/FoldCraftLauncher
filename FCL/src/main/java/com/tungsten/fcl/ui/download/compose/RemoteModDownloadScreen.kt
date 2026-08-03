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
import com.tungsten.fcl.ui.theme.FCLThemeTokens
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
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.EnumMap

/** 依赖类型 → 本地化字符串 key（迁移自遗留 RemoteModDownloadPage.STRING_ID_KEY）。 */
private val DEPENDENCY_STRING_ID_KEY =
    EnumMap<RemoteMod.DependencyType, String>(RemoteMod.DependencyType::class.java).apply {
        put(RemoteMod.DependencyType.EMBEDDED, "mods_dependency_embedded")
        put(RemoteMod.DependencyType.OPTIONAL, "mods_dependency_optional")
        put(RemoteMod.DependencyType.REQUIRED, "mods_dependency_required")
        put(RemoteMod.DependencyType.TOOL, "mods_dependency_tool")
        put(RemoteMod.DependencyType.INCLUDE, "mods_dependency_include")
        put(RemoteMod.DependencyType.INCOMPATIBLE, "mods_dependency_incompatible")
        put(RemoteMod.DependencyType.BROKEN, "mods_dependency_broken")
    }

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
        // 对齐 page_download_addon.xml 头部 FCLLinearLayout 的 bg_container_white +
        // auto_linear_background_tint（ltColor 染色 = primaryContainer）
        FCLCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
        ) {
            // 对齐 page_download_addon.xml 头部 padding 10/8
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                // 对齐 page_download_addon.xml 头部：name/tag/date 均 auto_text_tint（autoTint = onPrimary），
                // name+tag 同行内联（tag marginStart=10dp，D-P2-1）
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = holder.modVersion.name,
                        style = MiuixTheme.textStyles.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    Text(
                        text = modVersionTag(context, holder.modVersion),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f, fill = false),
                    )
                }
                Text(
                    text = REMOTE_MOD_DATE_FORMATTER.format(holder.modVersion.datePublished),
                    // 对齐 page_download_addon.xml date：12sp 单行
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
        }
        Spacer(Modifier.height(10.dp))

        // 依赖区（对齐 dependency_layout，marginTop=10dp）
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
                    // 对齐 page_download_addon.xml retry（无 auto_tint）：drawable 自带静态灰
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            R.drawable.ic_baseline_refresh_24,
                        ),
                        contentDescription = null,
                        tint = FCLThemeTokens.StrokeGray,
                    )
                }

                holder.dependencies.isNotEmpty() -> FCLCard(
                    // 对齐 dependency_layout 的 bg_container_white +
                    // RemoteModDownloadPage:166 registerEvent（ltColor 染色 = primaryContainer）
                    colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // 单容器 + 1px 灰分隔线（D-P1-2，对齐 loadDependencyList：
                    // 组间 preSplit、组标题下 split、组内行 divider 均为 1px darker_gray）
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        // EnumMap 迭代顺序 = 枚举声明顺序（对齐遗留 EnumMap keySet 顺序）
                        holder.dependencies.entries.forEachIndexed { groupIndex, (type, mods) ->
                            if (groupIndex > 0) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = FCLThemeTokens.StrokeGray,
                                )
                            }
                            // 分组标题（对齐 :120-127 autoTint 文本 = onPrimary，padding 10dp）
                            Text(
                                text = AndroidUtils.getLocalizedText(
                                    context,
                                    DEPENDENCY_STRING_ID_KEY[type],
                                ),
                                style = MiuixTheme.textStyles.body2,
                                maxLines = 1,
                                color = MiuixTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(10.dp),
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = FCLThemeTokens.StrokeGray,
                            )
                            mods.forEachIndexed { index, mod ->
                                if (index > 0) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = FCLThemeTokens.StrokeGray,
                                    )
                                }
                                DependencyRow(
                                    mod = mod,
                                    repository = repository,
                                    onClick = { onOpenDependency(mod) },
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))

        // 四按钮一行等宽（对齐 page_download_addon.xml 底部 Row：相邻按钮
        // marginEnd=5dp + marginStart=5dp = 10dp 间距，D-P1-1）
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                onClick = onDownload,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_download), maxLines = 1)
            }
            Button(
                onClick = onSaveAs,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_save_as), maxLines = 1)
            }
            Button(
                // 对齐 :219-221：取消 = 一次返回；旧版四按钮均为 FCLButton（主色实心 = primary）
                onClick = { LegacyBridge.onBackPressed() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(
                    text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                    maxLines = 1,
                )
            }
            Button(
                // 对齐 :222-227：返回 = 连续 3 次返回弹三层，点击后禁用
                onClick = {
                    backClicked = true
                    repeat(3) { LegacyBridge.onBackPressed() }
                },
                modifier = Modifier.weight(1f),
                enabled = !backClicked,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.button_back), maxLines = 1)
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
        // 对齐 DependencyAdapter:63-64（autoTint = onPrimary）
        color = MiuixTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(10.dp),
    )
}
