package com.example.picsum.data.vo

import android.os.Parcelable
import com.example.picsum.data.local.db.entity.ImageEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: String,
    val author: String,
    val downloadUrl: String,
    val height: Int,
    val url: String,
    val width: Int,
    var grayScale: Boolean = false,
    var blur: Int = 0,
    var isLike: Boolean,
    val tableId: Long
) : Parcelable

fun Image.toEntity() =
    ImageEntity(
        imageId = id,
        author,
        downloadUrl,
        height,
        url,
        width,
        grayScale,
        blur,
        isLike,
        0L
    ).apply {
        id = tableId
    }
