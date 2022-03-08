package com.example.picsum.ui.image.dialog

import androidx.lifecycle.viewModelScope
import com.example.picsum.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageMoreBottomViewModel @Inject constructor() : BaseViewModel() {

    private val _fileDownloadEvent = MutableSharedFlow<Unit>()
    val fileDownloadEvent = _fileDownloadEvent.asSharedFlow()

    private val _webLinkEvent = MutableSharedFlow<Unit>()
    val webLinkEvent = _webLinkEvent.asSharedFlow()

    private val _shareEvent = MutableSharedFlow<Unit>()
    val shareEvent = _shareEvent.asSharedFlow()

    private val _effectEvent = MutableSharedFlow<Unit>()
    val effectEvent = _effectEvent.asSharedFlow()

    fun onClickFileDownload() {
        viewModelScope.launch {
            _fileDownloadEvent.emit(Unit)
        }
    }

    fun onClickWebLink() {
        viewModelScope.launch {
            _webLinkEvent.emit(Unit)
        }
    }

    fun onClickShare() {
        viewModelScope.launch {
            _shareEvent.emit(Unit)
        }
    }

    fun onClickEffect() {
        viewModelScope.launch {
            _effectEvent.emit(Unit)
        }
    }
}