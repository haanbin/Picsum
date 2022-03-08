package com.example.picsum.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.vo.Image

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<ImageEntity>)

    @Query("SELECT * FROM image ORDER BY id ASC")
    fun selectImages(): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM image")
    suspend fun deleteImages()

    @Query("UPDATE image SET isLike = :isLike, grayScale = :grayScale, blur = :blur WHERE imageId = :imageId")
    suspend fun updateImage(imageId: String, isLike: Boolean, grayScale: Boolean, blur: Int)
}