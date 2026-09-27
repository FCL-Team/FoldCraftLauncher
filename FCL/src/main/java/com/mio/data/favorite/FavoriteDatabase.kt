package com.mio.data.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        /** v2 新增分组：收藏表补 groups 列（存量条目为空 = 未分组），并建分组表 */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `download_favorites` ADD COLUMN `groups` TEXT NOT NULL DEFAULT '[]'")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `favorite_groups` (" +
                        "`groupId` TEXT NOT NULL, `name` TEXT NOT NULL, `createTime` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`groupId`))"
                )
            }
        }

        @Volatile
        private var instance: FavoriteDatabase? = null

        fun getInstance(context: Context): FavoriteDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "download_favorites.db")
                .addMigrations(MIGRATION_1_2)
                .build().also { instance = it }
        }
    }
}
