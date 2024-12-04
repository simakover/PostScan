package ru.nyxsed.postscan.util

sealed class UiEvent {
    class ShowToast(val messageResId: Int) : UiEvent()
    class LaunchActivity() : UiEvent()
    class OpenUrl(val url: String) : UiEvent()
}