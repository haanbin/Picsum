package com.example.picsum.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.picsum.data.vo.Image

@Entity(tableName = "image")
data class ImageEntity(
    val imageId: String,
    val author: String,
    val downloadUrl: String,
    val height: Int,
    val url: String,
    val width: Int,
    val grayScale: Boolean,
    val blur: Int,
    val isLike: Boolean,
    val inputTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

fun ImageEntity.toVo() =
    Image(
        id = imageId,
        author,
        downloadUrl,
        height,
        url,
        width,
        grayScale,
        blur,
        isLike,
        id
    )