package com.example.univibe.presentation.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(deepLinkEventId: String? = null) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val suggestedEvents by viewModel.suggestedEvents.collectAsState()
    val favoriteEvents by viewModel.favoriteEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val isSubscribed by viewModel.isSubscribed.collectAsState()

    // Crear set de IDs de eventos favoritos para verificación rápida
    val favoriteEventIds = favoriteEvents.map { it.id }.toSet()
    val subscribedEventIds = isSubscribed.filterValues { it }.keys

    // Manejar deep link para abrir evento automáticamente
    LaunchedEffect(deepLinkEventId) {
        if (deepLinkEventId != null) {
            viewModel.openEventFromDeepLink(deepLinkEventId)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 16.dp)
            ) {
                // Sección de Sugerencias
                item {
                    if (suggestedEvents.isNotEmpty()) {
                        SuggestionsSection(
                            events = suggestedEvents,
                            favoriteEventIds = favoriteEventIds,
                            subscribedEventIds = subscribedEventIds,
                            processingEventIds = uiState.processingEventIds,
                            onEventClick = viewModel::selectEvent,
                            onLikeClick = viewModel::toggleLike,
                            onSubscribeClick = viewModel::toggleSubscription
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }

                // Sección de Favoritos
                item {
                    FavoritesSection(
                        events = favoriteEvents,
                        favoriteEventIds = favoriteEventIds,
                        subscribedEventIds = subscribedEventIds,
                        processingEventIds = uiState.processingEventIds,
                        onEventClick = viewModel::selectEvent,
                        onLikeClick = viewModel::toggleLike,
                        onSubscribeClick = viewModel::toggleSubscription
                    )
                }
            }
        }

        // Modal de detalles del evento
        if (selectedEvent != null) {
            EventDetailModal(
                event = selectedEvent,
                isFavorite = selectedEvent?.id in favoriteEventIds,
                isSubscribed = selectedEvent?.id in subscribedEventIds,
                isProcessing = selectedEvent?.id in uiState.processingEventIds,
                onDismiss = viewModel::closeModal,
                onLikeClick = viewModel::toggleLike,
                onSubscribeClick = viewModel::toggleSubscription
            )
        }
    }
}
