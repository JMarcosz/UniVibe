package com.example.univibe.presentation.auth

sealed class AuthEvent {
    object NavigateToHome : AuthEvent()
    object NavigateToAuth : AuthEvent()
}