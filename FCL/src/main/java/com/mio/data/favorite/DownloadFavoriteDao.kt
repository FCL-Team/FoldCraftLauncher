package com.mio.data.favorite

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadFavoriteDao {

    @Query("SELECT * FROM download_favorites ORDER BY favoriteTime DESC")
    fun observeAll(): Flow<List<DownloadFavoriteEntity>>

    @Query("SELECT * FROM download_favorites WHERE id = :id")
    fun getById(id: String): DownloadFavoriteEntity?

    @Upsert
    suspend fun upsert(entity: DownloadFavoriteEntity)

    @Query("DELETE FROM download_favorites WHERE id = :id")
    suspend fun deleteById(id: String)
}
