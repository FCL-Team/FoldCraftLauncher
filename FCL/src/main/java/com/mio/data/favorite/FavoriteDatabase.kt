package com.mio.data.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.json.Json

/** categories 列表与 TEXT 列互转（kotlinx.serialization JSON） */
class FavoriteConverters {

    @TypeConverter
    fun fromCategories(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toCategories(value: String): List<String> = try {
        Json.decodeFromString(value)
    } catch (e: Exception) {
        emptyList()
    }
}

/**
 * 下载收藏库：用户数据，schema 变更必须走迁移，导出 schema 留底。
 */
@Database(
    entities = [DownloadFavoriteEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(FavoriteConverters::class)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun downloadFavoriteDao(): DownloadFavoriteDao

    companion object {
        @Volatile
        private var instance: FavoriteDatabase? = null

        fun getInstance(context: Context): FavoriteDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "download_favorites.db")
                .build().also { instance = it }
        }
    }
}
