package com.example.picsum.domain

import com.example.picsum.data.PicsumRepository
import com.example.picsum.data.vo.Image
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateImageUseCase @Inject constructor(private val repository: PicsumRepository) {

    suspend operator
    fun invoke(image: Image) {
        repository.updateImage(image)
    }
}