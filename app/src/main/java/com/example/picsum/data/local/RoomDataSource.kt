package com.example.picsum.data.local

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.local.db.entity.ImageRemoteKeysEntity
import com.example.picsum.data.vo.Image

interface RoomDataSource {

    suspend fun addAllImages(repos: List<ImageEntity>)

    fun getImages(): PagingSource<Int, ImageEntity>

    suspend fun removeImages()

    suspend fun addAllRemoteKeys(remoteKey: List<ImageRemoteKeysEntity>)

    suspend fun getRemoteKeysImageId(imageId: Long): ImageRemoteKeysEntity?

    suspend fun removeRemoteKeys()

    suspend fun updateImage(image: ImageEntity)
}