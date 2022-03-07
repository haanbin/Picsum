package com.example.picsum.ui.image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.picsum.base.BaseViewModel
import com.example.picsum.data.vo.Image
import com.example.picsum.domain.UpdateImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val updateImageUseCase: UpdateImageUseCase,
    state: SavedStateHandle
) :
    BaseViewModel() {

    private val _image = MutableStateFlow<Image?>(null)
    val image = _image.asStateFlow()

    private val _failedVisible = MutableStateFlow(false)
    val failedVisible = _failedVisible.asStateFlow()

    private val _loadingVisible = MutableStateFlow(true)
    val loadingVisible = _loadingVisible.asStateFlow()

    private val _backEvent = MutableSharedFlow<Unit>()
    val backEvent = _backEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<Unit>()
    val errorEvent = _errorEvent.asSharedFlow()

    // 이미지 로딩 처리
    val onResourceReady: () -> Unit = {
        _loadingVisible.value = false
    }

    // 이미지 에러처리
    val onLoadFailed: () -> Unit = {
        _failedVisible.value = true
        _loadingVisible.value = false
    }

    init {
        state.get<Image>(IMAGE)?.let {
            _image.value = it
        } ?: kotlin.run {
            viewModelScope.launch {
                _errorEvent.emit(Unit)
            }
        }
    }

    fun onClickBackImage() {
        viewModelScope.launch {
            _backEvent.emit(Unit)
        }
    }

    fun onClickLike() {
        _image.value?.let {
            viewModelScope.launch {
                val image = it.copy(isLike = !it.isLike)
                updateImageUseCase(image.id, image.isLike)
                updatePhoto(image, false)
            }
        }
    }

    fun onClickMore() {

    }

    fun onClickRetry() {
        _image.value?.let {
            updatePhoto(it.copy())
        }
    }

    private fun updatePhoto(image: Image, isNeedLoading: Boolean = true) {
        _image.update { image }
        _failedVisible.value = false
        if (isNeedLoading) {
            _loadingVisible.value = true
        }
    }

    companion object {
        const val IMAGE = "image"
    }
}