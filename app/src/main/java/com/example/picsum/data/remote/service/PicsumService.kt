package com.example.picsum.data.remote.service

import com.example.picsum.data.remote.dto.ImageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PicsumService {

    @GET("v2/list")
    suspend fun getPhotoList(
        @QueryMap
        queryMap: Map<String, String>
    ): Response<List<ImageDto>>

    @GET("id/{id}/info")
    suspend fun getPhoto(
        @Path("id")
        id: String
    ): Response<ImageDto>
}
