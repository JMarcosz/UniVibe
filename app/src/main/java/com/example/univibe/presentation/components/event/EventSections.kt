package com.example.univibe.presentation.components.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.find_event.TextGray

@Composable
fun SuggestionsSection(
    events: List<Event>,
    favoriteEventIds: Set<String>,
    subscribedEventIds: Set<String>,
    processingEventIds: Set<String>,
    onEventClick: (Event)  -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Sugerencias",
            style = MaterialTheme.typography.headlineMedium,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            EmptyStateMessage(message = "No hay sugerencias disponibles")
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        isFavorite = event.id in favoriteEventIds,
                        isSubscribed = event.id in subscribedEventIds,
                        isProcessing = event.id in processingEventIds,
                        onCardClick = onEventClick,
                        onLikeClick = onLikeClick,
                        onSubscribeClick = onSubscribeClick,
                        modifier = Modifier.width(300.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesSection(
    events: List<Event>,
    favoriteEventIds: Set<String>,
    subscribedEventIds: Set<String>,
    processingEventIds: Set<String>,
    onEventClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            EmptyStateMessage(message = "No hay eventos favoritos aún")
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                events.forEach { event ->
                    EventCard(
                        event = event,
                        isFavorite = event.id in favoriteEventIds,
                        isSubscribed = event.id in subscribedEventIds,
                        isProcessing = event.id in processingEventIds,
                        onCardClick = onEventClick,
                        onLikeClick = onLikeClick,
                        onSubscribeClick = onSubscribeClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        textAlign = TextAlign.Center
    )
}

