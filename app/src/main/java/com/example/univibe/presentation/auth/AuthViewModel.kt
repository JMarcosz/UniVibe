package com.example.univibe.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // initialize state from current user if available
        val current = authRepository.getCurrentUser()
        if (current != null) {
            _uiState.value = AuthUiState(
                isLoading = false,
                user = current,
                error = null,
                isAuthenticated = true
            )
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signUp(email, password)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = result.user,
                        error = null,
                        isAuthenticated = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = null,
                        error = result.message,
                        isAuthenticated = false
                    )
                }
                is AuthResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is AuthResult.Unauthenticated -> {
                    _uiState.value = AuthUiState(isLoading = false, user = null, error = null, isAuthenticated = false)
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signIn(email, password)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = result.user,
                        error = null,
                        isAuthenticated = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = null,
                        error = result.message,
                        isAuthenticated = false
                    )
                }
                is AuthResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is AuthResult.Unauthenticated -> {
                    _uiState.value = AuthUiState(isLoading = false, user = null, error = null, isAuthenticated = false)
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signInWithGoogle(idToken)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = result.user,
                        error = null,
                        isAuthenticated = true
                    )
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState(
                        isLoading = false,
                        user = null,
                        error = result.message,
                        isAuthenticated = false
                    )
                }
                is AuthResult.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is AuthResult.Unauthenticated -> {
                    _uiState.value = AuthUiState(isLoading = false, user = null, error = null, isAuthenticated = false)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = authRepository.signOut()) {
                is AuthResult.Unauthenticated -> {
                    _uiState.value = AuthUiState(isLoading = false, user = null, error = null, isAuthenticated = false)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
                else -> {
                    _uiState.value = AuthUiState(isLoading = false, user = null, error = null, isAuthenticated = false)
                }
            }
        }
    }
}

