package com.example.picsum.ui.splash

import androidx.lifecycle.viewModelScope
import com.example.picsum.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    private val _navImagesEvent = MutableSharedFlow<Unit>()
    val navImagesEvent = _navImagesEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1000L)
            _navImagesEvent.emit(Unit)
        }
    }
}
