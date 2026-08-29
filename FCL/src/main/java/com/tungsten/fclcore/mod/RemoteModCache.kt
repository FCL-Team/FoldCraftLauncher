package com.tungsten.fclcore.mod

import android.database.sqlite.SQLiteDatabase
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.gson.JsonUtils
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.Callable
import java.util.logging.Level

/**
 * 模组仓库（CurseForge / Modrinth）远程查询结果缓存。
 * SQLite key-value 存储（files/cache/mod_repository_cache.db），条目按需读写，
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

    private const val TABLE = "cache"

    @Volatile
    private var db: SQLiteDatabase? = null

    private val dbLock = Any()

    // 存 files 目录：CACHE_DIR 会被周期性清理，指纹类永久缓存会被误删
    private fun dbFile(): File = File(File(FCLPath.FILES_DIR, "cache"), "mod_repository_cache.db")

    private fun db(): SQLiteDatabase {
        db?.let { return it }
        synchronized(dbLock) {
            db?.let { return it }
            val file = dbFile()
            file.parentFile?.mkdirs()
            // 旧版单 JSON 文件缓存不迁移（缓存可重建），直接删除
            File(file.parentFile, "mod_repository_cache.json").delete()
            return SQLiteDatabase.openOrCreateDatabase(file, null).also {
                it.execSQL(
                    "CREATE TABLE IF NOT EXISTS $TABLE (" +
                            "key TEXT PRIMARY KEY NOT NULL, time INTEGER NOT NULL, " +
                            "ttl INTEGER NOT NULL, json TEXT)"
                )
                db = it
            }
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

    /** 清空并删除缓存库（设置页"清除模组缓存"入口调用，须在后台线程） */
    @JvmStatic
    fun clear() {
        synchronized(dbLock) {
            db?.let {
                it.close()
                db = null
            }
            val file = dbFile()
            file.delete()
            File(file.parentFile, file.name + "-journal").delete()
        }
    }

    /** 命中且未过期时返回缓存值（value 为 null 即负缓存命中），未命中或已过期返回 null；命中刷新 time 实现 LRU */
    private fun lookup(key: String, type: Type): Hit? {
        val d = db()
        var time = 0L
        var ttl = 0L
        var json: String? = null
        d.query(TABLE, arrayOf("time", "ttl", "json"), "key = ?", arrayOf(key), null, null, null).use { c ->
            if (!c.moveToFirst()) return null
            time = c.getLong(0)
            ttl = c.getLong(1)
            if (!c.isNull(2)) json = c.getString(2)
        }
        if (ttl != TTL_PERMANENT && System.currentTimeMillis() - time >= ttl) return null
        d.execSQL("UPDATE $TABLE SET time = ? WHERE key = ?", arrayOf<Any>(System.currentTimeMillis(), key))
        return Hit(json?.let { JsonUtils.GSON.fromJson<Any>(it, type) })
    }

    private class Hit(val value: Any?)

    private fun store(key: String, ttl: Long, json: String?) {
        if (json != null && json.length > MAX_ENTRY_BYTES) {
            LOG.warning("Mod cache entry too large, skipped: $key")
            return
        }
        val d = db()
        d.beginTransaction()
        try {
            d.execSQL(
                "INSERT OR REPLACE INTO $TABLE (key, time, ttl, json) VALUES (?, ?, ?, ?)",
                arrayOf<Any?>(key, System.currentTimeMillis(), ttl, json)
            )
            evict(d)
            d.setTransactionSuccessful()
        } finally {
            d.endTransaction()
        }
    }

    /** 清理已过期条目；条数与总字节双限制，超限按 time 淘汰最旧（调用方持事务） */
    private fun evict(d: SQLiteDatabase) {
        val now = System.currentTimeMillis()
        // delete 的参数按 TEXT 绑定，与 INTEGER 列比较需 CAST（否则 INTEGER 恒小于 TEXT，会误删全部）
        d.delete(TABLE, "ttl != 0 AND time + ttl <= CAST(? AS INTEGER)", arrayOf(now.toString()))
        var count = count(d)
        var totalBytes = totalBytes(d)
        if (count <= MAX_ENTRIES && totalBytes <= MAX_TOTAL_BYTES) return
        // 从最旧开始淘汰，剩余条目满足双限制即止
        val toDelete = ArrayList<String>()
        d.query(TABLE, arrayOf("key", "json"), null, null, null, null, "time ASC").use { c ->
            while (c.moveToNext()) {
                if (count <= MAX_ENTRIES && totalBytes <= MAX_TOTAL_BYTES) break
                totalBytes -= if (c.isNull(1)) 0 else c.getString(1).length
                count--
                toDelete.add(c.getString(0))
            }
        }
        toDelete.forEach { d.delete(TABLE, "key = ?", arrayOf(it)) }
    }

    private fun count(d: SQLiteDatabase): Int =
        d.rawQuery("SELECT COUNT(*) FROM $TABLE", null).use { c ->
            c.moveToFirst()
            c.getInt(0)
        }

    private fun totalBytes(d: SQLiteDatabase): Long =
        d.rawQuery("SELECT COALESCE(SUM(LENGTH(json)), 0) FROM $TABLE", null).use { c ->
            c.moveToFirst()
            c.getLong(0)
        }
}
