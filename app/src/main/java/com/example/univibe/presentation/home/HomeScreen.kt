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
import com.example.univibe.presentation.components.event.modal.EventDetailDialog
import com.example.univibe.presentation.components.event.FavoritesSection
import com.example.univibe.presentation.components.event.SuggestionsSection

@Composable
fun HomeScreen(deepLinkEventId: String? = null) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val suggestedEvents by viewModel.suggestedEvents.collectAsState()
    val favoriteEvents by viewModel.favoriteEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()

    val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

    val favoriteEventIds = favoriteEvents.map { it.id }.toSet()
    // Calcular subscribedEventIds directamente desde los eventos en Firebase
    val subscribedEventIds = uiState.events.filter { event ->
        currentUserId?.let { event.subscriptions.containsKey(it) } ?: false
    }.map { it.id }.toSet()

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
        selectedEvent?.let { event ->
            EventDetailDialog(
                eventId = event.id,
                onDismiss = viewModel::closeModal
            )
        }
    }
}
