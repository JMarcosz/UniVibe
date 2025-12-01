package com.example.univibe.presentation.find_event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.home.EventCard
import com.example.univibe.presentation.home.EventDetailModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindEventScreen(
    viewModel: FindEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredEvents by viewModel.filteredEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = { viewModel.setSearchActive(false) },
                active = uiState.isSearchActive,
                onActiveChange = viewModel::setSearchActive,
                placeholder = { Text("Buscar eventos") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Contenido cuando el SearchBar está activo
                EventsList(
                    events = filteredEvents,
                    subscriptions = subscriptions,
                    processingEventIds = uiState.processingEventIds,
                    onEventClick = viewModel::selectEvent,
                    onLikeClick = viewModel::toggleLike,
                    onSubscribeClick = viewModel::toggleSubscription,
                    isFavorite = viewModel::isFavorite
                )
            }

            // Lista de eventos cuando no está activo
            if (!uiState.isSearchActive) {
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    EventsList(
                        events = filteredEvents,
                        subscriptions = subscriptions,
                        processingEventIds = uiState.processingEventIds,
                        onEventClick = viewModel::selectEvent,
                        onLikeClick = viewModel::toggleLike,
                        onSubscribeClick = viewModel::toggleSubscription,
                        isFavorite = viewModel::isFavorite
                    )
                }
            }
        }

        // Mostrar mensaje de error si existe
        uiState.errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    IconButton(onClick = { viewModel.clearError() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
            ) {
                Text(error)
            }
        }

        // Modal de detalles del evento
        selectedEvent?.let { event ->
            EventDetailModal(
                event = event,
                isFavorite = viewModel.isFavorite(event),
                isSubscribed = viewModel.isSubscribed(event.id),
                isProcessing = event.id in uiState.processingEventIds,
                onDismiss = viewModel::closeModal,
                onLikeClick = viewModel::toggleLike,
                onSubscribeClick = viewModel::toggleSubscription
            )
        }
    }
}

@Composable
private fun EventsList(
    events: List<Event>,
    subscriptions: Map<String, Boolean>,
    processingEventIds: Set<String>,
    onEventClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit,
    isFavorite: (Event) -> Boolean
) {
    if (events.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se encontraron eventos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    isFavorite = isFavorite(event),
                    isSubscribed = subscriptions[event.id] ?: false,
                    isProcessing = event.id in processingEventIds,
                    onCardClick = onEventClick,
                    onLikeClick = onLikeClick,
                    onSubscribeClick = onSubscribeClick,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}