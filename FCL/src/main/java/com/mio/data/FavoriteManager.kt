package com.mio.data

import android.content.Context
import com.mio.data.favorite.DownloadFavoriteEntity
import com.mio.data.favorite.FavoriteDatabase
import com.mio.data.favorite.FavoriteGroupEntity
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

/**
 * 下载资源收藏管理：Room 持久化 + 内存镜像（滑动菜单需同步判断"是否已收藏"）。
 * init 后 Room Flow 自动驱动 StateFlow；toggle 写库后立即同步内存，
 * 不等 Flow 回调，避免快速连点读到过期状态。
 * 分组管理：分组定义存 favorite_groups 表，收藏条目经 groups 字段引用分组 id。
 */
object FavoriteManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Volatile
    private var initialized = false

    private val _favorites = MutableStateFlow<List<DownloadFavoriteEntity>>(emptyList())

    /** 收藏列表，按收藏时间倒序（最新在前） */
    val favorites: StateFlow<List<DownloadFavoriteEntity>> = _favorites

    private val _groups = MutableStateFlow<List<FavoriteGroupEntity>>(emptyList())

    /** 自定义分组列表，按创建时间正序 */
    val groups: StateFlow<List<FavoriteGroupEntity>> = _groups

    @Volatile
    private var favoriteIds: Set<String> = emptySet()

    /** 分组新建/重命名的并发互斥：双击确认等场景下同名查重与写入串行化，避免重复创建 */
    private val groupMutex = Mutex()

    /** 幂等初始化：启动 Room Flow 收集，在下载 UI 创建时调用 */
    @JvmStatic
    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            initialized = true
            val db = FavoriteDatabase.getInstance(context)
            scope.launch {
                db.downloadFavoriteDao().observeAll().collect { list ->
                    applyList(list)
                }
            }
            scope.launch {
                db.favoriteGroupDao().observeAll().collect { list ->
                    _groups.value = list
                }
            }
        }
    }

    @JvmStatic
    fun isFavorited(id: String): Boolean = favoriteIds.contains(id)

    /** 切换收藏状态，返回切换后是否已收藏；收藏时可携带所属分组 */
    suspend fun toggle(entity: DownloadFavoriteEntity, groups: List<String> = emptyList()): Boolean {
        val dao = FavoriteDatabase.getInstance(FCLApp.getAppContext()).downloadFavoriteDao()
        val current = _favorites.value
        return if (favoriteIds.contains(entity.id)) {
            dao.deleteById(entity.id)
            applyList(current.filterNot { it.id == entity.id })
            false
        } else {
            val fav = entity.copy(favoriteTime = System.currentTimeMillis(), groups = groups)
            dao.upsert(fav)
            applyList(listOf(fav) + current.filterNot { it.id == entity.id })
            true
        }
    }

    /** 修改收藏条目所属分组 */
    suspend fun setGroups(favoriteId: String, groupIds: List<String>) {
        val updated = _favorites.value.firstOrNull { it.id == favoriteId }?.copy(groups = groupIds) ?: return
        FavoriteDatabase.getInstance(FCLApp.getAppContext()).downloadFavoriteDao().upsert(updated)
        applyList(_favorites.value.map { if (it.id == favoriteId) updated else it })
    }

    /** 新建分组（同名复用既有分组），返回分组实体 */
    suspend fun createGroup(name: String): FavoriteGroupEntity = groupMutex.withLock {
        val trimmed = name.trim()
        _groups.value.firstOrNull { it.name == trimmed }?.let { return it }
        val group = FavoriteGroupEntity(
            groupId = UUID.randomUUID().toString(),
            name = trimmed,
            createTime = System.currentTimeMillis(),
        )
        FavoriteDatabase.getInstance(FCLApp.getAppContext()).favoriteGroupDao().upsert(group)
        _groups.value = _groups.value + group
        group
    }

    /** 重命名分组，重名时放弃返回 false */
    suspend fun renameGroup(groupId: String, newName: String): Boolean = groupMutex.withLock {
        val trimmed = newName.trim()
        if (_groups.value.any { it.name == trimmed && it.groupId != groupId }) return@withLock false
        val group = _groups.value.firstOrNull { it.groupId == groupId } ?: return@withLock false
        val renamed = group.copy(name = trimmed)
        FavoriteDatabase.getInstance(FCLApp.getAppContext()).favoriteGroupDao().upsert(renamed)
        _groups.value = _groups.value.map { if (it.groupId == groupId) renamed else it }
        true
    }

    /** 删除分组：仅摘除收藏条目上的分组标记，不取消收藏 */
    suspend fun deleteGroup(groupId: String) {
        val db = FavoriteDatabase.getInstance(FCLApp.getAppContext())
        db.favoriteGroupDao().deleteById(groupId)
        _groups.value = _groups.value.filterNot { it.groupId == groupId }
        val affected = _favorites.value.filter { groupId in it.groups }
        for (favorite in affected) {
            db.downloadFavoriteDao().upsert(favorite.copy(groups = favorite.groups - groupId))
        }
        if (affected.isNotEmpty()) {
            val idSet = affected.map { it.id }.toSet()
            applyList(_favorites.value.map { if (it.id in idSet) it.copy(groups = it.groups - groupId) else it })
        }
    }

    /** 批量取消收藏 */
    suspend fun removeFavorites(favoriteIds: Collection<String>) {
        val dao = FavoriteDatabase.getInstance(FCLApp.getAppContext()).downloadFavoriteDao()
        favoriteIds.forEach { dao.deleteById(it) }
        applyList(_favorites.value.filterNot { it.id in favoriteIds })
    }

    /** 批量设置收藏条目所属分组（覆盖原分组） */
    suspend fun setGroupsBulk(favoriteIds: Collection<String>, groupIds: List<String>) {
        val db = FavoriteDatabase.getInstance(FCLApp.getAppContext())
        val idSet = favoriteIds.toSet()
        val updated = _favorites.value.map { if (it.id in idSet) it.copy(groups = groupIds) else it }
        updated.filter { it.id in idSet }.forEach { db.downloadFavoriteDao().upsert(it) }
        applyList(updated)
    }

    private fun applyList(list: List<DownloadFavoriteEntity>) {
        favoriteIds = list.map { it.id }.toSet()
        _favorites.value = list
    }

    const val SOURCE_CURSEFORGE = "CURSEFORGE"
    const val SOURCE_MODRINTH = "MODRINTH"

    /** 条目 id：平台前缀 + 平台侧项目 id */
    @JvmStatic
    fun idOf(source: String, modId: String): String = "$source:$modId"

    /** 条目平台：与 DownloadPage.repositoryFor 同一约定（data 非 CurseAddon 即 Modrinth） */
    @JvmStatic
    fun sourceOf(mod: RemoteMod): String =
        if (mod.data is CurseAddon) SOURCE_CURSEFORGE else SOURCE_MODRINTH

    /** 搜索结果条目 → 收藏实体（favoriteTime 由 toggle 时写入） */
    @JvmStatic
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
