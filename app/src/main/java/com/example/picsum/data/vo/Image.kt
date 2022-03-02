package com.example.picsum.data.vo

import android.os.Parcelable
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
    var likeCount: Int
) : Parcelable

/*fun Photo.toLikeEntity() =
    LikeEntity(
        photoId = id,
        author,
        downloadUrl,
        height,
        url,
        width,
        grayScale,
        blur
    )*/
