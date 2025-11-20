package com.example.univibe.presentation.auth

import com.example.univibe.domain.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val email: String = "",
    val password: String = ""
)
