package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var filterChips: List<Pair<FCLButton, RemoteModRepository.Type?>>

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
        setupFilterBar(binding)
        binding.btnDownloadAll.setOnClickListener { startBatchDownload() }
        MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.favorites.collect { favorites ->
                allFavorites = favorites
                applyFilter()
            }
        }
    }

    private fun setupFilterBar(binding: PageDownloadFavoriteBinding) {
        val chips = listOf(
            binding.chipAll to null,
            binding.chipMod to RemoteModRepository.Type.MOD,
            binding.chipModpack to RemoteModRepository.Type.MODPACK,
            binding.chipResourcepack to RemoteModRepository.Type.RESOURCE_PACK,
            binding.chipShaderpack to RemoteModRepository.Type.SHADER_PACK,
            binding.chipWorld to RemoteModRepository.Type.WORLD,
        )
        filterChips = chips
        chips.forEach { (button, type) ->
            button.setOnClickListener {
                if (filterType != type) {
                    filterType = type
                    updateChipStates()
                    applyFilter()
                }
            }
        }
        updateChipStates()
    }

    private fun updateChipStates() {
        // 浅色主题下半透明按钮的边框几乎不可见，叠加粗体强化选中态对比
        filterChips.forEach { (button, type) ->
            val selected = type == filterType
            button.alpha = if (selected) 1f else 0.35f
            button.setTypeface(if (selected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT)
        }
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
     * 一键下载全部收藏模组：按当前游戏版本与加载器匹配各模组的最新版本，
     * 逐个走 downloadWithDependencies（解析 REQUIRED 前置、已安装去重、入队下载面板）。
     * 无匹配版本/加载器的模组跳过并汇总提示。
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
        binding.progress.visibility = View.VISIBLE
        Task.supplyAsync {
            val analyzer = LibraryAnalyzer.analyze(
                profile.repository.getResolvedPreservingPatchesVersion(selected),
                selected
            )
            val mcv = analyzer.getVersion(LibraryAnalyzer.LibraryType.MINECRAFT).orElse("")
            val loaders: Set<ModLoaderType> = analyzer.modLoaders
            val picks = mutableListOf<RemoteMod.Version>()
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
                        picks.add(match)
                    } else {
                        skipped.add(favorite.title)
                    }
                } catch (e: Exception) {
                    skipped.add(favorite.title)
                }
            }
            Pair(picks, skipped)
        }.whenComplete(Schedulers.androidUIThread()) { result, exception ->
            loading = false
            binding.progress.visibility = View.GONE
            if (exception != null || result == null) {
                Toast.makeText(context, context.getString(R.string.favorite_batch_failed), Toast.LENGTH_SHORT).show()
                return@whenComplete
            }
            val (picks, skipped) = result
            if (picks.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.favorite_batch_none_matched), Toast.LENGTH_SHORT).show()
            } else {
                picks.forEach { downloadPage.downloadWithDependencies(context, profile, selected, it, "mods") }
            }
            if (skipped.isNotEmpty()) {
                Toast.makeText(context, context.getString(R.string.favorite_batch_skipped_note, skipped.size), Toast.LENGTH_SHORT).show()
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
