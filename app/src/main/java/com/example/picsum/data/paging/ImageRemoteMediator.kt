package com.example.picsum.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.picsum.data.local.RoomDataSource
import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.local.db.entity.ImageRemoteKeysEntity
import com.example.picsum.data.remote.PicsumDataSource
import com.example.picsum.data.remote.dto.toEntity
import com.example.picsum.data.remote.dto.toVo
import com.example.picsum.data.vo.Image
import retrofit2.HttpException
import java.io.IOException

private const val PICSUM_IMAGE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val picsumDataSource: PicsumDataSource,
    private val roomDataSource: RoomDataSource
) : RemoteMediator<Int, ImageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PICSUM_IMAGE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val response = picsumDataSource.getImages(page, state.config.pageSize)
            val images = response.body()?.toEntity() ?: emptyList()
            val endOfPaginationReached = images.isEmpty()
            // clear all tables in the database
            if (loadType == LoadType.REFRESH) {
                roomDataSource.removeRemoteKeys()
                roomDataSource.removeImages()
            }
            val prevKey = if (page == PICSUM_IMAGE_STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = images.map {
                ImageRemoteKeysEntity(
                    imageId = it.imageId.toLong(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            roomDataSource.addAllRemoteKeys(keys)
            roomDataSource.addAllImages(images)
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ImageEntity>): ImageRemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                // Get the remote keys of the last item retrieved
                val tes = roomDataSource.getRemoteKeysImageId(image.imageId.toLong())
                tes
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ImageEntity>): ImageRemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                // Get the remote keys of the first items retrieved
                roomDataSource.getRemoteKeysImageId(image.imageId.toLong())
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageEntity>
    ): ImageRemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.imageId?.let { id ->
                roomDataSource.getRemoteKeysImageId(id.toLong())
            }
        }
    }
}