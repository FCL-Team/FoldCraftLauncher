package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.versioning.VersionNumber
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * 远程资源详情页（对齐 RemoteModInfoPage + page_download_addon_info.xml）：
 * 头部信息（图标/名称/tag/简介/mcmod/官网）+ 游戏版本列表（搜索过滤、推荐置顶）。
 *
 * 行为对齐（interaction-map §5.4）：
 * - 版本搜索框实时过滤游戏版本列表，推荐版本置顶（:145-159）；
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

    val translations: ModTranslations =
        ModTranslations.getTranslationsByRepositoryType(repository.type)
    val translatedMod: ModTranslations.Mod? = translations.getModByCurseForgeId(addon.slug)

    /** 标题（对齐 :131-133；安装前缀由 [installed] 在渲染期拼接）。 */
    val displayName: String =
        if (translatedMod != null && LocaleUtils.isChinese(context)) translatedMod.displayName
        else addon.title

    /** 分类 tag（对齐 :135-139）。 */
    val tag: String = StringUtils.removeSuffix(
        addon.categories.stream()
            .map { tab.localizedCategory(context, isModrinth, it) }
            .collect(Collectors.toList())
            .joinToString("   ") + "   ",
        "   ",
    )

    init {
        // 对齐 onStart :141-142
        loadModVersions()
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
                loading = false
                failed = true
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
            val found = withContext(Dispatchers.IO) {
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
            if (found) installed = true
        }
    }
}

@Composable
fun RemoteModInfoScreen(
    holder: RemoteModInfoStateHolder,
    onOpenVersionPage: (versions: List<RemoteMod.Version>) -> Unit,
) {
    val context = LocalContext.current
    val recommendPrefix = stringResource(R.string.recommend_version)

    Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
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

            else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                item(key = "info") {
                    InfoCard(holder)
                }
                item(key = "search") {
                    FCLTextField(
                        value = holder.searchText,
                        onValueChange = { holder.searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        label = stringResource(R.string.search),
                        useLabelAsPlaceholder = true,
                        singleLine = true,
                    )
                }
                items(
                    holder.displayedGameVersions(),
                    key = { it },
                ) { gameVersion ->
                    // 入场动画对齐 ModGameVersionAdapter:69（animationSpeed×30）；
                    // 按压反馈对齐 anim_scale（ModGameVersionAdapter:62）→ Miuix Sink
                    FCLCard(
                        onClick = {
                            holder.versionMap[gameVersion]?.let(onOpenVersionPage)
                        },
                        pressFeedbackType = PressFeedbackType.Sink,
                        modifier = fclItemEntryModifier()
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        // 对齐 version_list 容器 bg_container_white +
                        // RemoteModInfoPage:118 registerEvent（ltColor 染色 = primaryContainer，
                        // 旧版条目自身透明、底色由列表容器提供）
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                    ) {
                        Text(
                            // 对齐 ModGameVersionAdapter：推荐项不带 "Minecraft " 前缀
                            text = (if (gameVersion.contains(recommendPrefix)) "" else "Minecraft ") + gameVersion,
                            style = MiuixTheme.textStyles.body2,
                            // 对齐 ModGameVersionAdapter:60-61（autoTint = onPrimary）
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        )
                    }
                }
            }
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
            .padding(bottom = 8.dp),
        // 对齐 page_download_addon_info.xml 头部 FCLLinearLayout 的 bg_container_white +
        // auto_linear_background_tint（ltColor 染色 = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            GlideImage(
                model = holder.addon.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                // 对齐 page_download_addon_info.xml 头部：name/tag/description/mcmod
                // 均为 auto_text_tint（autoTint = onPrimary）
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
                    style = MiuixTheme.textStyles.body1,
                    color = MiuixTheme.colorScheme.onPrimary,
                )
                if (holder.tag.isNotBlank()) {
                    Text(
                        text = holder.tag,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
                Text(
                    text = holder.addon.description,
                    fontSize = 11.sp,
                    color = MiuixTheme.colorScheme.onPrimary,
                )
            }
            Column {
                // mcmod 按钮仅存在中文译名时可见（对齐 :132）；
                // 旧版为 auto_text_tint 纯文本（autoTint = onPrimary）
                holder.translatedMod?.let { mod ->
                    Text(
                        text = stringResource(R.string.mcmod),
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .clickable {
                                AndroidUtils.openLink(context, holder.translations.getMcmodUrl(mod))
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
                if (StringUtils.isNotBlank(holder.addon.pageUrl)) {
                    // 对齐 website FCLImageButton auto_tint（图标 autoTint = onPrimary）
                    IconButton(onClick = {
                        AndroidUtils.openLink(context, holder.addon.pageUrl)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_earth_24),
                            contentDescription = null,
                            tint = MiuixTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}
