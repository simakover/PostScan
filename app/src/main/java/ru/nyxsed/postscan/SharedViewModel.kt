package ru.nyxsed.postscan

import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.nyxsed.postscan.presentation.screens.postsscreen.AuthState

class SharedViewModel(
    private val storage: VKKeyValueStorage,
): ViewModel() {

    private val _authStateFlow = MutableStateFlow<AuthState>(AuthState.NotAuthorized)
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    fun checkState() {
        val currentToken = VKAccessToken.restore(storage)
        val loggedIn = currentToken != null && currentToken.isValid
        _authStateFlow.value = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }

    init {
        checkState()
    }
}