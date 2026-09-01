package com.abasyn.mvvmarchitecture.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SplashViewModel : ViewModel() {
    private val _events = MutableSharedFlow<SplashEvents>()
    val events : SharedFlow<SplashEvents> = _events

    init {
        navigateToMain()
    }

    private fun navigateToMain() = viewModelScope.launch {
        delay(2000.milliseconds)
        _events.emit(
            SplashEvents.NavigateToMain
        )
    }

    sealed class SplashEvents {
        data object NavigateToMain : SplashEvents()
    }
}