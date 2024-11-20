package ru.nyxsed.postscan.presentation.screens.postsscreen

sealed class AuthState {
    object Authorized : AuthState()
    object NotAuthorized : AuthState()
}