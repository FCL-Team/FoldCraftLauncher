package com.tungsten.fcl.ui.download.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mio.util.format
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.compose.FCLDialog
import com.tungsten.fcl.ui.compose.FCLDialogButton
import com.tungsten.fcl.ui.compose.FCLDropdownField
import com.tungsten.fcl.ui.compose.dialog.MiuixTranslationDialog
import com.tungsten.fcl.ui.compose.fclCollapsingBar
import com.tungsten.fcl.ui.compose.fclItemEntryModifier
import com.tungsten.fcl.ui.compose.rememberFCLCollapsingBarState
import com.tungsten.fcl.ui.theme.FCLThemeTokens
import com.tungsten.fcl.util.ModTranslations
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.util.Logging
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import com.tungsten.fcl.ui.compose.FCLCard
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import com.tungsten.fcl.ui.compose.FCLTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType
import java.util.logging.Level
import java.util.stream.Collectors

/**
 * 远程资源搜索页（下载 Tab 1-5，对齐 DownloadPage + page_download.xml 全家：
 * Mod/整合包/资源包/存档/光影）。
 *
 * 行为对齐（interaction-map §5.2/§5.3）：
 * - 搜索按钮点击 → 重置页码并搜索（:391）；搜索为异步任务，先取消旧任务（:162-164）；
 * - 按钮式分页：首/上/下/末四按钮（带边界判断 :395-410）+ 点击页码弹窗跳页（:414）；
 * - 下载源切换 → 重新拉取分类并重搜（:321,445）；分类为递归缩进树（:448-452）；
 * - Mod 页 ModLoader 并行流二次过滤（:168-176）与"[已安装]"前缀（Adapter :37-61,117-128）；
 * - 搜索失败 → 重试按钮（:411）。
 */
class RemoteModSearchStateHolder(
    private val context: Context,
    val tab: DownloadTab,
    private val scope: CoroutineScope,
) {
    /** 当前搜索参数快照（retry 需复用失败时的参数，对齐 DownloadPage.retrySearch 闭包）。 */
    private data class SearchParams(
        val gameVersion: String,
        val category: RemoteModRepository.Category?,
        val pageOffset: Int,
        val filter: String,
        val sort: RemoteModRepository.SortType,
        val loader: ModLoaderType?,
    )

    var searchText by mutableStateOf("")
    var isModrinth by mutableStateOf(tab.defaultModrinth)
    var gameVersion by mutableStateOf("")
    var categories by mutableStateOf(listOf(CategoryEntry(0, null)))
    var categoryIndex by mutableIntStateOf(0)
    var sortIndex by mutableIntStateOf(0)
    var modLoaderIndex by mutableIntStateOf(0)
    var pageOffset by mutableIntStateOf(0)
    var pageCount by mutableIntStateOf(-1)
    var loading by mutableStateOf(true)
    var failed by mutableStateOf(false)
    var results by mutableStateOf<List<RemoteModItemUi>>(emptyList())
    var showPageJumpDialog by mutableStateOf(false)

    val repository: RemoteModRepository = tab.createRepository { isModrinth }
    val gameVersions: List<String> =
        listOf("") + RemoteModRepository.DEFAULT_GAME_VERSIONS.toList()
    val sortTypes: List<RemoteModRepository.SortType> = RemoteModRepository.SortType.values().toList()
    val modLoaders: List<ModLoaderType?> = listOf(
        null,
        ModLoaderType.FORGE,
        ModLoaderType.NEO_FORGED,
        ModLoaderType.FABRIC,
        ModLoaderType.QUILT,
    )

    private val downloadProvider = DownloadProviders.getDownloadProvider()
    private var searchJob: Job? = null
    private var retrySearch: (() -> Unit)? = null

    init {
        // 对齐 DownloadPage.create()：先拉分类（不触发搜索），再初始搜索
        refreshCategory(doSearch = false)
        performSearch()
    }

    private fun currentParams() = SearchParams(
        gameVersion = gameVersion,
        category = categories.getOrNull(categoryIndex)?.category,
        pageOffset = pageOffset,
        filter = searchText,
        sort = sortTypes[sortIndex],
        loader = if (tab.hasModLoaderFilter) modLoaders[modLoaderIndex] else null,
    )

    /** 搜索按钮：重置页码并搜索（对齐 :400-402）。 */
    fun onSearchClick() {
        pageOffset = 0
        performSearch()
    }

    /** 翻译对话框回填：仅改文本并搜索，不重置页码（对齐 :476-479）。 */
    fun onTranslationResult(text: String) {
        searchText = text
        performSearch()
    }

    fun onNextPage() {
        if (pageCount > 1 && pageOffset < pageCount - 1) {
            pageOffset++
            performSearch()
        }
    }

    fun onPreviousPage() {
        if (pageOffset > 0) {
            pageOffset--
            performSearch()
        }
    }

    fun onFirstPage() {
        if (pageCount != 0 && pageCount != -1) {
            pageOffset = 0
            performSearch()
        }
    }

    fun onLastPage() {
        if (pageCount != 0 && pageCount != -1) {
            pageOffset = pageCount - 1
            performSearch()
        }
    }

    fun onPageClick() {
        if (pageCount != 0 && pageCount != -1) {
            showPageJumpDialog = true
        }
    }

    /** 跳页（对齐 :424-436：解析失败静默；越界钳制到 [1, pageCount]）。 */
    fun onPageJump(input: String) {
        try {
            var i = input.toInt()
            if (i <= 0) {
                i = 1
            } else if (i > pageCount) {
                i = pageCount
            }
            pageOffset = i - 1
            performSearch()
        } catch (_: Throwable) {
        }
    }

    fun onRetry() {
        retrySearch?.invoke()
    }

    /** 下载源切换：重拉分类并重搜（对齐 downloadSource 监听 :321 + refreshCategory(true)）。 */
    fun onSourceChange(index: Int) {
        // spinner 数据顺序：[CurseForge, Modrinth]（对齐 :35-36 + :299）
        isModrinth = index == 1
        refreshCategory(doSearch = true)
    }

    /** 拉取分类树（对齐 refreshCategory :454-473：选中重置到"全部"）。 */
    private fun refreshCategory(doSearch: Boolean) {
        scope.launch {
            val fetched = try {
                withContext(Dispatchers.IO) { repository.categories }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                null
            } ?: return@launch
            val list = mutableListOf(CategoryEntry(0, null))
            for (category in fetched.collect(Collectors.toList())) {
                resolveCategoryEntries(category, 0, list)
            }
            categories = list
            categoryIndex = 0
            if (doSearch) performSearch()
        }
    }

    /** 执行搜索（对齐 DownloadPage.search(String,...) :162-198）。 */
    private fun performSearch(params: SearchParams = currentParams()) {
        retrySearch = null
        loading = true
        failed = false
        searchJob?.cancel()
        searchJob = scope.launch {
            try {
                val (list, totalPages, installed) = withContext(Dispatchers.IO) {
                    val result = repository.search(
                        downloadProvider,
                        params.gameVersion,
                        params.category,
                        params.pageOffset,
                        50,
                        params.filter,
                        params.sort,
                        RemoteModRepository.SortOrder.DESC,
                    )
                    var mods = result.results.collect(Collectors.toList())
                    if (tab.hasModLoaderFilter && params.loader != null) {
                        // 对齐 :168-176：ModLoader 并行流过滤，异常项保留
                        mods = mods.parallelStream().filter { mod ->
                            try {
                                mod.data.loadVersions(repository)
                                    .flatMap { it.loaders.stream() }
                                    .collect(Collectors.toList())
                                    .contains(params.loader)
                            } catch (_: Throwable) {
                                true
                            }
                        }.collect(Collectors.toList())
                    }
                    val installedIds =
                        if (tab.hasModLoaderFilter) computeInstalledIds() else emptySet()
                    Triple(mods, result.totalPages, installedIds)
                }
                pageCount = totalPages
                results = list.map { toItemUi(it, installed) }
                loading = false
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                loading = false
                failed = true
                pageCount = -1
                retrySearch = { performSearch(params) }
            }
        }
    }

    /**
     * 已安装 Mod 检测（对齐 RemoteModListAdapter.init :37-61：
     * 哈希匹配本地 Mod，产出远程 modid 集合；>100MB 跳过）。
     */
    private fun computeInstalledIds(): Set<String> {
        val ids = HashSet<String>()
        val modManager = Profiles.getSelectedProfile().repository
            .getModManager(Profiles.getSelectedVersion())
        val modFiles = runCatching {
            modManager.mods.parallelStream().collect(Collectors.toList())
        }.getOrNull() ?: return ids
        for (localModFile in modFiles) {
            try {
                val size = localModFile.file.toFile().length()
                if (size > 104857600) continue
                val optional = repository.getRemoteVersionByLocalFile(localModFile, localModFile.file)
                optional.ifPresent { localModFile.remoteVersion = it }
                localModFile.remoteVersion?.let { ids.add(it.modid) }
            } catch (e: Throwable) {
                System.gc()
                Logging.LOG.log(Level.SEVERE, e.toString())
            }
        }
        return ids
    }

    /** 列表项 UI 模型构建（对齐 RemoteModListAdapter.onBind :87-128）。 */
    private fun toItemUi(mod: RemoteMod, installedIds: Set<String>): RemoteModItemUi {
        val translations = ModTranslations.getTranslationsByRepositoryType(repository.type)
        val translated = translations.getModByCurseForgeId(mod.slug)
        var title =
            if (translated != null && LocaleUtils.isChinese(context)) translated.displayName
            else mod.title
        if (tab.hasModLoaderFilter && installedIds.isNotEmpty() && installedIds.contains(mod.modID)) {
            val installedPrefix = context.getString(R.string.installed)
            if (!title.startsWith(installedPrefix)) {
                title = String.format("[%s] %s", installedPrefix, title)
            }
        }
        val tag = mod.categories.stream()
            .map { tab.localizedCategory(context, isModrinth, it) }
            .collect(Collectors.toList())
            .joinToString("   ")
        return RemoteModItemUi(
            mod = mod,
            title = title,
            tag = tag,
            description = mod.description,
            iconUrl = mod.iconUrl,
            downloadCount = mod.downloadCount.format(context),
        )
    }
}

/** 搜索结果列表项 UI 模型（对齐 item_remote_mod.xml 绑定数据）。 */
data class RemoteModItemUi(
    val mod: RemoteMod,
    val title: String,
    val tag: String,
    val description: String,
    val iconUrl: String,
    val downloadCount: String,
)

@Composable
fun RemoteModSearchScreen(
    tab: DownloadTab,
    onOpenModInfo: (mod: RemoteMod, repository: RemoteModRepository, isModrinth: Boolean) -> Unit,
    onImportModpack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // tab 是 Composable 参数，切下载 Tab 重组时必须重建 holder（repository 由 tab 创建）
    val holder = remember(tab) { RemoteModSearchStateHolder(context, tab, scope) }
    // 对齐 page_download.xml FCLAppBarLayout scroll|enterAlways|snap（S-P0-2）
    val barState = rememberFCLCollapsingBarState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        // ---------- 左栏：搜索条件（对齐 search_layout，30% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
        ) {
            FCLCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                // 对齐 page_download.xml search_layout 的 bg_container_white +
                // DownloadPage:256 registerEvent（ltColor 染色 = primaryContainer）
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp),
                    // 对齐 page_download.xml search_layout：label/控件间距均 10dp
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // 对齐 page_download.xml "名称" FCLTextView 小字 label（13sp，auto_text_tint）
                    Text(
                        text = stringResource(R.string.mods_name),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary.copy(
                            alpha = if (holder.loading) 0.38f else 1f,
                        ),
                    )
                    // 对齐 DownloadPage :280-287：imeOptions=actionSearch 触发搜索；
                    // hint 按 supportChinese 切换中英文提示（S-P1-1 / S-P1-3）
                    FCLTextField(
                        value = holder.searchText,
                        onValueChange = { holder.searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(
                            if (tab.supportChinese) R.string.search_hint_chinese
                            else R.string.search_hint_english,
                        ),
                        useLabelAsPlaceholder = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { holder.onSearchClick() }),
                        singleLine = true,
                        enabled = !holder.loading,
                    )
                    // 对齐 page_download.xml 的 FCLTextView label + FCLSpinner 紧凑下拉行
                    // 纵向堆叠（S-P0-1），加载中禁用（S-P1-2，对齐 DownloadPage.setLoading）
                    if (tab.hasDownloadSource) {
                        FCLDropdownField(
                            label = stringResource(R.string.settings_launcher_download_source),
                            items = listOf(
                                stringResource(R.string.mods_curseforge),
                                stringResource(R.string.mods_modrinth),
                            ),
                            selectedIndex = if (holder.isModrinth) 1 else 0,
                            onSelectedIndexChange = holder::onSourceChange,
                            enabled = !holder.loading,
                        )
                    }
                    FCLDropdownField(
                        label = stringResource(R.string.world_game_version),
                        items = holder.gameVersions,
                        selectedIndex = holder.gameVersions.indexOf(holder.gameVersion).coerceAtLeast(0),
                        onSelectedIndexChange = { holder.gameVersion = holder.gameVersions[it] },
                        enabled = !holder.loading,
                    )
                    if (tab.hasModLoaderFilter) {
                        FCLDropdownField(
                            label = stringResource(R.string.modloader),
                            items = listOf(
                                stringResource(R.string.curse_category_0),
                                "Forge",
                                "NeoForge",
                                "Fabric",
                                "Quilt",
                            ),
                            selectedIndex = holder.modLoaderIndex,
                            onSelectedIndexChange = { holder.modLoaderIndex = it },
                            enabled = !holder.loading,
                        )
                    }
                    FCLDropdownField(
                        label = stringResource(R.string.mods_category),
                        items = holder.categories.map {
                            tab.localizedCategoryIndent(context, holder.isModrinth, it)
                        },
                        selectedIndex = holder.categoryIndex,
                        onSelectedIndexChange = { holder.categoryIndex = it },
                        enabled = !holder.loading,
                    )
                    FCLDropdownField(
                        label = stringResource(R.string.search_sort),
                        items = listOf(
                            stringResource(R.string.curse_sort_popularity),
                            stringResource(R.string.curse_sort_name),
                            stringResource(R.string.curse_sort_date_created),
                            stringResource(R.string.curse_sort_last_updated),
                            stringResource(R.string.curse_sort_author),
                            stringResource(R.string.curse_sort_total_downloads),
                        ),
                        selectedIndex = holder.sortIndex,
                        onSelectedIndexChange = { holder.sortIndex = it },
                        enabled = !holder.loading,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = holder::onSearchClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !holder.loading,
                colors = ButtonDefaults.buttonColorsPrimary(),
            ) {
                Text(text = stringResource(R.string.search))
            }
            if (tab.hasInstallLocalModpack) {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onImportModpack,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColorsPrimary(),
                ) {
                    Text(text = stringResource(R.string.install_modpack))
                }
            }
        }

        Spacer(Modifier.width(10.dp))

        // ---------- 右栏：分页栏 + 结果列表（对齐 list_layout + recyclerView，70% 宽） ----------
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
                .nestedScroll(barState.nestedScrollConnection),
        ) {
            // 对齐 page_download.xml FCLAppBarLayout auto_tint（ltColor 染色 = primaryContainer）
            // + scroll|enterAlways|snap 折叠（S-P0-2）
            FCLCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .fclCollapsingBar(barState),
                colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (tab.supportChinese && LocaleUtils.isChinese(context)) {
                        IconButton(onClick = {
                            MiuixTranslationDialog(
                                context,
                                holder.repository,
                                holder::onTranslationResult,
                            ).show()
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_translation),
                                contentDescription = null,
                                tint = MiuixTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    // 对齐 page_download.xml page：bg_container_white +
                    // auto_text_background_tint（主色实心 chip = primary）+
                    // auto_text_tint（文字 autoTint = onPrimary）
                    Text(
                        text = stringResource(
                            R.string.search_page_n,
                            holder.pageOffset + 1,
                            if (holder.pageCount == -1) "-" else holder.pageCount.toString(),
                        ),
                        style = MiuixTheme.textStyles.body2,
                        color = MiuixTheme.colorScheme.onPrimary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                MiuixTheme.colorScheme.primary,
                                RoundedCornerShape(5.dp),
                            )
                            .clickable(onClick = holder::onPageClick)
                            .padding(vertical = 10.dp),
                    )
                    PageButton(
                        label = stringResource(R.string.search_previous_page),
                        enabled = holder.pageOffset > 0,
                        onClick = holder::onPreviousPage,
                    )
                    PageButton(
                        label = stringResource(R.string.search_next_page),
                        enabled = holder.pageCount > 1 && holder.pageOffset < holder.pageCount - 1,
                        onClick = holder::onNextPage,
                    )
                    PageButton(
                        label = stringResource(R.string.search_first_page),
                        enabled = holder.pageCount != 0 && holder.pageCount != -1,
                        onClick = holder::onFirstPage,
                    )
                    PageButton(
                        label = stringResource(R.string.search_last_page),
                        enabled = holder.pageCount != 0 && holder.pageCount != -1,
                        onClick = holder::onLastPage,
                    )
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
                        onClick = holder::onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        // 对齐 page_download.xml retry（无 auto_tint）：
                        // 图标保持 drawable 自带 darker_gray 静态着色，不随主题变化
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_refresh_24),
                            contentDescription = null,
                            tint = FCLThemeTokens.StrokeGray,
                        )
                    }

                    else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(holder.results, key = { it.mod.slug + "@" + it.mod.modID }) { item ->
                            RemoteModRow(
                                item = item,
                                onClick = { onOpenModInfo(item.mod, holder.repository, holder.isModrinth) },
                                // 入场动画对齐 RemoteModListAdapter:113（animationSpeed×30）
                                modifier = fclItemEntryModifier(),
                            )
                        }
                    }
                }
            }
        }
    }

    if (holder.showPageJumpDialog) {
        PageJumpDialog(
            onConfirm = {
                holder.showPageJumpDialog = false
                holder.onPageJump(it)
            },
            onDismiss = { holder.showPageJumpDialog = false },
        )
    }
}

@Composable
private fun PageButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    // 对齐 page_download.xml 分页 FCLButton（主色实心 = primary，文字 autoTint = onPrimary）
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(start = 2.dp),
        colors = ButtonDefaults.buttonColorsPrimary(),
    ) {
        Text(text = label, maxLines = 1)
    }
}

/** 搜索结果卡片（对齐 item_remote_mod.xml 两行结构：title+tag 同行、下载量+description 同行）。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RemoteModRow(
    item: RemoteModItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FCLCard(
        onClick = onClick,
        // 按压反馈对齐 item_remote_mod.xml stateListAnimator=anim_scale（按压缩放）→ Sink
        pressFeedbackType = PressFeedbackType.Sink,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        // 对齐 item_remote_mod.xml 的 bg_container_white_clickable + auto_tint（ltColor 染色 = primaryContainer）
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 对齐 item_remote_mod.xml icon：30×30dp
            GlideImage(
                model = item.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                // 对齐 item_remote_mod.xml：条目内全部文本 auto_text_tint（autoTint = onPrimary）
                // 第一行：title + tag 内联（tag marginStart=10dp）
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        style = MiuixTheme.textStyles.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (item.tag.isNotBlank()) {
                        Text(
                            text = item.tag,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MiuixTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f, fill = false),
                        )
                    }
                }
                // 第二行：icon_download + download_count + description 内联（marginStart=5dp）
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 对齐 item_remote_mod.xml icon_download（12×12dp，auto_src_tint = onPrimary）
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_download_24),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MiuixTheme.colorScheme.onPrimary,
                    )
                    Text(
                        text = item.downloadCount,
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 5.dp),
                    )
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MiuixTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .weight(1f),
                    )
                }
            }
        }
    }
}

/** 页码跳转弹窗（对齐 :414-436 的 EditDialog：数字解析、失败静默、越界钳制）。 */
@Composable
private fun PageJumpDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember { mutableStateOf("") }
    FCLDialog(
        show = true,
        onDismissRequest = onDismiss,
        buttons = listOf(
            FCLDialogButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_positive),
                onClick = { onConfirm(input) },
            ),
            FCLDialogButton(
                text = stringResource(com.tungsten.fcllibrary.R.string.dialog_negative),
                onClick = onDismiss,
            ),
        ),
        content = {
            FCLTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        },
    )
}
