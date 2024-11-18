package ru.nyxsed.postscan.presentation.screens.mainscreen

sealed class AuthState {
    object Authorized : AuthState()
    object NotAuthorized : AuthState()
}