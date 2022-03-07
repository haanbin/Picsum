package com.example.picsum.ui.images

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.picsum.base.BaseViewModel
import com.example.picsum.data.vo.Image
import com.example.picsum.domain.GetImagesUseCase
import com.example.picsum.domain.UpdateImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val updateImageUseCase: UpdateImageUseCase
) : BaseViewModel() {

    private val _refreshVisible = MutableStateFlow(false)
    val refreshVisible = _refreshVisible.asStateFlow()

    private val _loadingVisible = MutableStateFlow(false)
    val loadingVisible = _loadingVisible.asStateFlow()

    private val _retryVisible = MutableStateFlow(false)
    val retryVisible = _retryVisible.asStateFlow()

    private val _listVisible = MutableStateFlow(false)
    val listVisible = _listVisible.asStateFlow()

    private val _emptyVisible = MutableStateFlow(false)
    val emptyVisible = _emptyVisible.asStateFlow()

    private val _refreshEvent = MutableSharedFlow<Unit>()
    val refreshEvent = _refreshEvent.asSharedFlow()

    private val _retryEvent = MutableSharedFlow<Unit>()
    val retryEvent = _retryEvent.asSharedFlow()

    private val _navImageEvent = MutableSharedFlow<Image>()
    val navImageEvent = _navImageEvent.asSharedFlow()

    var images: Flow<PagingData<Image>> = getImagesUseCase().cachedIn(viewModelScope)

    fun searchImages(isRefresh: Boolean = false): Flow<PagingData<Image>> {
        if (!isRefresh) {
            return images
        }
        _refreshVisible.value = false
        images = getImagesUseCase().cachedIn(viewModelScope)
        return images
    }

    fun onRefresh() {
        _refreshVisible.value = true
        viewModelScope.launch {
            _refreshEvent.emit(Unit)
        }
    }

    fun retry() {
        viewModelScope.launch {
            _retryEvent.emit(Unit)
        }
    }

    fun addLoadStateListener(
        emptyVisible: Boolean,
        listVisible: Boolean,
        progressVisible: Boolean,
        retryVisible: Boolean
    ) {
        _emptyVisible.value = emptyVisible
        _listVisible.value = listVisible
        _loadingVisible.value = progressVisible
        _retryVisible.value = retryVisible
    }

    fun updateImage(image: Image) {
        viewModelScope.launch {
            updateImageUseCase(imageId = image.id, !image.isLike)
        }
    }

    fun goNavImageEvent(image: Image) {
        viewModelScope.launch {
            _navImageEvent.emit(image.copy())
        }
    }
}