package com.example.picsum.data

import androidx.paging.PagingData
import com.example.picsum.data.vo.Image
import kotlinx.coroutines.flow.Flow

interface PicsumRepository {

    fun getImages(): Flow<PagingData<Image>>

    suspend fun updateImage(image: Image)
}