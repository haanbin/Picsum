package com.example.picsum.ui.image.dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.picsum.base.BaseViewModel
import com.example.picsum.data.vo.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageEffectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _applyEvent = MutableSharedFlow<Image>()
    val applyEvent = _applyEvent.asSharedFlow()

    val grayscaleChecked = MutableStateFlow(false)

    val blur = MutableStateFlow(0F)

    private lateinit var image: Image

    init {
        savedStateHandle.get<Image>(IMAGE)?.let {
            image = it
            grayscaleChecked.value = it.grayScale
            blur.value = it.blur.toFloat()
        }
    }

    fun onClickApply() {
        viewModelScope.launch {
            image.grayScale = grayscaleChecked.value
            image.blur = blur.value.toInt()
            _applyEvent.emit(image)
        }
    }

    companion object {
        private const val IMAGE = "image"
    }

}