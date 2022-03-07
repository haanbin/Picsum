package com.example.picsum.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picsum.data.local.db.entity.ImageRemoteKeysEntity

@Dao
interface ImageRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ImageRemoteKeysEntity>)

    @Query("SELECT * FROM image_remote_keys WHERE imageId = :imageId")
    suspend fun selectRemoteKeysImageId(imageId: Long): ImageRemoteKeysEntity?

    @Query("DELETE FROM image_remote_keys")
    suspend fun deleteRemoteKeys()
}