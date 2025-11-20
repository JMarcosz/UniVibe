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

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<AuthEvent>(replay = 1)
    val uiEvent: SharedFlow<AuthEvent> = _uiEvent.asSharedFlow()

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
                    _uiEvent.emit(AuthEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is AuthResult.Unauthenticated -> {
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
                    _uiEvent.emit(AuthEvent.NavigateToHome)
                }

                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                is AuthResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is AuthResult.Unauthenticated -> {
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
