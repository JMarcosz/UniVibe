package com.example.univibe.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.base.Navigation.NavigationManager
import com.example.univibe.base.Navigation.NavRoute
import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.use_case.auth.SignInUseCase
import com.example.univibe.domain.use_case.account.SignUpUseCase
import com.example.univibe.domain.use_case.auth.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = signInUseCase(uiState.value.email, uiState.value.password)
            handleAuthResult(result)
        }
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = signUpUseCase(uiState.value.email, uiState.value.password)
            handleAuthResult(result)
        }
    }

    fun onGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = signInWithGoogleUseCase(idToken)
            handleAuthResult(result)
        }
    }

    private suspend fun handleAuthResult(result: AuthResult) {
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
                NavigationManager.navigateTo(NavRoute.Home)
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
                NavigationManager.navigateTo(NavRoute.Auth)
            }
        }
    }
}


