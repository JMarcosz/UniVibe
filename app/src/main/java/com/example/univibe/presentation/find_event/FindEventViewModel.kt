package com.example.univibe.presentation.find_event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.use_case.events.GetEventsUseCase
import com.example.univibe.domain.use_case.events.SearchEventsUseCase
import com.example.univibe.domain.use_case.events.ToggleEventSubscriptionUseCase
import com.example.univibe.domain.use_case.events.ToggleLikeUseCase
import com.example.univibe.domain.use_case.event.GetEventCategoriesUseCase
import com.example.univibe.domain.use_case.event.FilterEventsByDateUseCase
import com.example.univibe.domain.use_case.event.FilterEventsByCategoryUseCase
import com.example.univibe.domain.use_case.event.DateFilter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindEventViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val searchEventsUseCase: SearchEventsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase,
    private val toggleSubscriptionUseCase: ToggleEventSubscriptionUseCase,
    private val getEventCategoriesUseCase: GetEventCategoriesUseCase,
    private val filterByDateUseCase: FilterEventsByDateUseCase,
    private val filterByCategoryUseCase: FilterEventsByCategoryUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindEventUiState())
    val uiState: StateFlow<FindEventUiState> = _uiState.asStateFlow()

    private val _allEvents = MutableStateFlow<List<Event>>(emptyList())

    private val _filteredEvents = MutableStateFlow<List<Event>>(emptyList())
    val filteredEvents: StateFlow<List<Event>> = _filteredEvents.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _eventCategories = MutableStateFlow<List<String>>(listOf("Todos"))
    val eventCategories: StateFlow<List<String>> = _eventCategories.asStateFlow()

    init {
        loadEvents()
        loadCategories()
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

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                Log.d("FindEventViewModel", "Cargando categorías...")
                getEventCategoriesUseCase().collect { categories ->
                    Log.d("FindEventViewModel", "Categorías recibidas: $categories")
                    _eventCategories.value = categories
                }
            } catch (e: Exception) {
                Log.e("FindEventViewModel", "Error cargando categorías", e)
            }
        }
    }

    private fun applySearch() {
        val query = _uiState.value.searchQuery
        val events = _allEvents.value
        val filtered = searchEventsUseCase(events, query)

        // Aplicar filtros adicionales después de la búsqueda
        val finalFiltered = applyCurrentFilters(filtered)
        _filteredEvents.value = finalFiltered

        Log.d("FindEventViewModel", "Búsqueda: '$query', Resultados: ${finalFiltered.size}")
    }

    private fun applyCurrentFilters(events: List<Event>): List<Event> {
        val selectedChip = _uiState.value.selectedFilterChip
        val selectedTab = _uiState.value.selectedTab

        return when {
            selectedChip == "Todos" -> events
            selectedTab == 0 -> {
                // Filtro por fecha
                val dateFilter = when (selectedChip) {
                    "Hoy" -> DateFilter.TODAY
                    "Mañana" -> DateFilter.TOMORROW
                    "Esta semana" -> DateFilter.THIS_WEEK
                    "Próximo mes" -> DateFilter.NEXT_MONTH
                    else -> DateFilter.ALL
                }
                filterByDateUseCase(events, dateFilter)
            }
            else -> {
                // Filtro por categoría
                filterByCategoryUseCase(events, selectedChip)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applySearch()
    }

    fun onTabSelected(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab, selectedFilterChip = "Todos") }
        applySearch()
    }

    fun onFilterChipSelected(chip: String) {
        _uiState.update { it.copy(selectedFilterChip = chip) }
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
        viewModelScope.launch {
            // Agregar a processingEventIds
            _uiState.update {
                it.copy(processingEventIds = it.processingEventIds + eventId)
            }

            try {
                Log.d("FindEventViewModel", "Iniciando toggle subscription para evento: $eventId")
                val result = toggleSubscriptionUseCase(eventId)

                result.onSuccess {
                    Log.d("FindEventViewModel", "Toggle subscription exitoso para evento: $eventId")
                }.onFailure { error ->
                    Log.e("FindEventViewModel", "Error al toggle subscription: ${error.message}")
                    _uiState.update {
                        it.copy(errorMessage = "Error al cambiar suscripción: ${error.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("FindEventViewModel", "Excepción en toggleSubscription", e)
                _uiState.update {
                    it.copy(errorMessage = "Error al cambiar suscripción")
                }
            } finally {
                // Remover de processingEventIds
                _uiState.update {
                    it.copy(processingEventIds = it.processingEventIds - eventId)
                }
            }
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
        val currentUserId = auth.currentUser?.uid ?: return false
        // Buscar el evento en la lista de eventos y verificar subscriptions
        val event = _allEvents.value.find { it.id == eventId }
        return event?.subscriptions?.containsKey(currentUserId) ?: false
    }
}

