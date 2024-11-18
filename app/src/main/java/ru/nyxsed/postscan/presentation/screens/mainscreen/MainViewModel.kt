package ru.nyxsed.postscan.presentation.screens.mainscreen

import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val storage: VKKeyValueStorage,
) : ViewModel() {
    private val _authStateFlow = MutableStateFlow<AuthState>(checkState())
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()


    fun performAuthResult() {
        _authStateFlow.value = checkState()
    }

    fun checkState() : AuthState {
        val currentToken = VKAccessToken.restore(storage)
        val loggedIn = currentToken != null && currentToken.isValid
        return if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }
}