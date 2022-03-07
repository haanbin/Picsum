package com.example.picsum.domain

import com.example.picsum.data.PicsumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateImageUseCase @Inject constructor(private val repository: PicsumRepository) {

    suspend operator
    fun invoke(imageId: String, isLike: Boolean) {
        repository.updateImage(imageId, isLike)
    }
}