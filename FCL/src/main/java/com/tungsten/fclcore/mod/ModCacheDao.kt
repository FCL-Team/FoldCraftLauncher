package com.tungsten.fclcore.mod

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/** 按最旧优先取 key 与 json 长度（容量淘汰用）的投影 */
data class ModCacheKeyJson(
    val key: String,
    val json: String?,
)

/**
 * 全同步 DAO：调用方（RemoteModCache）均在后台线程访问，缓存读写失败由
 * RemoteModCache 捕获并降级为直连网络。
 */
@Dao
interface ModCacheDao {

    @Query("SELECT * FROM `cache` WHERE `key` = :key")
    fun lookup(key: String): ModCacheEntity?

    @Query("UPDATE `cache` SET `time` = :time WHERE `key` = :key")
    fun touch(key: String, time: Long)

    @Upsert
    fun upsert(entity: ModCacheEntity)

    @Query("DELETE FROM `cache` WHERE ttl != 0 AND time + ttl <= :now")
    fun deleteExpired(now: Long)

    @Query("SELECT COUNT(*) FROM `cache`")
    fun count(): Int

    @Query("SELECT COALESCE(SUM(LENGTH(json)), 0) FROM `cache`")
    fun totalBytes(): Long

    @Query("SELECT `key`, json FROM `cache` ORDER BY time ASC")
    fun oldestFirst(): List<ModCacheKeyJson>

    @Query("DELETE FROM `cache` WHERE `key` IN (:keys)")
    fun deleteKeys(keys: List<String>)

    @Query("DELETE FROM `cache`")
    fun clearAll()
}
