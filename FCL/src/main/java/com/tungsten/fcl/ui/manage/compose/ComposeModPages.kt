package com.tungsten.fcl.ui.manage.compose

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.bridge.LegacyBridge
import com.tungsten.fcl.ui.compose.FCLCard
import com.tungsten.fcl.ui.compose.FCLDialogs
import com.tungsten.fcl.ui.compose.FCLTextField
import com.tungsten.fcl.ui.compose.MiuixTaskDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixModInfoDialog
import com.tungsten.fcl.ui.compose.dialog.MiuixModRollbackDialog
import com.tungsten.fcl.ui.download.DownloadPageManager
import com.tungsten.fcl.ui.download.compose.ComposeDownloadPage
import com.tungsten.fcl.ui.download.compose.ComposeTempPage
import com.tungsten.fcl.ui.manage.ManagePageManager
import com.tungsten.fcl.ui.manage.ManageUI
import com.tungsten.fcl.ui.manage.ModCheckUpdatesTask
import com.tungsten.fcl.ui.manage.ModListPage.ModInfoObject
import com.tungsten.fcl.ui.manage.ModUpdatesPage
import com.tungsten.fcl.util.AndroidUtils
import com.tungsten.fcl.util.NavigationBus
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.LocalModFile
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.StringUtils
import com.tungsten.fclcore.util.io.CSVTable
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout
import com.tungsten.fcllibrary.util.LocaleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.logging.Level
import java.util.regex.Pattern

/**
 * 管理域 Mod 列表页（对齐 ModListPage + page_manage_mod.xml + item_local_mod.xml）。
 * 旧 ModListPage/LocalModListAdapter 与 XML 全部保留，由
 * ManagePageManager.USE_COMPOSE_MOD_PAGES 开关双分支回滚。
 * 数据模型直接复用 ModListPage.ModInfoObject，搜索/筛选/启停/删除/更新检查/
 * 回滚/跳转下载页等行为与旧实现逐项对齐。
 */
class ComposeModListPage(context: Context, id: Int, parent: FCLUILayout) :
    FCLCommonPage(context, id, parent, R.layout.page_compose_container), ManageUI.VersionLoadable {

    private var composeInstalled = false

    private val moddedState = mutableStateOf(false)
    private val modManagerState = mutableStateOf<ModManager?>(null)
    private var profile: Profile? = null
    private var versionId: String? = null
    private val reloadTick = mutableIntStateOf(0)

    override fun onStart() {
        if (!composeInstalled) {
            composeInstalled = true
            findViewById<FrameLayout>(R.id.compose_container).addView(
                LegacyBridge.createComposeView(context) { Content(); LegacyBridge.LegacyDialogHost() },
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        super.onStart()
    }

    override fun loadVersion(profile: Profile, version: String?) {
        // 对齐旧 loadVersion：分析 ModLoader → 置 modded → 加载 Mod 列表
        this.profile = profile
        this.versionId = version
        val repository = profile.repository
        val resolved = repository.getResolvedPreservingPatchesVersion(versionId)
        val libraryAnalyzer = LibraryAnalyzer.analyze(resolved, repository.getGameVersion(resolved).orElse(null))
        moddedState.value = libraryAnalyzer.hasModLoader()
        modManagerState.value = repository.getModManager(version)
        reloadTick.intValue++
    }

    /** 对齐旧页 public refresh()（ModUpdatesPage 更新完成后调用）。 */
    fun refreshMods() {
        reloadTick.intValue++
    }

    override fun refresh(vararg param: Any?): Task<*>? = null

    @Composable
    private fun Content() {
        val context = getContext()
        val scope = rememberCoroutineScope()
        // 对齐旧 setEnable(false)：未装 ModLoader 时只显示 warning，隐藏左右栏
        if (!moddedState.value) {
            Column(Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
                FCLCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
                ) {
                    Text(
                        text = stringResource(R.string.mods_not_modded),
                        modifier = Modifier.padding(10.dp),
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
            }
            return
        }

        val modManager = modManagerState.value
        val tick = reloadTick.intValue
        var loading by remember { mutableStateOf(false) }
        var allItems by remember { mutableStateOf<List<ModInfoObject>>(emptyList()) }
        // page_manage_mod.xml：enabled/disabled 默认 checked=true
        var enabledFilter by remember { mutableStateOf(true) }
        var disabledFilter by remember { mutableStateOf(true) }
        // 对齐 calculateMod：启用/禁用计数；异常时退化为纯文案（null）
        var counts by remember { mutableStateOf<Pair<Int, Int>?>(null) }
        var query by remember { mutableStateOf("") }
        val selected = remember { mutableStateListOf<ModInfoObject>() }

        fun recalcCounts() {
            counts = try {
                val mods = modManagerState.value?.mods ?: emptyList()
                val active = mods.count { it.isActive }
                active to (mods.size - active)
            } catch (e: Throwable) {
                null
            }
        }

        fun reload() {
            reloadTick.intValue++
        }

        // 对齐旧 loadMods：切版本/刷新/切换过滤时清空选择与搜索，后台 refreshMods 后按启停过滤
        LaunchedEffect(modManager, tick, enabledFilter, disabledFilter) {
            val mm = modManager ?: return@LaunchedEffect
            selected.clear()
            query = ""
            loading = true
            val result = withContext(Dispatchers.IO) {
                try {
                    synchronized(this@ComposeModListPage) {
                        mm.refreshMods()
                        mm.mods.map { ModInfoObject(context, it) }
                    }
                } catch (e: Throwable) {
                    Logging.LOG.log(Level.SEVERE, "Failed to load local mod list", e)
                    null
                }
            }
            loading = false
            if (result != null) {
                recalcCounts()
                allItems = result.filter {
                    (enabledFilter && it.modInfo.isActive) || (disabledFilter && !it.modInfo.isActive)
                }
            }
        }

        // 对齐旧 search()：输入即清空选择；空串显示全部；regex: 前缀走正则，非法正则列表清空
        LaunchedEffect(query) { selected.clear() }
        val displayed = remember(allItems, query) {
            if (StringUtils.isBlank(query)) {
                allItems
            } else {
                val predicate: (String) -> Boolean = if (query.startsWith("regex:")) {
                    try {
                        val pattern = Pattern.compile(query.removePrefix("regex:"))
                        ({ s: String -> pattern.matcher(s).find() })
                    } catch (e: Throwable) {
                        Logging.LOG.log(Level.WARNING, "Illegal regular expression", e)
                        ({ false })
                    }
                } else {
                    val lower = query.lowercase(Locale.ROOT)
                    ({ s: String -> s.lowercase(Locale.ROOT).contains(lower) })
                }
                allItems.filter {
                    predicate(it.modInfo.fileName) || (it.remoteMod != null && predicate(it.remoteMod.title))
                }
            }
        }

        fun removeSelected() {
            val mm = modManager ?: return
            try {
                mm.removeMods(*selected.map { it.modInfo }.toTypedArray())
                reload()
            } catch (e: IOException) {
                // 与旧实现一致：游戏运行中或文件缺失时删除失败静默忽略
            }
        }

        fun addMods() {
            val mm = modManager ?: return
            val suffix = arrayListOf(".jar", ".zip", ".litemod")
            MainActivity.getInstance().fileLauncher.launchMultiSelection(null, suffix) { files ->
                if (files == null) return@launchMultiSelection
                val res = files.map { Uri.parse(it) }.filterNotNull().map<Uri, Any> { uri ->
                    if (AndroidUtils.isDocUri(uri)) uri else File(uri.toString())
                }
                val succeeded = ArrayList<String>(res.size)
                val failed = ArrayList<String>()
                scope.launch {
                    withContext(Dispatchers.IO) {
                        for (obj in res) {
                            if (obj is File) {
                                try {
                                    mm.addMod(obj.toPath())
                                    succeeded.add(obj.name)
                                } catch (e: Exception) {
                                    Logging.LOG.log(Level.WARNING, "Unable to add mod $obj", e)
                                    failed.add(obj.name)
                                }
                            } else if (obj is Uri) {
                                try {
                                    val name = AndroidUtils.getFileName(getActivity(), obj)
                                    mm.addMod(getActivity(), obj, name)
                                    succeeded.add(name)
                                } catch (e: Exception) {
                                    Logging.LOG.log(Level.WARNING, "Unable to add mod $obj", e)
                                    failed.add(obj.toString())
                                }
                            }
                        }
                    }
                    val prompt = ArrayList<String>(1)
                    if (succeeded.isNotEmpty())
                        prompt.add(AndroidUtils.getLocalizedText(context, "mods_add_success", succeeded.joinToString(", ")))
                    if (failed.isNotEmpty())
                        prompt.add(AndroidUtils.getLocalizedText(context, "mods_add_failed", failed.joinToString(", ")))
                    FCLDialogs.showAlert(
                        context,
                        context.getString(R.string.mods_add),
                        prompt.joinToString("\n"),
                        cancelable = false,
                    )
                    reload()
                }
            }
        }

        fun checkUpdates(isSelected: Boolean) {
            val mm = modManager ?: return
            val p = profile ?: return
            val action = {
                val dialog = MiuixTaskDialog(context)
                dialog.setTitle(context.getString(R.string.update_checking))
                val task = Task.composeAsync<List<LocalModFile.ModUpdate>> {
                    val gameVersion = p.repository.getGameVersion(versionId)
                    if (gameVersion.isPresent) {
                        if (isSelected) ModCheckUpdatesTask(gameVersion.get(), selected.map { it.modInfo })
                        else ModCheckUpdatesTask(gameVersion.get(), mm.mods)
                    } else null
                }.whenComplete(Schedulers.androidUIThread()) { result, exception ->
                    when {
                        exception is CancellationException -> Unit
                        exception != null || result == null -> FCLDialogs.showAlert(
                            context,
                            context.getString(R.string.message_failed),
                            "Failed to check updates",
                            cancelable = false,
                        )
                        result.isEmpty() -> FCLDialogs.showAlert(
                            context,
                            null,
                            context.getString(R.string.mods_check_updates_empty),
                            cancelable = false,
                        )
                        else -> ManagePageManager.instance?.showTempPage(
                            ComposeModUpdatesPage(context, PageManager.PAGE_ID_TEMP, parent, this@ComposeModListPage, mm, result)
                        )
                    }
                }.withStagesHint(listOf("mods.check_updates"))
                val executor = task.executor()
                dialog.setExecutor(executor)
                dialog.show()
                executor.start()
            }
            // 对齐旧实现：整合包版本先弹警告再执行
            if (p.repository.isModpack(versionId)) {
                FCLDialogs.showAlert(
                    context,
                    null,
                    context.getString(R.string.mods_update_modpack_mod_warning),
                    negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                    onResult = { if (it) action() },
                    cancelable = false,
                )
            } else {
                action()
            }
        }

        fun confirmDelete() {
            FCLDialogs.showAlert(
                context,
                null,
                context.getString(R.string.button_remove_confirm),
                positiveText = context.getString(R.string.button_remove),
                negativeText = context.getString(com.tungsten.fcllibrary.R.string.dialog_negative),
                onResult = { if (it) removeSelected() },
                cancelable = false,
            )
        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 8.dp)
        ) {
            // 左栏（对齐 left ScrollView，constraintWidth_percent=0.3）
            Column(
                Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        state = if (enabledFilter) ToggleableState.On else ToggleableState.Off,
                        onClick = { enabledFilter = !enabledFilter },
                    )
                    Text(
                        text = stringResource(R.string.enabled) + (counts?.let { " (${it.first})" } ?: ""),
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        state = if (disabledFilter) ToggleableState.On else ToggleableState.Off,
                        onClick = { disabledFilter = !disabledFilter },
                    )
                    Text(
                        text = stringResource(R.string.disabled) + (counts?.let { " (${it.second})" } ?: ""),
                        color = MiuixTheme.colorScheme.onPrimary,
                    )
                }
                if (selected.isEmpty()) {
                    // normal_layout
                    ModActionButton(R.string.mods_add, !loading) { addMods() }
                    ModActionButton(R.string.mods_check_updates, !loading) { checkUpdates(false) }
                    ModActionButton(R.string.action_refresh, !loading) { reload() }
                } else {
                    // selected_layout
                    ModActionButton(R.string.mods_check_updates, !loading) { checkUpdates(true) }
                    ModActionButton(R.string.button_remove, !loading) { confirmDelete() }
                    ModActionButton(R.string.button_select_all, !loading) {
                        selected.clear()
                        selected.addAll(displayed)
                    }
                    ModActionButton(R.string.button_select_invert, !loading) {
                        val inverted = displayed.filter { !selected.contains(it) }
                        selected.clear()
                        selected.addAll(inverted)
                    }
                    ModActionButton(R.string.button_cancel, !loading) { selected.clear() }
                }
            }
            // 右栏（对齐 right：搜索栏 + 列表/进度）
            Column(
                Modifier
                    .weight(7f)
                    .fillMaxHeight()
                    .padding(start = 10.dp)
            ) {
                FCLTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.search),
                    useLabelAsPlaceholder = true,
                    singleLine = true,
                    enabled = !loading,
                )
                if (loading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 5.dp)
                    ) {
                        items(displayed) { item ->
                            ModListItemRow(
                                context = context,
                                item = item,
                                selected = selected.contains(item),
                                onToggleSelect = {
                                    if (selected.contains(item)) selected.remove(item) else selected.add(item)
                                },
                                onActiveChanged = { recalcCounts() },
                                onRollback = { from, to -> rollbackMod(modManager, from, to) },
                                onJump = { jumpToModPage(it) },
                            )
                        }
                    }
                }
            }
        }
    }

    /** 对齐旧 rollback()：失败 Toast，成功刷新列表。 */
    private fun rollbackMod(modManager: ModManager?, from: LocalModFile, to: LocalModFile?) {
        if (modManager == null || to == null) return
        try {
            modManager.rollback(from, to)
            reloadTick.intValue++
        } catch (e: IOException) {
            Toast.makeText(context, context.getString(R.string.message_failed), Toast.LENGTH_SHORT).show()
        }
    }

    /** 对齐 LocalModListAdapter jump：切到下载 UI 的 Mod 子页并打开该 Mod 详情。 */
    private fun jumpToModPage(item: ModInfoObject) {
        val uiManager = MainActivity.getInstance().uiManager
        NavigationBus.select(NavigationBus.Menu.DOWNLOAD)
        uiManager.downloadUI.runAfterInit {
            uiManager.downloadUI.tabLayout.selectTab(uiManager.downloadUI.tabLayout.getTabAt(2))
            uiManager.downloadUI.pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD)
            val downloadPage =
                uiManager.downloadUI.pageManager.getPageById(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD) as ComposeDownloadPage
            downloadPage.jumpToModPage(item.remoteMod)
        }
    }

    @Composable
    private fun ModActionButton(textRes: Int, enabled: Boolean, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            Text(stringResource(textRes))
        }
    }
}

/** 对齐 item_local_mod.xml 的单行：勾选框 + 图标 + 名称/标签/描述 + 回滚/详情/跳转按钮。 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ModListItemRow(
    context: Context,
    item: ModInfoObject,
    selected: Boolean,
    onToggleSelect: () -> Unit,
    onActiveChanged: () -> Unit,
    onRollback: (LocalModFile, LocalModFile?) -> Unit,
    onJump: (ModInfoObject) -> Unit,
) {
    // 对齐 LocalModListAdapter：后台解析远程 Mod（>100MB 跳过），成功后替换图标/名称并显示跳转按钮
    var remoteMod by remember(item) { mutableStateOf(item.remoteMod) }
    LaunchedEffect(item) {
        if (remoteMod != null) return@LaunchedEffect
        remoteMod = withContext(Dispatchers.IO) {
            if (item.modInfo.file.toFile().length() > 104857600) return@withContext null
            for (type in RemoteMod.Type.entries) {
                try {
                    if (item.remoteMod == null) {
                        val remoteVersion =
                            type.remoteModRepository.getRemoteVersionByLocalFile(item.modInfo, item.modInfo.file)
                        if (remoteVersion.isPresent) {
                            val version = remoteVersion.get()
                            if (version != null) {
                                item.modInfo.remoteVersion = version
                                item.remoteMod = type.remoteModRepository.getModById(version.modid)
                            } else {
                                continue
                            }
                        } else {
                            continue
                        }
                    }
                    return@withContext item.remoteMod
                } catch (e: Throwable) {
                    System.gc()
                    Logging.LOG.log(Level.SEVERE, e.toString())
                }
            }
            null
        }
    }

    val translation = item.mod
    val isChinese = translation != null && LocaleUtils.isChinese(context)
    // 对齐 adapter：解析出远程 Mod 后标题换为远程标题，中文翻译存在时前置 [译名]
    val title = remoteMod?.let { mod ->
        if (isChinese && translation!!.name.isNotEmpty() && StringUtils.containsChinese(translation.name)) {
            "[${translation.name}]${mod.title}"
        } else {
            mod.title
        }
    } ?: item.title
    // 对齐 adapter getTag：加载器名 + 中文译名（仅中文环境）
    val tag = buildString {
        append(modLoaderText(context, item.modInfo.modLoaderType))
        if (isChinese) {
            if (isNotEmpty()) append("   ")
            append(translation!!.displayName)
        }
    }
    val active by item.active.collectAsState()
    val contentColor = MiuixTheme.colorScheme.onPrimary

    FCLCard(
        onClick = onToggleSelect,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        // 对齐旧条目背景染色：选中 = theme.color（primary），未选中 = ltColor（primaryContainer）
        colors = CardDefaults.defaultColors(
            color = if (selected) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.primaryContainer
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                state = if (active) ToggleableState.On else ToggleableState.Off,
                onClick = {
                    item.modInfo.setActive(!active)
                    onActiveChanged()
                },
                modifier = Modifier.size(30.dp),
            )
            if (remoteMod?.iconUrl != null) {
                GlideImage(
                    model = remoteMod?.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            } else {
                // 对齐 adapter 默认图标：ic_cube 染 theme.color（primary）
                Icon(
                    painter = painterResource(R.drawable.ic_cube),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MiuixTheme.colorScheme.primary,
                )
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (tag.isNotEmpty()) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (item.modInfo.mod.oldFiles.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_restore_24),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.clickable {
                        MiuixModRollbackDialog(
                            context,
                            item.modInfo.mod.oldFiles.filterNotNull(),
                        ) { chosen -> onRollback(item.modInfo, chosen) }.show()
                    },
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_outline_info_24),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.clickable {
                    MiuixModInfoDialog(context, item.modInfo).show()
                },
            )
            if (remoteMod != null) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.clickable { onJump(item) },
                )
            }
        }
    }
}

private fun modLoaderText(context: Context, type: ModLoaderType): String = when (type) {
    ModLoaderType.FORGE -> context.getString(R.string.install_installer_forge)
    ModLoaderType.NEO_FORGED -> context.getString(R.string.install_installer_neoforge)
    ModLoaderType.FABRIC -> context.getString(R.string.install_installer_fabric)
    ModLoaderType.LITE_LOADER -> context.getString(R.string.install_installer_liteloader)
    ModLoaderType.QUILT -> context.getString(R.string.install_installer_quilt)
    else -> ""
}

/**
 * Mod 更新结果页（对齐 ModUpdatesPage + page_mod_update.xml + item_update_mod.xml）。
 * 数据模型与更新任务直接复用 ModUpdatesPage.ModUpdateObject/ModUpdateTask。
 * 旧 update_without 按钮的 selected 高亮为旧 FCLButton 染色细节，Compose 侧不再复制。
 */
class ComposeModUpdatesPage(
    context: Context,
    id: Int,
    parent: FCLUILayout,
    private val modListPage: ComposeModListPage,
    private val modManager: ModManager,
    updates: List<LocalModFile.ModUpdate>,
) : ComposeTempPage(context, id, parent) {

    private val objects: List<ModUpdatesPage.ModUpdateObject> =
        updates.map { ModUpdatesPage.ModUpdateObject(context, it) }

    @Composable
    override fun Content() {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 10.dp)
            ) {
                items(objects) { obj -> ModUpdateRow(obj) }
            }
            Row(Modifier.fillMaxWidth()) {
                Button(
                    onClick = { exportList() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 7.dp),
                ) { Text(stringResource(R.string.button_export)) }
                Button(
                    onClick = { updateMods(true) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 3.dp, end = 3.dp),
                ) { Text(stringResource(R.string.mods_check_updates_update), maxLines = 1, overflow = TextOverflow.Ellipsis) }
                Button(
                    onClick = { updateMods(false) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 3.dp, end = 3.dp),
                ) { Text(stringResource(R.string.mods_check_updates_update_without), maxLines = 1, overflow = TextOverflow.Ellipsis) }
                Button(
                    onClick = { ManagePageManager.instance?.dismissCurrentTempPage() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 7.dp),
                ) { Text(stringResource(R.string.button_cancel)) }
            }
        }
    }

    /** 对齐旧 updateMods：逐 Mod 下载新版本，结束后关页刷新；失败列表与成功提示均保留。 */
    private fun updateMods(keepOldVersion: Boolean) {
        val task = ModUpdatesPage.ModUpdateTask(
            modManager,
            objects.filter { it.isEnabled }
                .map { com.tungsten.fclcore.util.Pair.pair(it.data.localMod, it.data.candidates[0]) },
            keepOldVersion,
        )
        val dialog = MiuixTaskDialog(context)
        dialog.setTitle(context.getString(R.string.mods_check_updates_update))
        val executor = task.whenComplete(Schedulers.androidUIThread()) { exception ->
            ManagePageManager.instance?.dismissCurrentTempPage()
            modListPage.refreshMods()
            if (task.failedMods.isNotEmpty()) {
                FCLDialogs.showAlert(
                    context,
                    context.getString(R.string.install_failed),
                    context.getString(R.string.mods_check_updates_failed) + "\n" +
                        task.failedMods.joinToString("\n") { it.fileName },
                    cancelable = false,
                )
            }
            if (exception == null) {
                FCLDialogs.showAlert(context, null, context.getString(R.string.install_success), cancelable = false)
            }
        }.executor()
        dialog.setExecutor(executor)
        dialog.show()
        executor.start()
    }

    /** 对齐旧 exportList：导出 CSV 到外部存储 FCL 目录，弹窗显示结果路径或错误。 */
    private fun exportList() {
        val path = File(
            Environment.getExternalStorageDirectory().absolutePath + "/FCL",
            "fcl-mod-update-list-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss")) + ".csv",
        ).toPath()
        val dialog = MiuixTaskDialog(context)
        dialog.setTitle(context.getString(R.string.button_export))
        val executor = Task.runAsync {
            val csvTable = CSVTable.createEmpty()
            csvTable.set(0, 0, "Source File Name")
            csvTable.set(1, 0, "Current Version")
            csvTable.set(2, 0, "Target Version")
            csvTable.set(3, 0, "Update Source")
            for (i in objects.indices) {
                csvTable.set(0, i + 1, objects[i].fileName)
                csvTable.set(1, i + 1, objects[i].currentVersion)
                csvTable.set(2, i + 1, objects[i].targetVersion)
                csvTable.set(3, i + 1, objects[i].source)
            }
            csvTable.write(Files.newOutputStream(path))
        }.whenComplete(Schedulers.androidUIThread()) { exception ->
            if (exception == null) {
                FCLDialogs.showAlert(context, context.getString(R.string.message_success), path.toString(), cancelable = false)
            } else {
                FCLDialogs.showAlert(context, context.getString(R.string.message_error), exception.message, cancelable = false)
            }
        }.executor()
        dialog.setExecutor(executor)
        dialog.show()
        executor.start()
    }
}

/** 对齐 item_update_mod.xml：勾选框 + 文件名/来源/版本变化。 */
@Composable
private fun ModUpdateRow(obj: ModUpdatesPage.ModUpdateObject) {
    val enabled by obj.enabledFlow().collectAsState()
    val contentColor = MiuixTheme.colorScheme.onPrimary
    FCLCard(
        onClick = { obj.setEnabled(!enabled) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                state = if (enabled) ToggleableState.On else ToggleableState.Off,
                onClick = { obj.setEnabled(!enabled) },
                modifier = Modifier.size(30.dp),
            )
            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = obj.fileName,
                    fontSize = 14.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = obj.source,
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${obj.currentVersion}  ->  ${obj.targetVersion}",
                    fontSize = 12.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
