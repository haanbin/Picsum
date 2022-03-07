package com.example.picsum.data.local

import androidx.paging.PagingSource
import com.example.picsum.data.local.db.dao.ImageDao
import com.example.picsum.data.local.db.dao.ImageRemoteKeysDao
import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.local.db.entity.ImageRemoteKeysEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDataSourceImpl @Inject constructor(
    private val imageDao: ImageDao,
    private val imageRemoteKeysDao: ImageRemoteKeysDao
) : RoomDataSource {

    override suspend fun addAllImages(repos: List<ImageEntity>) {
        imageDao.insertAll(repos)
    }

    override fun getImages(): PagingSource<Int, ImageEntity> =
        imageDao.selectImages()

    override suspend fun removeImages() {
        imageDao.deleteImages()
    }

    override suspend fun addAllRemoteKeys(remoteKey: List<ImageRemoteKeysEntity>) {
        imageRemoteKeysDao.insertAll(remoteKey)
    }

    override suspend fun getRemoteKeysImageId(imageId: Long): ImageRemoteKeysEntity? =
        imageRemoteKeysDao.selectRemoteKeysImageId(imageId)

    override suspend fun removeRemoteKeys() {
        imageRemoteKeysDao.deleteRemoteKeys()
    }

    override suspend fun updateImage(imageId: String, isLike: Boolean) {
        imageDao.updateImage(imageId, isLike)
    }
}