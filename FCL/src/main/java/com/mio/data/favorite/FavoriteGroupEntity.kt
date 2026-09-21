package com.mio.data.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 收藏自定义分组（如"Forge 模组""性能优化"），收藏条目经 DownloadFavoriteEntity.groups 引用分组 id。
 */
@Entity(tableName = "favorite_groups")
data class FavoriteGroupEntity(
    @PrimaryKey
    @ColumnInfo(name = "groupId")
    val groupId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "createTime")
    val createTime: Long,
)
