package com.example.univibe.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.model.User
import com.example.univibe.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    // UI state
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // UI events (one-shot) for navigation, to be observed by the NavigationWrapper
    private val _uiEvent = MutableSharedFlow<AuthEvent>(replay = 1)
    val uiEvent: SharedFlow<AuthEvent> = _uiEvent.asSharedFlow()

    init {
        // Comprobar si ya existe un usuario autenticado al iniciar
        val currentUser: User? = authRepository.getCurrentUser()
        if (currentUser != null) {
            Log.d(TAG, "init: current user exists -> navigating to home")
            _uiState.update { it.copy(user = currentUser, isAuthenticated = true) }
            // emitir evento de navegación en coroutine
            viewModelScope.launch { _uiEvent.emit(AuthEvent.NavigateToHome) }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.signIn(uiState.value.email, uiState.value.password)
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.user,
                            isAuthenticated = true,
                            password = "",
                            error = null
                        )
                    }
                    // Emitir evento de navegación a home
                    Log.d(TAG, "onSignInClick: sign-in success, emitting NavigateToHome")
                    _uiEvent.emit(AuthEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Log.d(TAG, "onSignInClick: sign-in error = ${result.message}")
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is AuthResult.Unauthenticated -> {
                    Log.d(TAG, "onSignInClick: unauthenticated")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = null,
                            isAuthenticated = false
                        )
                    }
                    _uiEvent.emit(AuthEvent.NavigateToAuth)
                }
            }
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.signUp(uiState.value.email, uiState.value.password)
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.user,
                            isAuthenticated = true,
                            password = "",
                            error = null
                        )
                    }
                    // Emitir evento de navegación a home
                    Log.d(TAG, "onSignUpClick: sign-up success, emitting NavigateToHome")
                    _uiEvent.emit(AuthEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    Log.d(TAG, "onSignUpClick: sign-up error = ${result.message}")
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is AuthResult.Unauthenticated -> {
                    Log.d(TAG, "onSignUpClick: unauthenticated")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = null,
                            isAuthenticated = false
                        )
                    }
                    _uiEvent.emit(AuthEvent.NavigateToAuth)
                }
            }
        }
    }


}
