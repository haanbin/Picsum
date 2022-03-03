package com.example.picsum.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.picsum.data.paging.ImagesPagingSource
import com.example.picsum.data.paging.ImagesPagingSource.Companion.LIMIT_DEFAULT
import com.example.picsum.data.remote.PicsumDataSource
import com.example.picsum.data.vo.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicsumRepositoryImpl @Inject constructor(private val picsumDataSource: PicsumDataSource) :
    PicsumRepository {

    override fun getImages(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = LIMIT_DEFAULT, enablePlaceholders = false),
            pagingSourceFactory = { ImagesPagingSource(picsumDataSource) }
        ).flow
    }
}
