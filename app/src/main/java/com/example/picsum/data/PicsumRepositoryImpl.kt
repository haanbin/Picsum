package com.example.picsum.data

import androidx.paging.*
import com.example.picsum.data.local.RoomDataSource
import com.example.picsum.data.local.db.entity.toVo
import com.example.picsum.data.paging.ImageRemoteMediator
import com.example.picsum.data.paging.ImagesPagingSource.Companion.LIMIT_DEFAULT
import com.example.picsum.data.remote.PicsumDataSource
import com.example.picsum.data.vo.Image
import com.example.picsum.data.vo.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicsumRepositoryImpl @Inject constructor(
    private val picsumDataSource: PicsumDataSource,
    private val roomDataSource: RoomDataSource
) : PicsumRepository {

    override fun getImages(): Flow<PagingData<Image>> {
        val pagingSourceFactory = { roomDataSource.getImages() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = LIMIT_DEFAULT, enablePlaceholders = true),
            remoteMediator = ImageRemoteMediator(picsumDataSource, roomDataSource),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map {
            it.map { imageEntity ->
                imageEntity.toVo()
            }
        }
    }

    override suspend fun updateImage(image: Image) {
        roomDataSource.updateImage(image.toEntity())
    }
}

