package com.tungsten.fcl.ui.download.favorite

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.data.FavoriteManager
import com.mio.data.favorite.DownloadFavoriteEntity
import com.tungsten.fcl.R
import com.tungsten.fcl.activity.MainActivity
import com.tungsten.fcl.databinding.PageDownloadFavoriteBinding
import com.tungsten.fcl.ui.download.DownloadUI
import com.tungsten.fcl.ui.download.common.DownloadPage
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.ui.FCLPage
import kotlinx.coroutines.launch

/**
 * 收藏页：展示 CurseForge / Modrinth 资源收藏，左滑取消收藏，
 * 点击条目重新拉取详情并进入对应下载页（临时页栈）。
 * 注意：FCLPage 构造器在子类字段赋值前回调 onCreate，故字段均在 onCreate 内初始化。
 */
class FavoritePage(
    context: Context,
    id: Int,
    downloadPage: DownloadPage,
) : FCLPage(context, id, R.layout.page_download_favorite) {

    private var binding: PageDownloadFavoriteBinding? = null
    private var downloadPage: DownloadPage? = downloadPage
    private var adapter: FavoriteAdapter? = null

    /** 详情拉取中，防止重复点击 */
    private var loading = false

    override fun onCreate() {
        super.onCreate()
        val binding = PageDownloadFavoriteBinding.bind(contentView)
        this.binding = binding
        val adapter = FavoriteAdapter(context, this)
        this.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        MainActivity.getInstance().lifecycleScope.launch {
            FavoriteManager.favorites.collect { list ->
                adapter.submit(list)
                binding.emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /** 点击收藏条目：对齐下载页模式 → 按平台 id 重新拉取完整详情 → 打开详情临时页 */
    fun openFavorite(favorite: DownloadFavoriteEntity) {
        if (loading) return
        val page = downloadPage ?: return
        val type = try {
            RemoteModRepository.Type.valueOf(favorite.type)
        } catch (e: IllegalArgumentException) {
            RemoteModRepository.Type.MOD
        }
        // 模式对齐：下载回调按模式绑定安装目录，推荐版本/分类本地化也按模式生效
        val pageId = pageIdForType(type)
        if (page.pageId != pageId) {
            page.switchType(pageId)
        }
        loading = true
        binding?.progress?.visibility = View.VISIBLE
        Task.supplyAsync { repositoryFor(favorite).getModById(favorite.modId) }
            .whenComplete(Schedulers.androidUIThread()) { mod, exception ->
                loading = false
                binding?.progress?.visibility = View.GONE
                if (exception == null && mod != null) {
                    page.openModDetail(mod)
                } else {
                    Toast.makeText(context, context.getString(R.string.favorite_open_failed), Toast.LENGTH_SHORT).show()
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
