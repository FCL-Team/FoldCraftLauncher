package com.mio.data.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.json.Json

/** List<String> 与 TEXT 列互转（kotlinx.serialization JSON），categories 与 groups 列共用 */
class FavoriteConverters {

    @TypeConverter
    fun fromStringList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toStringList(value: String): List<String> = try {
        Json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 * 下载收藏库：用户数据，导出 schema 留底。
 * 分组功能（v2）处于测试阶段，schema 变更暂用破坏式重建，合并前需补正式迁移。
 */
@Database(
    entities = [DownloadFavoriteEntity::class, FavoriteGroupEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(FavoriteConverters::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun downloadFavoriteDao(): DownloadFavoriteDao

    abstract fun favoriteGroupDao(): FavoriteGroupDao

    companion object {
        @Volatile
        private var instance: FavoriteDatabase? = null

        fun getInstance(context: Context): FavoriteDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "download_favorites.db")
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }
}
