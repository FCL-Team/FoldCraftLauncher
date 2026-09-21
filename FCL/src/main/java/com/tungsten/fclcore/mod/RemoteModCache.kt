package com.tungsten.fclcore.mod

import androidx.room.Room
import com.tungsten.fcl.FCLApp
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.gson.JsonUtils
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.Callable
import java.util.logging.Level

/**
 * 模组仓库（CurseForge / Modrinth）远程查询结果缓存。
 * Room key-value 存储（数据库 mod_repository_cache.db），条目按需读写，
 * 无全量加载与全量重写。缓存 API 响应的原始 JSON，命中时由调用方重新走
 * toVersion()/toMod() 转换，减少重复网络请求。
 * json 为 NULL 的条目表示"负缓存"（远程确认未命中，如自制 mod 的指纹），同样防止反复白查。
 * 指纹与单文件按内容寻址永不改变，可长期缓存；详情/版本列表带 TTL 防过期。
 */
object RemoteModCache {

    /** 永不过期 */
    const val TTL_PERMANENT = 0L

    /** 模组详情 */
    const val TTL_DETAIL = 24 * 60 * 60 * 1000L

    /** 版本列表：短过期，避免影响检查更新的新鲜度 */
    const val TTL_VERSIONS = 10 * 60 * 1000L

    /** 分类列表 */
    const val TTL_CATEGORIES = 7 * 24 * 60 * 60 * 1000L

    private const val MAX_ENTRIES = 300
    private const val MAX_TOTAL_BYTES = 10L * 1024 * 1024
    private const val MAX_ENTRY_BYTES = 2L * 1024 * 1024

    @Volatile
    private var database: ModCacheDatabase? = null

    private val dbLock = Any()

    private fun db(): ModCacheDatabase {
        database?.let { return it }
        synchronized(dbLock) {
            database?.let { return it }
            // 旧版缓存（原生 SQLite / 单 JSON 文件，存于 files/cache/）不迁移（缓存可重建），直接删除
            File(FCLPath.FILES_DIR, "cache").deleteRecursively()
            return Room.databaseBuilder(FCLApp.getAppContext(), ModCacheDatabase::class.java, "mod_repository_cache.db")
                .build().also { database = it }
        }
    }

    /**
     * 优先返回未过期的缓存结果，否则执行 fetcher（网络请求）并缓存其结果。
     * fetcher 返回 null 写负缓存；fetcher 抛异常（网络失败）不写任何缓存，直接向上传播。
     * 缓存读写失败仅记录日志并降级为直连网络，不影响查询本身。
     */
    @JvmStatic
    fun <T> getOrFetch(key: String, ttlMillis: Long, type: Type, fetcher: Callable<T>): T {
        val hit = try {
            lookup(key, type)
        } catch (e: Exception) {
            LOG.log(Level.WARNING, "Failed to read mod cache entry: $key", e)
            null
        }
        if (hit != null) {
            LOG.info("Mod cache hit: $key")
            @Suppress("UNCHECKED_CAST")
            return hit.value as T
        }
        val result = fetcher.call()
        try {
            store(key, ttlMillis, if (result == null) null else JsonUtils.GSON.toJson(result, type))
        } catch (e: Exception) {
            LOG.log(Level.WARNING, "Failed to write mod cache entry: $key", e)
        }
        return result
    }

    /** 清空全部缓存条目（设置页"清除模组缓存"入口调用，须在后台线程） */
    @JvmStatic
    fun clear() {
        synchronized(dbLock) {
            database?.clearAllTables()
        }
    }

    /** 命中且未过期时返回缓存值（value 为 null 即负缓存命中），未命中或已过期返回 null；命中刷新 time 实现 LRU */
    private fun lookup(key: String, type: Type): Hit? {
        val dao = db().modCacheDao()
        val entry = dao.lookup(key) ?: return null
        if (entry.ttl != TTL_PERMANENT && System.currentTimeMillis() - entry.time >= entry.ttl) return null
        dao.touch(key, System.currentTimeMillis())
        return Hit(entry.json?.let { JsonUtils.GSON.fromJson<Any>(it, type) })
    }

    private class Hit(val value: Any?)

    private fun store(key: String, ttl: Long, json: String?) {
        if (json != null && json.length > MAX_ENTRY_BYTES) {
            LOG.warning("Mod cache entry too large, skipped: $key")
            return
        }
        val d = db()
        val dao = d.modCacheDao()
        d.runInTransaction {
            dao.upsert(ModCacheEntity(key = key, time = System.currentTimeMillis(), ttl = ttl, json = json))
            evict(dao)
        }
    }

    /** 清理已过期条目；条数与总字节双限制，超限按 time 淘汰最旧（调用方持事务） */
    private fun evict(dao: ModCacheDao) {
        val now = System.currentTimeMillis()
        dao.deleteExpired(now)
        var count = dao.count()
        var totalBytes = dao.totalBytes()
        if (count <= MAX_ENTRIES && totalBytes <= MAX_TOTAL_BYTES) return
        // 从最旧开始淘汰，剩余条目满足双限制即止
        val toDelete = ArrayList<String>()
        for (entry in dao.oldestFirst()) {
            if (count <= MAX_ENTRIES && totalBytes <= MAX_TOTAL_BYTES) break
            totalBytes -= entry.json?.length ?: 0
            count--
            toDelete.add(entry.key)
        }
        if (toDelete.isNotEmpty()) {
            dao.deleteKeys(toDelete)
        }
    }
}
