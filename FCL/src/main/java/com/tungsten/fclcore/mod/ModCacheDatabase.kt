package com.tungsten.fclcore.mod

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 模组仓库缓存库：内容为可重建的 API 响应缓存，无迁移价值，
 * schema 变更时直接换库名或 destructive 重建即可，故不导出 schema。
 */
@Database(entities = [ModCacheEntity::class], version = 1, exportSchema = false)
abstract class ModCacheDatabase : RoomDatabase() {
    abstract fun modCacheDao(): ModCacheDao
}
