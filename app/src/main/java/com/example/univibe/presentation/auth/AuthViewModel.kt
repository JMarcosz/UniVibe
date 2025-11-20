package com.example.univibe.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.AuthResult
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

    // UI state
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // UI events (one-shot) for navigation, to be observed by the NavigationWrapper
    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent: SharedFlow<AuthUiEvent> = _uiEvent.asSharedFlow()

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
                    _uiEvent.emit(AuthUiEvent.NavigateToHome)
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is AuthResult.Unauthenticated -> {
                    _uiState.update { it.copy(isLoading = false, user = null, isAuthenticated = false) }
                    _uiEvent.emit(AuthUiEvent.NavigateToAuth)
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
                    _uiEvent.emit(AuthUiEvent.NavigateToHome)
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is AuthResult.Unauthenticated -> {
                    _uiState.update { it.copy(isLoading = false, user = null, isAuthenticated = false) }
                    _uiEvent.emit(AuthUiEvent.NavigateToAuth)
                }
            }
        }
    }

    // Eventos UI para navegación o acciones puntuales
    sealed class AuthUiEvent {
        object NavigateToHome : AuthUiEvent()
        object NavigateToAuth : AuthUiEvent()
    }
}
