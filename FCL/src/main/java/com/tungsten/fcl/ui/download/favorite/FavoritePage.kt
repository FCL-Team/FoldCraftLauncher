package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.view.FCLTextView
import com.mio.data.FavoriteManager
import com.mio.data.favorite.DownloadFavoriteEntity
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageDownloadFavoriteBinding
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.download.common.DownloadPage
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.mod.ModLoaderType
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLPage
import com.tungsten.fcllibrary.component.view.FCLButton
import kotlinx.coroutines.launch
import java.util.stream.Collectors

/**
 * 收藏页：展示 CurseForge / Modrinth 资源收藏，支持按资源类别过滤、
 * 一键下载全部收藏模组（匹配当前游戏版本与加载器，经 downloadWithDependencies 解析前置依赖），
 * 左滑取消收藏，点击条目重新拉取详情并进入对应下载页（临时页栈）。
 * 注意：FCLPage 构造器在子类字段赋值前回调 onCreate，故字段均在 onCreate 内初始化。
 */
class FavoritePage(
    context: Context,
    id: Int,
    page: DownloadPage,
) : FCLPage(context, id, R.layout.page_download_favorite) {

    // FCLPage 在超类构造期间回调 onCreate，属性初始化器要等构造完成才执行、会覆盖 onCreate 的赋值，
    // 因此字段一律 lateinit（无初始化器、赋值后保留），仅真正需要空默认的可变标志用普通 var
    private lateinit var binding: PageDownloadFavoriteBinding
    private lateinit var downloadPage: DownloadPage
    private lateinit var adapter: FavoriteAdapter
    private lateinit var allFavorites: List<DownloadFavoriteEntity>
    private var filterType: RemoteModRepository.Type? = null
    private lateinit var filterOptionRows: List<Pair<FCLTextView, RemoteModRepository.Type?>>

    /** 详情拉取/批量下载进行中，防止重复点击 */
    private var loading = false

    init {
        // init 块属于主构造器（可访问构造参数），执行于超类构造之后；lateinit 无初始化器，赋值不会被覆盖
        this.downloadPage = page
    }

    override fun onCreate() {
        super.onCreate()
        binding = PageDownloadFavoriteBinding.bind(contentView)
        adapter = FavoriteAdapter(context, this)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        setupFilter(binding)
        binding.btnDownloadAll.setOnClickListener { startBatchDownload() }
        MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.favorites.collect { favorites ->
                allFavorites = favorites
                applyFilter()
            }
        }
    }

    /** 类别筛选：选项直接展开在左侧面板（面板区域较大，无需下拉），选中项主题色高亮 */
    private fun setupFilter(binding: PageDownloadFavoriteBinding) {
        val options = listOf(
            context.getString(R.string.favorite_filter_all) to null,
            context.getString(R.string.mods) to RemoteModRepository.Type.MOD,
            context.getString(R.string.modpack) to RemoteModRepository.Type.MODPACK,
            context.getString(R.string.resourcepack) to RemoteModRepository.Type.RESOURCE_PACK,
            context.getString(R.string.shaderpack) to RemoteModRepository.Type.SHADER_PACK,
            context.getString(R.string.world) to RemoteModRepository.Type.WORLD,
        )
        val density = context.resources.displayMetrics.density
        filterOptionRows = options.map { (label, type) ->
            val view = FCLTextView(context).apply {
                text = label
                textSize = 14f
                setPadding(0, (density * 8).toInt(), 0, (density * 8).toInt())
                setOnClickListener {
                    if (filterType != type) {
                        filterType = type
                        refreshFilterOptionStates()
                        applyFilter()
                    }
                }
            }
            // 主题变化时刷新选中高亮
            ThemeEngine.getInstance().registerEvent(view) { applyFilterOptionStyle(view, type) }
            binding.filterOptions.addView(view)
            view to type
        }
        refreshFilterOptionStates()
        // 面板背景独立着色为主题浅色（mutate 避免污染共享 drawable），与条目卡片一致且随主题联动
        applyPanelBackground()
    }

    private fun refreshFilterOptionStates() {
        filterOptionRows.forEach { (view, type) -> applyFilterOptionStyle(view, type) }
    }

    private fun applyFilterOptionStyle(view: FCLTextView, type: RemoteModRepository.Type?) {
        val theme = ThemeEngine.getInstance().getTheme()
        if (type == filterType) {
            view.setTextColor(theme.color2)
            view.setTypeface(Typeface.DEFAULT_BOLD)
        } else {
            view.setTextColor(theme.autoTint)
            view.setTypeface(Typeface.DEFAULT)
        }
    }

    private fun applyPanelBackground() {
        // 与条目卡片（FCLConstraintLayout auto_tint）完全相同的着色路径：白底 + 主题浅色 tint
        binding.filterPanel.setBackgroundResource(R.drawable.bg_container_white)
        binding.filterPanel.backgroundTintList =
            ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
    }

    private fun applyFilter() {
        val type = filterType
        val filtered = if (type == null) allFavorites else allFavorites.filter { it.type == type.name }
        adapter.submit(filtered)
        binding.emptyView.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    /** 点击收藏条目：对齐下载页模式 → 按平台 id 重新拉取完整详情 → 打开详情临时页 */
    fun openFavorite(favorite: DownloadFavoriteEntity) {
        if (loading) return
        val type = try {
            RemoteModRepository.Type.valueOf(favorite.type)
        } catch (e: IllegalArgumentException) {
            RemoteModRepository.Type.MOD
        }
        // 模式对齐：下载回调按模式绑定安装目录，推荐版本/分类本地化也按模式生效
        val pageId = pageIdForType(type)
        if (downloadPage.pageId != pageId) {
            downloadPage.switchType(pageId)
        }
        loading = true
        binding.progress.visibility = View.VISIBLE
        Task.supplyAsync { repositoryFor(favorite).getModById(favorite.modId) }
            .whenComplete(Schedulers.androidUIThread()) { mod, exception ->
                loading = false
                binding.progress.visibility = View.GONE
                if (exception == null && mod != null) {
                    downloadPage.openModDetail(mod)
                } else {
                    Toast.makeText(context, context.getString(R.string.favorite_open_failed), Toast.LENGTH_SHORT).show()
                }
            }.start()
    }

    /**
     * 一键下载全部收藏模组：先弹对话框后台解析（按当前游戏版本与加载器匹配各模组最新版本），
     * 解析完成转为勾选列表由用户选择，确认后经 downloadWithDependencies 同款批量流程
     * 下载（解析 REQUIRED 前置、已安装去重、入队下载面板）；无匹配的模组在对话框底部汇总提示。
     */
    private fun startBatchDownload() {
        if (loading) return
        val profile = Profiles.getSelectedProfile()
        val selected = profile.selectedVersion
        if (selected == null) {
            Toast.makeText(context, context.getString(R.string.favorite_batch_no_version), Toast.LENGTH_SHORT).show()
            return
        }
        val mods = allFavorites.filter { it.type == RemoteModRepository.Type.MOD.name }
        if (mods.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.favorite_batch_no_mods), Toast.LENGTH_SHORT).show()
            return
        }
        loading = true
        val dialog = FavoriteBatchDialog(context)
        dialog.showParsing()
        dialog.show()
        Task.supplyAsync {
            val analyzer = LibraryAnalyzer.analyze(
                profile.repository.getResolvedPreservingPatchesVersion(selected),
                selected
            )
            val mcv = analyzer.getVersion(LibraryAnalyzer.LibraryType.MINECRAFT).orElse("")
            val loaders: Set<ModLoaderType> = analyzer.modLoaders
            val entries = mutableListOf<FavoriteBatchDialog.Entry>()
            val skipped = mutableListOf<String>()
            for (favorite in mods) {
                try {
                    val repo = repositoryFor(favorite)
                    val mod = repo.getModById(favorite.modId)
                    val versions = mod.data.loadVersions(repo).collect(Collectors.toList())
                    // 匹配当前游戏版本与加载器；当前版本无加载器（原版）时仅接受无加载器要求的模组，
                    // 同一版本取发布日期最新
                    val match = versions
                        .filter { v -> v.gameVersions.contains(mcv) }
                        .filter { v -> loaders.isEmpty() || v.loaders.any { it in loaders } }
                        .maxByOrNull { it.datePublished?.toEpochMilli() ?: 0L }
                    if (match != null) {
                        entries.add(FavoriteBatchDialog.Entry(favorite.title, match))
                    } else {
                        skipped.add(favorite.title)
                    }
                } catch (e: Exception) {
                    skipped.add(favorite.title)
                }
            }
            Pair(entries, skipped)
        }.whenComplete(Schedulers.androidUIThread()) { result, exception ->
            loading = false
            if (exception != null || result == null) {
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(context, context.getString(R.string.favorite_batch_failed), Toast.LENGTH_SHORT).show()
                return@whenComplete
            }
            val (entries, skipped) = result
            // 解析期间用户已关闭对话框：不再弹出结果
            if (!dialog.isShowing) return@whenComplete
            if (entries.isEmpty()) {
                dialog.dismiss()
                Toast.makeText(context, context.getString(R.string.favorite_batch_none_matched), Toast.LENGTH_SHORT).show()
                return@whenComplete
            }
            dialog.showResult(entries, skipped) { selectedVersions ->
                downloadPage.downloadModsBatch(context, profile, selected, selectedVersions, "mods") { queued, installedSkipped, failed ->
                    when {
                        queued > 0 -> Toast.makeText(
                            context,
                            context.getString(R.string.favorite_batch_queued, queued),
                            Toast.LENGTH_SHORT
                        ).show()

                        installedSkipped > 0 && failed == 0 -> Toast.makeText(
                            context,
                            context.getString(R.string.mods_already_installed),
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> Toast.makeText(
                            context,
                            context.getString(R.string.favorite_batch_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }.start()
    }

    /** 按平台取仓库：getModById 与资源类别无关，直接用静态实例 */
    private fun repositoryFor(favorite: DownloadFavoriteEntity): RemoteModRepository =
        if (favorite.source == FavoriteManager.SOURCE_CURSEFORGE) CurseForgeRemoteModRepository.MODS
        else ModrinthRemoteModRepository.MODS

    private fun pageIdForType(type: RemoteModRepository.Type): Int = when (type) {
        RemoteModRepository.Type.MODPACK -> DownloadUI.PAGE_ID_DOWNLOAD_MODPACK
        RemoteModRepository.Type.RESOURCE_PACK -> DownloadUI.PAGE_ID_DOWNLOAD_RESOURCE_PACK
        RemoteModRepository.Type.SHADER_PACK -> DownloadUI.PAGE_ID_DOWNLOAD_SHADER_PACK
        RemoteModRepository.Type.WORLD -> DownloadUI.PAGE_ID_DOWNLOAD_WORLD
        else -> DownloadUI.PAGE_ID_DOWNLOAD_MOD
    }

    override fun refresh(vararg param: Any?): Task<*>? = null
}
