package com.example.univibe.presentation.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// I will assume a use case for sending a password reset email exists.
// import com.example.univibe.domain.use_case.SendPasswordResetEmailUseCase

data class RecoveryState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailSentSuccess: Boolean = false
)

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    // private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onSendPasswordResetEmailClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // sendPasswordResetEmailUseCase(_uiState.value.email)
                _uiState.update { it.copy(isLoading = false, emailSentSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

