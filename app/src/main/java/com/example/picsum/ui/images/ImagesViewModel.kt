package com.example.picsum.ui.images

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.picsum.base.BaseViewModel
import com.example.picsum.data.vo.Image
import com.example.picsum.domain.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(private val getImagesUseCase: GetImagesUseCase) :
    BaseViewModel() {

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

    val images: Flow<PagingData<Image>>? = null

    fun searchImages(isRefresh: Boolean = false): Flow<PagingData<Image>> {
        if (images != null && !isRefresh) {
            return images
        }
        _refreshVisible.value = false
        return getImagesUseCase().cachedIn(viewModelScope)
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
}