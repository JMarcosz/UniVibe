package com.example.univibe.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.use_case.AuthUseCases
import com.example.univibe.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _state = mutableStateOf(AuthState())
    val state: State<AuthState> = _state

    fun onEvent(event: AuthEvent) {
        viewModelScope.launch {
            when (event) {
                is AuthEvent.OnEmailChange -> {
                    _state.value = _state.value.copy(email = event.email)
                }
                is AuthEvent.OnPasswordChange -> {
                    _state.value = _state.value.copy(pass = event.pass)
                }
                is AuthEvent.OnLoginClick -> {
                    login()
                }
                is AuthEvent.OnRegisterClick -> {
                    register()
                }
                is AuthEvent.OnGoogleSignInResult -> {
                    event.account?.idToken?.let {
                        googleSignIn(it)
                    } ?: run {
                        _state.value = _state.value.copy(error = "Error al obtener la cuenta de Google")
                    }
                }
                is AuthEvent.ClearError -> {
                    _state.value = _state.value.copy(error = null)
                }
            }
        }
    }

    private suspend fun login() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authUseCases.login(_state.value.email, _state.value.pass)
        _state.value = when (result) {
            is Resource.Success -> _state.value.copy(isLoading = false, authSuccess = result.data)
            is Resource.Error -> _state.value.copy(isLoading = false, error = result.message)
            is Resource.Loading -> _state.value.copy(isLoading = true)
        }
    }

    private suspend fun register() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authUseCases.register(_state.value.email, _state.value.pass)
        _state.value = when (result) {
            is Resource.Success -> _state.value.copy(isLoading = false, authSuccess = result.data)
            is Resource.Error -> _state.value.copy(isLoading = false, error = result.message)
            is Resource.Loading -> _state.value.copy(isLoading = true)
        }
    }

    private suspend fun googleSignIn(idToken: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        val result = authUseCases.googleSignIn(idToken)
        _state.value = when (result) {
            is Resource.Success -> _state.value.copy(isLoading = false, authSuccess = result.data)
            is Resource.Error -> _state.value.copy(isLoading = false, error = result.message)
            is Resource.Loading -> _state.value.copy(isLoading = true)
        }
    }
}