package com.example.picsum.ui.images

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.picsum.base.BaseViewModel
import com.example.picsum.domain.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(private val getImagesUseCase: GetImagesUseCase) :
    BaseViewModel() {

    val images = getImagesUseCase()
        .cachedIn(viewModelScope)
}