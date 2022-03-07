package com.example.picsum.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.picsum.data.remote.PicsumDataSource
import com.example.picsum.data.remote.dto.ImageDto
import com.example.picsum.data.remote.dto.toVo
import com.example.picsum.data.vo.Image
import retrofit2.HttpException
import java.io.IOException

class ImagesPagingSource(
    private val picsumDataSource: PicsumDataSource
) : PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page: Int = params.key ?: PAGE_DEFAULT
        val limit: Int = params.loadSize
        return try {
            val response = picsumDataSource.getImages(page, limit)
            val result = response.body()?.toVo() ?: emptyList()
            val nextKey = if (result.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                page + (params.loadSize / LIMIT_DEFAULT)
            }
            LoadResult.Page(
                data = result,
                prevKey = if (page == PAGE_DEFAULT) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    companion object {
        const val PAGE_DEFAULT = 1
        const val LIMIT_DEFAULT = 100
    }
}