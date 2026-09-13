package com.mio.data

import android.content.Context
import com.mio.data.favorite.DownloadFavoriteEntity
import com.mio.data.favorite.FavoriteDatabase
import com.tungsten.fcl.FCLApp
import com.tungsten.fclcore.mod.RemoteMod
import com.tungsten.fclcore.mod.RemoteModRepository
import com.tungsten.fclcore.mod.curse.CurseAddon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 下载资源收藏管理：Room 持久化 + 内存镜像（滑动菜单需同步判断"是否已收藏"）。
 * init 后 Room Flow 自动驱动 StateFlow；toggle 写库后立即同步内存，
 * 不等 Flow 回调，避免快速连点读到过期状态。
 */
object FavoriteManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Volatile
    private var initialized = false

    private val _favorites = MutableStateFlow<List<DownloadFavoriteEntity>>(emptyList())

    /** 收藏列表，按收藏时间倒序（最新在前） */
    val favorites: StateFlow<List<DownloadFavoriteEntity>> = _favorites

    @Volatile
    private var favoriteIds: Set<String> = emptySet()

    /** 幂等初始化：启动 Room Flow 收集，在下载 UI 创建时调用 */
    @JvmStatic
    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            initialized = true
            scope.launch {
                FavoriteDatabase.getInstance(context).downloadFavoriteDao().observeAll().collect { list ->
                    applyList(list)
                }
            }
        }
    }

    fun isFavorited(id: String): Boolean = favoriteIds.contains(id)

    /** 切换收藏状态，返回切换后是否已收藏 */
    suspend fun toggle(entity: DownloadFavoriteEntity): Boolean {
        val dao = FavoriteDatabase.getInstance(FCLApp.getAppContext()).downloadFavoriteDao()
        val current = _favorites.value
        return if (favoriteIds.contains(entity.id)) {
            dao.deleteById(entity.id)
            applyList(current.filterNot { it.id == entity.id })
            false
        } else {
            val fav = entity.copy(favoriteTime = System.currentTimeMillis())
            dao.upsert(fav)
            applyList(listOf(fav) + current.filterNot { it.id == entity.id })
            true
        }
    }

    private fun applyList(list: List<DownloadFavoriteEntity>) {
        favoriteIds = list.map { it.id }.toSet()
        _favorites.value = list
    }

    const val SOURCE_CURSEFORGE = "CURSEFORGE"
    const val SOURCE_MODRINTH = "MODRINTH"

    /** 条目 id：平台前缀 + 平台侧项目 id */
    fun idOf(source: String, modId: String): String = "$source:$modId"

    /** 条目平台：与 DownloadPage.repositoryFor 同一约定（data 非 CurseAddon 即 Modrinth） */
    fun sourceOf(mod: RemoteMod): String =
        if (mod.data is CurseAddon) SOURCE_CURSEFORGE else SOURCE_MODRINTH

    /** 搜索结果条目 → 收藏实体（favoriteTime 由 toggle 时写入） */
    fun fromRemoteMod(mod: RemoteMod, type: RemoteModRepository.Type): DownloadFavoriteEntity {
        val source = sourceOf(mod)
        return DownloadFavoriteEntity(
            id = idOf(source, mod.modID),
            source = source,
            type = type.name,
            modId = mod.modID,
            slug = mod.slug,
            title = mod.title,
            description = mod.description,
            iconUrl = mod.iconUrl,
            pageUrl = mod.pageUrl,
            downloadCount = mod.downloadCount,
            categories = mod.categories,
            favoriteTime = 0L,
        )
    }
}
