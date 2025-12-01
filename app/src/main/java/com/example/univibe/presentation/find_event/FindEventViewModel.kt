package com.example.univibe.presentation.find_event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.use_case.events.GetEventsUseCase
import com.example.univibe.domain.use_case.events.SearchEventsUseCase
import com.example.univibe.domain.use_case.events.ToggleLikeUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindEventViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val searchEventsUseCase: SearchEventsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindEventUiState())
    val uiState: StateFlow<FindEventUiState> = _uiState.asStateFlow()

    private val _allEvents = MutableStateFlow<List<Event>>(emptyList())

    private val _filteredEvents = MutableStateFlow<List<Event>>(emptyList())
    val filteredEvents: StateFlow<List<Event>> = _filteredEvents.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    // Mapa de suscripciones locales (eventId -> isSubscribed)
    private val _subscriptions = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val subscriptions: StateFlow<Map<String, Boolean>> = _subscriptions.asStateFlow()

    init {
        loadEvents()
        observeSearchQuery()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                Log.d("FindEventViewModel", "Cargando eventos...")

                getEventsUseCase().collect { events ->
                    Log.d("FindEventViewModel", "Eventos recibidos: ${events.size}")
                    _allEvents.value = events
                    applySearch()
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("FindEventViewModel", "Error cargando eventos", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar eventos: ${e.message}"
                    )
                }
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            combine(_allEvents, _uiState) { events, state ->
                events to state.searchQuery
            }.collect { (events, query) ->
                applySearch()
            }
        }
    }

    private fun applySearch() {
        val query = _uiState.value.searchQuery
        val events = _allEvents.value
        val filtered = searchEventsUseCase(events, query)
        _filteredEvents.value = filtered
        Log.d("FindEventViewModel", "Búsqueda: '$query', Resultados: ${filtered.size}")
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applySearch()
    }

    fun setSearchActive(active: Boolean) {
        _uiState.update { it.copy(isSearchActive = active) }
    }

    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun closeModal() {
        _selectedEvent.value = null
    }

    fun toggleLike(eventId: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(processingEventIds = it.processingEventIds + eventId)
                }

                Log.d("FindEventViewModel", "Toggle like para evento: $eventId")
                val result = toggleLikeUseCase(eventId)

                result.onSuccess {
                    Log.d("FindEventViewModel", "Like toggleado correctamente")
                }.onFailure { error ->
                    Log.e("FindEventViewModel", "Error al toggle like", error)
                    _uiState.update {
                        it.copy(errorMessage = "Error al actualizar favorito: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("FindEventViewModel", "Exception al toggle like", e)
                _uiState.update {
                    it.copy(errorMessage = "Error: ${e.message}")
                }
            } finally {
                _uiState.update {
                    it.copy(processingEventIds = it.processingEventIds - eventId)
                }
            }
        }
    }

    fun toggleSubscription(eventId: String) {
        _subscriptions.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            val currentValue = newMap[eventId] ?: false
            newMap[eventId] = !currentValue
            Log.d("FindEventViewModel", "Suscripción toggleada: $eventId = ${!currentValue}")
            newMap
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // Helper para verificar si un evento es favorito
    fun isFavorite(event: Event): Boolean {
        val currentUserId = auth.currentUser?.uid ?: return false
        return event.likes.containsKey(currentUserId)
    }

    // Helper para verificar si un evento está suscrito
    fun isSubscribed(eventId: String): Boolean {
        return _subscriptions.value[eventId] ?: false
    }
}

