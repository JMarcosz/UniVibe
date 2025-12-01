package com.example.univibe.presentation.events.detail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.use_case.events.GetEventsUseCase
import com.example.univibe.domain.use_case.events.ToggleEventSubscriptionUseCase
import com.example.univibe.domain.use_case.events.ToggleLikeUseCase
import com.example.univibe.util.QrCodeGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleSubscriptionUseCase: ToggleEventSubscriptionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventDetailUiState>(EventDetailUiState.Loading)
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    fun loadEventDetail(eventId: String) {
        viewModelScope.launch {
            getEventsUseCase().collect { events ->
                val event = events.find { it.id == eventId }
                if (event != null) {
                    val qrBitmap = QrCodeGenerator.generateQrCode(event.qrCodeUrl)
                    _uiState.value = EventDetailUiState.Success(event, qrBitmap)
                } else {
                    _uiState.value = EventDetailUiState.Error("Evento no encontrado")
                }
            }
        }
    }

    fun toggleLike(eventId: String) {
        viewModelScope.launch {
            toggleLikeUseCase(eventId)
        }
    }

    fun toggleSubscription(eventId: String) {
        viewModelScope.launch {
            toggleSubscriptionUseCase(eventId)
        }
    }
}

sealed class EventDetailUiState {
    object Loading : EventDetailUiState()
    data class Success(val event: Event, val qrBitmap: Bitmap?) : EventDetailUiState()
    data class Error(val message: String) : EventDetailUiState()
}

