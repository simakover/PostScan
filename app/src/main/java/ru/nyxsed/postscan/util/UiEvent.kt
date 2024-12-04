package ru.nyxsed.postscan.util

sealed class UiEvent {
    class ShowToast(val messageResId: Int) : UiEvent()
}