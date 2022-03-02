package com.example.picsum.data.remote

import com.example.picsum.data.remote.dto.ImageDto
import retrofit2.Response

interface PicsumDataSource {

    suspend fun getImages(
        page: Int,
        limit: Int
    ): Response<List<ImageDto>>

    suspend fun getImage(photoId: String): Response<ImageDto>
}