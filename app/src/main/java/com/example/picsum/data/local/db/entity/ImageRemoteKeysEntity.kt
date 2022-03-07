package com.example.picsum.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_remote_keys")
data class ImageRemoteKeysEntity(
    @PrimaryKey
    val imageId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)