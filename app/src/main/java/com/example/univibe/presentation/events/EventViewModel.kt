package com.example.univibe.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.use_case.events.GetSubscribedEventsUseCase
import com.example.univibe.domain.use_case.events.ToggleEventSubscriptionUseCase
import com.example.univibe.domain.use_case.events.ToggleLikeUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar la pantalla de eventos suscritos.
 * Muestra únicamente los eventos a los que el usuario se ha suscrito.
 */
@HiltViewModel
class EventViewModel @Inject constructor(
    private val getSubscribedEventsUseCase: GetSubscribedEventsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleSubscriptionUseCase: ToggleEventSubscriptionUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    init {
        loadSubscribedEvents()
    }

    /**
     * Carga los eventos a los que el usuario está suscrito.
     */
    private fun loadSubscribedEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            android.util.Log.d("EventViewModel", "Iniciando carga de eventos suscritos")

            try {
                getSubscribedEventsUseCase().collect { events ->
                    android.util.Log.d("EventViewModel", "Eventos suscritos recibidos en ViewModel: ${events.size}")
                    events.forEach { event ->
                        android.util.Log.d("EventViewModel", "  - ${event.title} (${event.id})")
                    }

                    _uiState.update {
                        it.copy(
                            subscribedEvents = events,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("EventViewModel", "Error cargando eventos suscritos", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar eventos suscritos"
                    )
                }
            }
        }
    }

    /**
     * Alterna el estado de "me gusta" de un evento.
     */
    fun toggleLike(event: Event) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(processingEventIds = it.processingEventIds + event.id)
            }

            try {
                val result = toggleLikeUseCase(event.id)
                result.onSuccess {
                    android.util.Log.d("EventViewModel", "Like toggle exitoso para evento: ${event.id}")
                }.onFailure { error ->
                    android.util.Log.e("EventViewModel", "Error al toggle like: ${error.message}")
                }
            } catch (e: Exception) {
                android.util.Log.e("EventViewModel", "Excepción en toggleLike", e)
            } finally {
                _uiState.update {
                    it.copy(processingEventIds = it.processingEventIds - event.id)
                }
            }
        }
    }

    /**
     * Alterna la suscripción a un evento.
     * Si el usuario se desuscribe, el evento desaparecerá de la lista.
     */
    fun toggleSubscription(event: Event) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(processingEventIds = it.processingEventIds + event.id)
            }

            try {
                val result = toggleSubscriptionUseCase(event.id)
                result.onSuccess {
                    android.util.Log.d("EventViewModel", "Suscripción toggle exitosa para evento: ${event.id}")
                }.onFailure { error ->
                    android.util.Log.e("EventViewModel", "Error al toggle suscripción: ${error.message}")
                }
            } catch (e: Exception) {
                android.util.Log.e("EventViewModel", "Excepción en toggleSubscription", e)
            } finally {
                _uiState.update {
                    it.copy(processingEventIds = it.processingEventIds - event.id)
                }
            }
        }
    }

    /**
     * Verifica si un evento está marcado como favorito por el usuario actual.
     */
    fun isFavorite(event: Event): Boolean {
        return currentUserId?.let { event.likes.containsKey(it) } ?: false
    }

    /**
     * Verifica si un evento está en la lista de suscritos.
     * En esta pantalla, todos los eventos deberían estar suscritos.
     */
    fun isSubscribed(eventId: String): Boolean {
        return _uiState.value.subscribedEvents.any { it.id == eventId }
    }

    /**
     * Abre el modal de detalles del evento.
     */
    fun openEventDetail(event: Event) {
        _uiState.update { it.copy(selectedEvent = event) }
    }

    /**
     * Cierra el modal de detalles del evento.
     */
    fun closeModal() {
        _uiState.update { it.copy(selectedEvent = null) }
    }

    /**
     * Recarga los eventos suscritos.
     */
    fun refresh() {
        loadSubscribedEvents()
    }
}

/**
 * Estado de la UI para la pantalla de eventos suscritos.
 */
data class EventUiState(
    val subscribedEvents: List<Event> = emptyList(),
    val selectedEvent: Event? = null,
    val processingEventIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

