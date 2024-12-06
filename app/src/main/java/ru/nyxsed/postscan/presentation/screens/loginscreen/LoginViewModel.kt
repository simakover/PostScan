package ru.nyxsed.postscan.presentation.screens.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.UiEvent

class LoginViewModel(
    private val connectionChecker: ConnectionChecker,
) : ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    fun launchActivity() {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(R.string.no_internet_connection))
                return@launch
            }
            _uiEventFlow.emit(UiEvent.LaunchActivity())
        }
    }
}