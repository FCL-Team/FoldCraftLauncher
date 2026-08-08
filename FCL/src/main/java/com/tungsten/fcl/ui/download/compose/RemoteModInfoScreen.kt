package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.versioning.VersionNumber
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.fclCursorBrush
import com.tungsten.fcl.ui.compose.fclTextFieldColors
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.logging.Level
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * 远程资源详情页（对齐 RemoteModInfoPage + page_download_addon_info.xml）：
 * 头部信息（图标/名称/tag/简介/mcmod/官网）+ 双栏主体——
 * 左栏（weight 0.5）搜索框 + 截图面板（加载圈/重试/无结果/列表），
 * 右栏（weight 1）游戏版本列表（搜索过滤、推荐置顶）。
 *
 * 行为对齐（interaction-map §5.4）：
 * - 左栏版本搜索框实时过滤游戏版本列表，推荐版本置顶（:145-159）；
 * - onStart 同时加载版本与截图（:141-142），截图三态机对齐 loadScreenshots :179-193
 *   （加载中→进度圈、空→无结果提示、失败→重试图标）；
 * - mcmod/官网按钮开浏览器（:315-325）；
 * - 后台检测已安装加"[已安装]"前缀（:198-223）；
 * - 版本项点击 → [onOpenVersionPage]（二级临时页堆叠）。
 */
class RemoteModInfoStateHolder(
    private val context: Context,
    val tab: DownloadTab,
    val repository: RemoteModRepository,
    val addon: RemoteMod,
    private val isModrinth: Boolean,
    private val scope: CoroutineScope,
) {
    var loading by mutableStateOf(true)
    var failed by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var versionMap by mutableStateOf<Map<String, List<RemoteMod.Version>>>(emptyMap())
    var recommendedKey by mutableStateOf<String?>(null)
    var installed by mutableStateOf(false)

    /** 截图状态（对齐 screenshot_loading / screenshot_retry / screenshot_no_result 三态）。 */
    var screenshotLoading by mutableStateOf(false)
    var screenshotFailed by mutableStateOf(false)
    var screenshotNoResult by mutableStateOf(false)
    var screenshots by mutableStateOf<List<RemoteMod.Screenshot>>(emptyList())

    val translations: ModTranslations =
        ModTranslations.getTranslationsByRepositoryType(repository.type)
    val translatedMod: ModTranslations.Mod? = translations.getModByCurseForgeId(addon.slug)

    /** 标题（对齐 :131-133；安装前缀由 [installed] 在渲染期拼接）。 */
    val displayName: String =
        if (translatedMod != null && LocaleUtils.isChinese(context)) translatedMod.displayName
        else addon.title

    /** 分类 tag（对齐 :135-139；categories 可能为 null，回退空列表避免 holder 初始化即 NPE）。 */
    val tag: String = StringUtils.removeSuffix(
        (addon.categories ?: emptyList()).stream()
            .map { tab.localizedCategory(context, isModrinth, it) }
            .collect(Collectors.toList())
            .joinToString("   ") + "   ",
        "   ",
    )

    init {
        // 对齐 onStart :141-142（同时加载版本与截图）
        loadModVersions()
        loadScreenshots()
    }

    /** 展示的游戏版本键列表（对齐 loadGameVersions :145-159：过滤 + 推荐置顶）。 */
    fun displayedGameVersions(): List<String> {
        val list = versionMap.keys.stream()
            .sorted { a, b -> VersionNumber.compare(b, a) }
            .filter { it.contains(searchText) }
            .collect(Collectors.toList())
        val recommended = recommendedKey
        if (recommended != null && list.contains(recommended)) {
            list.remove(recommended)
            list.add(0, recommended)
        }
        return list
    }

    /** 对齐 loadModVersions :161-177。 */
    fun loadModVersions() {
        loading = true
        failed = false
        scope.launch {
            try {
                val (map, recommended) = withContext(Dispatchers.IO) {
                    sortVersions(addon.data.loadVersions(repository))
                }
                versionMap = map
                recommendedKey = recommended
                loading = false
                checkInstalled()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                // 旧版 Task.whenComplete 静默置失败态；保留语义但输出真实异常（排查地图等详情页秒失败）
                Logging.LOG.log(Level.WARNING, "Failed to load versions of ${addon.title}", e)
                loading = false
                failed = true
            }
        }
    }

    /** 对齐 loadScreenshots :179-193（加载中→进度圈、空→无结果、失败→重试图标）。 */
    fun loadScreenshots() {
        screenshotLoading = true
        screenshotFailed = false
        screenshotNoResult = false
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    addon.data.loadScreenshots(repository)
                }
                screenshots = result
                screenshotLoading = false
                if (result.isEmpty()) screenshotNoResult = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Logging.LOG.log(Level.WARNING, "Failed to load screenshots of ${addon.title}", e)
                screenshotLoading = false
                screenshotFailed = true
            }
        }
    }

    /** 版本分类与推荐版本计算（对齐 sortVersions :225-264）。 */
    private fun sortVersions(
        versions: Stream<RemoteMod.Version>,
    ): Pair<Map<String, List<RemoteMod.Version>>, String?> {
        val classified = LinkedHashMap<String, MutableList<RemoteMod.Version>>()
        versions.forEach { version ->
            for (gameVersion in version.gameVersions) {
                classified.getOrPut(gameVersion) { mutableListOf() }.add(version)
            }
        }
        classified.values.forEach { list ->
            list.sortByDescending { it.datePublished }
        }

        var recommended: String? = null
        if (tab != DownloadTab.MODPACK) {
            val profile = Profiles.getSelectedProfile()
            if (profile.selectedVersion != null) {
                val analyzer = LibraryAnalyzer.analyze(
                    profile.repository.getResolvedPreservingPatchesVersion(profile.selectedVersion),
                    profile.selectedVersion,
                )
                val modLoaders = analyzer.modLoaders
                val mcv = analyzer.getVersion(LibraryAnalyzer.LibraryType.MINECRAFT).orElse("")
                if (classified.containsKey(mcv)) {
                    val matches = classified.getValue(mcv).filter { version ->
                        if (tab == DownloadTab.MOD) {
                            val loader =
                                version.loaders.firstOrNull { modLoaders.contains(it) }
                            if (loader != null) {
                                recommended =
                                    "${context.getString(R.string.recommend_version)}: $mcv ${loader.name}"
                                true
                            } else {
                                false
                            }
                        } else {
                            recommended = "${context.getString(R.string.recommend_version)}: $mcv"
                            true
                        }
                    }
                    matches.forEach { version ->
                        classified.getOrPut(recommended!!) { mutableListOf() }.add(version)
                    }
                }
            }
        }
        return classified to recommended
    }

    /** 已安装检测（对齐 checkInstalled :198-223）。 */
    private fun checkInstalled() {
        scope.launch {
            // 旧版 Task.whenComplete 对异常静默忽略；此处同步吞掉，
            // 避免异常逃逸协程导致整个 composition scope 被取消（页面假死、重试失效）
            val found = try {
                withContext(Dispatchers.IO) {
                    val remoteName = addon.title.replace(" ", "").lowercase()
                    val modFiles = Profiles.getSelectedProfile().repository
                        .getModManager(Profiles.getSelectedVersion())
                        .mods.parallelStream()
                        .filter { localModFile ->
                            remoteName.contains(localModFile.name.replace(" ", "").lowercase())
                        }
                        .collect(Collectors.toList())
                    for (localModFile in modFiles) {
                        try {
                            val optional =
                                repository.getRemoteVersionByLocalFile(localModFile, localModFile.file)
                            if (optional.isPresent && addon.modID == optional.get().modid) {
                                return@withContext true
                            }
                        } catch (_: Throwable) {
                        }
                    }
                    false
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Logging.LOG.log(Level.WARNING, "Failed to check installed state of ${addon.title}", e)
                false
            }
            if (found) installed = true
        }
    }
}

@Composable
fun RemoteModInfoScreen(
    holder: RemoteModInfoStateHolder,
    onOpenVersionPage: (versions: List<RemoteMod.Version>) -> Unit,
) {
    val recommendPrefix = stringResource(R.string.recommend_version)

    // 对齐 page_download_addon_info.xml 根 ConstraintLayout：
    // paddingStart/Top/End=10dp，无 paddingBottom
    Box(modifier = Modifier.fillMaxSize().padding(start = 10.dp, top = 10.dp, end = 10.dp)) {
        when {
            holder.loading -> InfiniteProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MiuixTheme.colorScheme.primary,
            )

            holder.failed -> IconButton(
                onClick = holder::loadModVersions,
                modifier = Modifier.align(Alignment.Center),
            ) {
                // 对齐 page_download_addon_info.xml retry（无 auto_tint）：drawable 自带静态灰
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_refresh_24),
                    contentDescription = null,
                    tint = FCLThemeTokens.StrokeGray,
                )
            }

            // 对齐根 LinearLayout(vertical)：头部信息卡（wrap）+ 双栏主体（填满剩余，marginTop=10dp）
            else -> Column(modifier = Modifier.fillMaxSize()) {
                InfoCard(holder)
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    // 左栏（对齐左 FCLConstraintLayout）：weight 0.5、match_parent 高、
                    // marginEnd=10dp、bg_container_white + auto_tint（ltColor = primaryContainer）、
                    // paddingHorizontal=8dp；顶部搜索框，下方截图面板
                    // （screenshot_recyclerView/loading/retry/no_result，约束填满 search 以下区域）
                    FCLCard(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .padding(end = 10.dp),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                        insideMargin = PaddingValues(horizontal = 8.dp),
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            VersionSearchField(holder)
                            ScreenshotPanel(holder, Modifier.fillMaxWidth().weight(1f))
                        }
                    }
                    // 右栏（对齐 version_list）：weight 1、match_parent 高、
                    // bg_container_white + ltColor 染色（primaryContainer）内纯文本行，
                    // 条目透明底、divider 透明 10dp（= 行间 10dp 间距）——去卡片化（I-P1-1）
                    FCLCard(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                        insideMargin = PaddingValues(0.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            holder.displayedGameVersions().forEach { gameVersion ->
                                item(key = gameVersion) {
                                    Text(
                                        // 对齐 ModGameVersionAdapter：推荐项不带 "Minecraft " 前缀
                                        text = (if (gameVersion.contains(recommendPrefix)) "" else "Minecraft ") + gameVersion,
                                        // 对齐 ModGameVersionAdapter:60-61（默认 14sp、autoTint = onPrimary）
                                        style = MiuixTheme.textStyles.body2,
                                        color = MiuixTheme.colorScheme.onPrimary,
                                        // 对齐 ModGameVersionAdapter:58（singleLine）
                                        maxLines = 1,
                                        // 入场动画对齐 ModGameVersionAdapter:69（animationSpeed×30，逐项）
                                        modifier = fclItemEntryModifier()
                                            .fillMaxWidth()
                                            .clickable {
                                                holder.versionMap[gameVersion]?.let(onOpenVersionPage)
                                            }
                                            // 对齐 ModGameVersionAdapter:55-56（padding 10dp）
                                            .padding(10.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 左栏版本搜索框（对齐 search FCLEditText：match_parent 宽、gravity=center、
 * hint=@string/search、singleLine、auto_edit_tint：文字 onPrimary、hint autoHintTint、
 * 下划线聚焦 color/未聚焦 dkColor——规格同 FCLControls.FCLTextField，仅文字居中为其特有）。
 */
@Composable
private fun VersionSearchField(holder: RemoteModInfoStateHolder) {
    val scheme = MiuixTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val underlineColor = if (isFocused) scheme.primary else scheme.primaryVariant
    TextField(
        value = holder.searchText,
        onValueChange = { holder.searchText = it },
        modifier = Modifier.fillMaxWidth().drawBehind {
            val strokeWidth = (if (isFocused) 2.dp else 1.dp).toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = underlineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth,
            )
        },
        colors = fclTextFieldColors(),
        label = stringResource(R.string.search),
        useLabelAsPlaceholder = true,
        // 对齐 search 的 android:gravity="center"
        textStyle = MiuixTheme.textStyles.main.copy(
            color = scheme.onPrimary,
            textAlign = TextAlign.Center,
        ),
        singleLine = true,
        interactionSource = interactionSource,
        cursorBrush = fclCursorBrush(),
    )
}

/**
 * 左栏截图面板（对齐 page_download_addon_info.xml 左栏 search 下方的
 * screenshot_recyclerView / screenshot_loading / screenshot_retry / screenshot_no_result）：
 * 加载中→居中进度圈；失败→居中重试图标（点击重新加载）；空→居中无结果提示；
 * 否则竖向截图列表（对齐 RecyclerView + LinearLayoutManager）。
 */
@Composable
private fun ScreenshotPanel(holder: RemoteModInfoStateHolder, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        when {
            holder.screenshotLoading -> InfiniteProgressIndicator(
                // 对齐 screenshot_loading：居中 + margin 8dp
                modifier = Modifier.align(Alignment.Center).padding(8.dp),
                color = MiuixTheme.colorScheme.primary,
            )

            holder.screenshotFailed -> Icon(
                painter = painterResource(R.drawable.ic_baseline_refresh_24),
                contentDescription = null,
                // 对齐 screenshot_retry（FCLImageView 无 auto_tint）：drawable 自带静态灰
                tint = FCLThemeTokens.StrokeGray,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
                    .clickable(onClick = holder::loadScreenshots),
            )

            holder.screenshotNoResult -> Text(
                text = stringResource(R.string.mods_screenshot_no_result),
                // 对齐 screenshot_no_result（auto_text_tint = onPrimary，默认 14sp）
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center).padding(8.dp),
            )

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(holder.screenshots) { screenshot ->
                    ScreenshotItem(screenshot)
                }
            }
        }
    }
}

/**
 * 截图条目（对齐 view_mod_screenshot.xml + RemoteModScreenshotAdapter）：
 * 全宽 adjustViewBounds 图（marginTop=10dp，加载中显示进度圈、失败显示 refresh 占位、
 * 点击图片重新加载）+ 标题（14sp 居中）+ 描述（11sp 居中），标题/描述为空白时隐藏。
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ScreenshotItem(screenshot: RemoteMod.Screenshot) {
    // 对齐 RemoteModScreenshotAdapter 点击重载：key 重建触发全新 Glide 请求
    var retryKey by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxWidth()) {
        key(retryKey) {
            GlideImage(
                model = screenshot.imageUrl,
                contentDescription = null,
                // 对齐 screenshot：match_parent 宽 + adjustViewBounds/fitCenter + marginTop=10dp
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable { retryKey++ },
                contentScale = ContentScale.FillWidth,
                // 对齐 loading FCLProgressBar（加载中显示进度圈）
                loading = placeholder {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        InfiniteProgressIndicator(color = MiuixTheme.colorScheme.primary)
                    }
                },
                // 对齐 .error(R.drawable.ic_baseline_refresh_24)
                failure = placeholder(R.drawable.ic_baseline_refresh_24),
                // 对齐 DiskCacheStrategy.NONE + fitCenter
                requestBuilderTransform = {
                    it.diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter()
                },
            )
        }
        // 对齐 title（auto_text_tint = onPrimary，默认 14sp，居中，空白隐藏）
        if (StringUtils.isNotBlank(screenshot.title)) {
            Text(
                text = screenshot.title.orEmpty(),
                style = MiuixTheme.textStyles.body2,
                color = MiuixTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        // 对齐 description（11sp，居中，空白隐藏）
        if (StringUtils.isNotBlank(screenshot.description)) {
            Text(
                text = screenshot.description.orEmpty(),
                fontSize = 11.sp,
                color = MiuixTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** 头部信息卡（对齐 page_download_addon_info.xml 顶部：图标/名称/tag/简介 + mcmod/官网）。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun InfoCard(holder: RemoteModInfoStateHolder) {
    val context = LocalContext.current
    FCLCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        // 对齐 page_download_addon_info.xml 头部 FCLLinearLayout 的 bg_container_white +
        // auto_linear_background_tint（ltColor 染色 = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // 对齐 page_download_addon_info.xml 头部 padding 10/8
                .padding(horizontal = 10.dp, vertical = 8.dp),
            // 对齐 icon/文字列/mcmod/website 的 android:layout_gravity="center"
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = holder.addon.iconUrl,
                contentDescription = null,
                // 对齐 page_download_addon_info.xml icon：30×30dp
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                // 对齐 page_download_addon_info.xml 头部：name/tag/description/mcmod
                // 均为 auto_text_tint（autoTint = onPrimary）
                // name + tag 同行内联（tag marginStart=10dp）
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (holder.installed) {
                            String.format(
                                "[%s] %s",
                                stringResource(R.string.installed),
                                holder.displayName,
                            )
                        } else {
                            holder.displayName
                        },
                        // 对齐 name：textSize=14sp（body2=14sp）
                        style = MiuixTheme.textStyles.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (holder.tag.isNotBlank()) {
                        Text(
                            text = holder.tag,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                // 对齐 tag：marginStart=10dp + padding 4/2
                                .padding(start = 10.dp)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .weight(1f, fill = false),
                        )
                    }
                }
                // 对齐 page_download_addon_info.xml description：singleLine 12sp
                Text(
                    text = holder.addon.description,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
            // mcmod 按钮仅存在中文译名时可见（对齐 :132）；
            // 旧版为 auto_text_tint 纯文本（默认 14sp，autoTint = onPrimary），
            // 与 website 同为头部行内兄弟节点（marginStart=10dp、gravity=center）
            holder.translatedMod?.let { mod ->
                Text(
                    text = stringResource(R.string.mcmod),
                    style = MiuixTheme.textStyles.body2,
                    maxLines = 1,
                    color = MiuixTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            AndroidUtils.openLink(context, holder.translations.getMcmodUrl(mod))
                        },
                )
            }
            if (StringUtils.isNotBlank(holder.addon.pageUrl)) {
                // 对齐 website FCLImageButton：auto_tint（图标 autoTint = onPrimary）、
                // no_padding（按钮=drawable 24dp）、marginStart=10dp，
                // 图标为 ic_baseline_jump_24（I-P2-1）
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_jump_24),
                    contentDescription = null,
                    tint = MiuixTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(24.dp)
                        .clickable {
                            AndroidUtils.openLink(context, holder.addon.pageUrl)
                        },
                )
            }
        }
    }
}
