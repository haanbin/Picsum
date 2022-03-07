package com.example.picsum.data.remote.dto

import com.example.picsum.data.local.db.entity.ImageEntity
import com.example.picsum.data.vo.Image
import com.google.gson.annotations.SerializedName

data class ImageDto(
    val id: String,
    val author: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    val height: Int,
    val url: String,
    val width: Int
)

//fun PhotoDto.toVo(): Photo = Photo(id, author, downloadUrl, height, url, width, likeCount = 0)


fun List<ImageDto>.toVo(grayscale: Boolean = false, blur: Int = 0): List<Image> =
    map {
        Image(
            it.id,
            it.author,
            it.downloadUrl,
            it.height,
            it.url,
            it.width,
            grayscale,
            blur,
            false,
            0L
        )
    }

fun List<ImageDto>.toEntity(): List<ImageEntity> {
    return map {
        ImageEntity(
            it.id,
            it.author,
            it.downloadUrl,
            it.height,
            it.url,
            it.width,
            false,
            0,
            false,
            System.currentTimeMillis()
        )
    }
}
