package com.example.picsum.domain

import com.example.picsum.data.PicsumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetImagesUseCase @Inject constructor(private val picsumRepository: PicsumRepository) {

    operator fun invoke() = picsumRepository.getImages()
}