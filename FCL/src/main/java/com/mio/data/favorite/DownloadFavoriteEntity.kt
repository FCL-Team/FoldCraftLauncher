package com.mio.data.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 下载资源收藏条目（CurseForge / Modrinth）。
 * id 形如 "CURSEFORGE:12345" / "MODRINTH:xxxx"，source 为平台（RemoteMod.Type 名），
 * type 为资源类别（RemoteModRepository.Type 名：MOD/MODPACK/RESOURCE_PACK/SHADER_PACK/WORLD），
 * modId 为平台侧项目 id，收藏页据此经 getModById 重新拉取完整详情。
 */
@Entity(tableName = "download_favorites")
data class DownloadFavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "modId")
    val modId: String,
    @ColumnInfo(name = "slug")
    val slug: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "iconUrl")
    val iconUrl: String,
    @ColumnInfo(name = "pageUrl")
    val pageUrl: String,
    @ColumnInfo(name = "downloadCount")
    val downloadCount: Int,
    @ColumnInfo(name = "categories")
    val categories: List<String>,
    @ColumnInfo(name = "favoriteTime")
    val favoriteTime: Long,
)
