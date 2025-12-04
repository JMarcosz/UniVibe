package com.example.univibe.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.presentation.components.event.FavoritesSection
import com.example.univibe.presentation.components.event.SuggestionsSection
import com.example.univibe.presentation.event_details.EventDetailDialog

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp),
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
