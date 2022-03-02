package com.example.picsum.data.remote

import com.example.picsum.data.remote.dto.ImageDto
import com.example.picsum.data.remote.service.PicsumService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PicsumDataSourceImpl @Inject constructor(private val picsumService: PicsumService) :
    PicsumDataSource {

    override suspend fun getImages(
        page: Int,
        limit: Int
    ): Response<List<ImageDto>> =
        picsumService.getPhotoList(
            mapOf(
                PAGE to page.toString(),
                Limit to limit.toString()
            )
        )

    override suspend fun getImage(photoId: String): Response<ImageDto> =
        picsumService.getPhoto(photoId)

    companion object {
        private const val PAGE = "page"
        private const val Limit = "limit"
    }
}