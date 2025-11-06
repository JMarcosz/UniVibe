package com.example.univibe.presentation.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed interface AuthEvent {
    data class OnEmailChange(val email: String) : AuthEvent
    data class OnPasswordChange(val pass: String) : AuthEvent
    object OnLoginClick : AuthEvent
    object OnRegisterClick : AuthEvent
    data class OnGoogleSignInResult(val account: GoogleSignInAccount?) : AuthEvent
    object ClearError : AuthEvent
}