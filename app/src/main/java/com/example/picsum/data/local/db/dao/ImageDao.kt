package com.example.picsum.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picsum.data.local.db.entity.ImageEntity

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ImageEntity>)

    @Query("SELECT * FROM image ORDER BY id ASC")
    fun selectImages(): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM image")
    suspend fun deleteImages()

}