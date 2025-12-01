package com.example.univibe.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.use_case.events.GetEventsUseCase
import com.example.univibe.domain.use_case.events.SeedEventsUseCase
import com.example.univibe.domain.use_case.events.ToggleLikeUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val seedEventsUseCase: SeedEventsUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _suggestedEvents = MutableStateFlow<List<Event>>(emptyList())
    val suggestedEvents: StateFlow<List<Event>> = _suggestedEvents.asStateFlow()

    private val _favoriteEvents = MutableStateFlow<List<Event>>(emptyList())
    val favoriteEvents: StateFlow<List<Event>> = _favoriteEvents.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _isSubscribed = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isSubscribed: StateFlow<Map<String, Boolean>> = _isSubscribed.asStateFlow()

    // Variable para rastrear si ya se inicializaron los eventos
    private var isEventsInitialized = false

    init {
        seedInitialEvents()
        loadEvents()
    }

    private fun seedInitialEvents() {
        viewModelScope.launch {
            try {
                if (!isEventsInitialized) {
                    Log.d("HomeViewModel", "Inicializando eventos en Firestore...")
                    val result = seedEventsUseCase()
                    result.onSuccess {
                        Log.d("HomeViewModel", "Eventos inicializados correctamente")
                        isEventsInitialized = true
                    }.onFailure { error ->
                        Log.e("HomeViewModel", "Error al inicializar eventos", error)
                        // No es crítico si falla (pueden existir ya)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception al inicializar eventos", e)
            }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                Log.d("HomeViewModel", "Iniciando loadEvents")

                getEventsUseCase().collect { events ->
                    val currentUserId = auth.currentUser?.uid ?: ""
                    Log.d("HomeViewModel", "Eventos recibidos: ${events.size}, UserId: $currentUserId")

                    // Sugerencias: 5 eventos aleatorios
                    val suggested = events.shuffled().take(5)
                    _suggestedEvents.value = suggested
                    Log.d("HomeViewModel", "Eventos sugeridos: ${suggested.size}")

                    // Favoritos: eventos donde el usuario actual tiene "like"
                    val favorites = events.filter { event ->
                        val isFavorite = event.likes.containsKey(currentUserId)
                        Log.d("HomeViewModel", "Evento: ${event.title}, Es favorito: $isFavorite, Likes: ${event.likes}")
                        isFavorite
                    }
                    _favoriteEvents.value = favorites
                    Log.d("HomeViewModel", "Eventos favoritos: ${favorites.size}")

                    _uiState.update {
                        it.copy(
                            events = events,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading events", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar eventos: ${e.message}"
                    )
                }
            }
        }
    }

    fun toggleLike(eventId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(processingEventIds = it.processingEventIds + eventId) }

                val result = toggleLikeUseCase(eventId)
                result.onSuccess {
                    _uiState.update {
                        it.copy(
                            successMessage = "Evento actualizado",
                            processingEventIds = it.processingEventIds - eventId
                        )
                    }
                    // El flujo de getEvents() se actualizará automáticamente
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message,
                            processingEventIds = it.processingEventIds - eventId
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error toggling like", e)
                _uiState.update {
                    it.copy(
                        errorMessage = "Error: ${e.message}",
                        processingEventIds = it.processingEventIds - eventId
                    )
                }
            }
        }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
        _uiState.update { it.copy(isModalOpen = true) }
    }

    fun closeModal() {
        _selectedEvent.value = null
        _uiState.update { it.copy(isModalOpen = false) }
    }

    fun toggleSubscription(eventId: String) {
        _isSubscribed.update {
            val newMap = it.toMutableMap()
            newMap[eventId] = !(newMap[eventId] ?: false)
            newMap
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }

    /**
     * Busca un evento por su id en la lista ya cargada en uiState.events
     * y lo selecciona para que se abra el modal de detalles.
     */
    fun openEventFromDeepLink(eventId: String) {
        viewModelScope.launch {
            try {
                val events = _uiState.value.events
                Log.d("HomeViewModel", "openEventFromDeepLink id=$eventId, eventos=${events.size}")
                val event = events.find { it.id == eventId }
                if (event != null) {
                    selectEvent(event)
                } else {
                    Log.e("HomeViewModel", "Evento no encontrado para id=$eventId")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error en openEventFromDeepLink", e)
            }
        }
    }
}
