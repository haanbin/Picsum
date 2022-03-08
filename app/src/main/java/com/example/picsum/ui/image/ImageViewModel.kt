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
) : BaseViewModel() {

    private val _image = MutableStateFlow<Image?>(null)
    val image = _image.asStateFlow()

    private val _failedVisible = MutableStateFlow(false)
    val failedVisible = _failedVisible.asStateFlow()

    private val _loadingVisible = MutableStateFlow(true)
    val loadingVisible = _loadingVisible.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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
                _uiEvent.emit(UIEvent.ImageError)
            }
        }
    }

    fun onClickBackImage() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.Back)
        }
    }

    fun onClickLike() {
        _image.value?.let {
            viewModelScope.launch {
                val image = it.copy(isLike = !it.isLike)
                updateImageUseCase(image)
                updatePhoto(image, false)
            }
        }
    }

    fun onClickMore() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.NavImageMore)
        }
    }

    fun onClickRetry() {
        _image.value?.let {
            updatePhoto(it.copy())
        }
    }


    fun onClickFileDownload() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.FileDownload)
        }
    }

    fun onClickWebLink() {
        viewModelScope.launch {
            _image.value?.let {
                _uiEvent.emit(UIEvent.OpenWebLink(it.url))
            }
        }
    }

    fun onClickShare() {
        viewModelScope.launch {
            _image.value?.let {
                _uiEvent.emit(UIEvent.Share(it.url))
            }
        }
    }

    fun onClickEffect() {
        viewModelScope.launch {
            _image.value?.let {
                _uiEvent.emit(UIEvent.NavImageEffect(it.copy()))
            }
        }
    }

    fun applyEffect(image: Image) {
        updatePhoto(image)
        viewModelScope.launch {
            updateImageUseCase(image)
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

    sealed class UIEvent {
        object ImageError : UIEvent()
        object Back : UIEvent()
        object FileDownload : UIEvent()
        object NavImageMore : UIEvent()
        data class NavImageEffect(val image: Image) : UIEvent()
        data class OpenWebLink(val url: String) : UIEvent()
        data class Share(val url: String) : UIEvent()
    }
}