package com.tungsten.fclcore.mod

import com.google.gson.reflect.TypeToken
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.FileUtils
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * 模组仓库（CurseForge / Modrinth）远程查询结果缓存。
 * 缓存 API 响应的原始 JSON，命中时由调用方重新走 toVersion()/toMod() 转换，减少重复网络请求。
 * json 为 null 的条目表示"负缓存"（远程确认未命中，如自制 mod 的指纹），同样防止反复白查。
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
    private const val FLUSH_DELAY_SECONDS = 5L

    private class Entry(var time: Long, val ttl: Long, val json: String?) {
        constructor() : this(0L, TTL_PERMANENT, null)
    }

    private val cache = ConcurrentHashMap<String, Entry>()

    @Volatile
    private var loaded = false

    @Volatile
    private var dirty = false

    @Volatile
    private var pendingFlush: ScheduledFuture<*>? = null

    private val executor = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "RemoteModCache").apply { isDaemon = true }
    }

    private val entryMapType: Type =
        TypeToken.getParameterized(Map::class.java, String::class.java, Entry::class.java).type

    // 存 files 目录：CACHE_DIR 会被周期性清理，指纹类永久缓存会被误删
    private fun cacheFile(): Path = Paths.get(FCLPath.FILES_DIR, "cache", "mod_repository_cache.json")

    /**
     * 优先返回未过期的缓存结果，否则执行 fetcher（网络请求）并缓存其结果。
     * fetcher 返回 null 写负缓存；fetcher 抛异常（网络失败）不写任何缓存，直接向上传播。
     */
    @JvmStatic
    fun <T> getOrFetch(key: String, ttlMillis: Long, type: Type, fetcher: Callable<T>): T {
        loadIfNeeded()
        val entry = cache[key]
        if (entry != null && !expired(entry)) {
            // LRU：命中刷新时间，常用条目（如永久指纹）不被新拉取的大条目挤出
            entry.time = System.currentTimeMillis()
            LOG.info("Mod cache hit: $key")
            @Suppress("UNCHECKED_CAST")
            return entry.json?.let { JsonUtils.GSON.fromJson<T>(it, type) } as T
        }
        val result = fetcher.call()
        put(key, ttlMillis, if (result == null) null else JsonUtils.GSON.toJson(result, type))
        return result
    }

    /** 清空内存缓存并删除磁盘文件（设置页"清除模组缓存"入口调用，须在后台线程） */
    @JvmStatic
    fun clear() {
        cache.clear()
        dirty = false
        pendingFlush?.cancel(false)
        pendingFlush = null
        try {
            Files.deleteIfExists(cacheFile())
        } catch (e: IOException) {
            LOG.log(Level.WARNING, "Failed to delete mod repository cache file", e)
        }
    }

    private fun put(key: String, ttl: Long, json: String?) {
        if (json != null && json.length > MAX_ENTRY_BYTES) {
            LOG.warning("Mod cache entry too large, skipped: $key")
            return
        }
        cache[key] = Entry(System.currentTimeMillis(), ttl, json)
        evict()
        dirty = true
        scheduleFlush()
    }

    private fun expired(entry: Entry): Boolean =
        entry.ttl != TTL_PERMANENT && System.currentTimeMillis() - entry.time >= entry.ttl

    /** 条数与总字节双限制，超限按时间淘汰最旧条目 */
    private fun evict() {
        var totalBytes = cache.values.sumOf { it.json?.length ?: 0 }
        while (cache.size > MAX_ENTRIES || totalBytes > MAX_TOTAL_BYTES) {
            val oldest = cache.entries.minByOrNull { it.value.time } ?: break
            totalBytes -= oldest.value.json?.length ?: 0
            cache.remove(oldest.key)
        }
    }

    private fun loadIfNeeded() {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            val file = cacheFile()
            if (Files.isRegularFile(file)) {
                try {
                    val map = JsonUtils.GSON.fromJson<Map<String, Entry>>(
                        FileUtils.readText(file), entryMapType
                    )
                    if (map != null) cache.putAll(map)
                } catch (e: Exception) {
                    LOG.log(Level.WARNING, "Failed to read mod repository cache", e)
                }
            }
            loaded = true
        }
    }

    private fun scheduleFlush() {
        if (pendingFlush != null) return
        synchronized(this) {
            if (pendingFlush != null) return
            pendingFlush = executor.schedule({ flush() }, FLUSH_DELAY_SECONDS, TimeUnit.SECONDS)
        }
    }

    /** 防抖落盘：合并高频写入；写入前剔除已过期条目 */
    private fun flush() {
        pendingFlush = null
        if (!dirty) return
        dirty = false
        val file = cacheFile()
        try {
            Files.createDirectories(file.parent)
            cache.entries.removeIf { expired(it.value) }
            val tmp = file.resolveSibling(file.fileName.toString() + ".tmp")
            FileUtils.writeText(tmp, JsonUtils.GSON.toJson(cache, entryMapType), StandardCharsets.UTF_8)
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            LOG.log(Level.WARNING, "Failed to write mod repository cache", e)
        }
    }
}
