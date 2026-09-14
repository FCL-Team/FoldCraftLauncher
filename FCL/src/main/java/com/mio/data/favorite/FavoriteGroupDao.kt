package com.mio.data.favorite

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGroupDao {

    @Query("SELECT * FROM favorite_groups ORDER BY createTime ASC")
    fun observeAll(): Flow<List<FavoriteGroupEntity>>

    @Upsert
    suspend fun upsert(group: FavoriteGroupEntity)

    @Query("DELETE FROM favorite_groups WHERE groupId = :groupId")
    suspend fun deleteById(groupId: String)
}
