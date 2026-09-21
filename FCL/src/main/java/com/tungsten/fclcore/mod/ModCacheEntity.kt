package com.tungsten.fclcore.mod

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 模组仓库远程查询结果缓存条目。
 * 列名与旧版原生 SQLite 表保持一致；json 为 NULL 表示负缓存（远程确认未命中）。
 */
@Entity(tableName = "cache")
data class ModCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "ttl")
    val ttl: Long,
    @ColumnInfo(name = "json")
    val json: String?,
)
