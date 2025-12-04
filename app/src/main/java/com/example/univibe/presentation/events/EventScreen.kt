package com.example.univibe.presentation.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.presentation.components.event.EventCard
import com.example.univibe.presentation.event_details.EventDetailDialog

/**
 * Pantalla que muestra los eventos a los que el usuario está suscrito.
 * Reutiliza componentes existentes: EventCard y EventDetailDialog.
 */
@Composable
fun EventScreen(
    viewModel: EventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Estado de carga
            uiState.isLoading && uiState.subscribedEvents.isEmpty() -> {
                LoadingState()
            }

            // Estado de error
            uiState.error != null && uiState.subscribedEvents.isEmpty() -> {
                ErrorState(
                    message = uiState.error ?: "Error desconocido",
                    onRetry = viewModel::refresh
                )
            }

            // Lista vacía (sin eventos suscritos)
            uiState.subscribedEvents.isEmpty() -> {
                EmptyState()
            }

            // Lista de eventos suscritos
            else -> {
                EventList(
                    events = uiState.subscribedEvents,
                    processingEventIds = uiState.processingEventIds,
                    onEventClick = viewModel::openEventDetail,
                    onLikeClick = viewModel::toggleLike,
                    onSubscribeClick = viewModel::toggleSubscription,
                    isFavorite = viewModel::isFavorite
                )
            }
        }

        // Modal de detalles del evento
        uiState.selectedEvent?.let { event ->
            EventDetailDialog(
                eventId = event.id,
                onDismiss = viewModel::closeModal
            )
        }
    }
}

@Composable
private fun EventList(
    events: List<com.example.univibe.domain.model.Event>,
    processingEventIds: Set<String>,
    onEventClick: (com.example.univibe.domain.model.Event) -> Unit,
    onLikeClick: (com.example.univibe.domain.model.Event) -> Unit,
    onSubscribeClick: (com.example.univibe.domain.model.Event) -> Unit,
    isFavorite: (com.example.univibe.domain.model.Event) -> Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Mis Eventos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${events.size} ${if (events.size == 1) "evento suscrito" else "eventos suscritos"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // Lista de eventos
        items(events, key = { it.id }) { event ->
            EventCard(
                event = event,
                isFavorite = isFavorite(event),
                isSubscribed = true, // Todos los eventos aquí están suscritos
                isProcessing = event.id in processingEventIds,
                onCardClick = onEventClick,
                onLikeClick = { onLikeClick(event) },
                onSubscribeClick = { onSubscribeClick(event) }
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = "📅",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "No tienes eventos suscritos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Explora eventos y suscríbete para verlos aquí",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}