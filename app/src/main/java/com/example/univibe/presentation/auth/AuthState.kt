package com.example.univibe.presentation.auth

import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val authSuccess: FirebaseUser? = null
)