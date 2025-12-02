package com.example.univibe.presentation.events.modal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.components.*
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailDialog(
    eventId: String,
    onDismiss: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp
        ) {
            when (val state = uiState) {
                is EventDetailUiState.Loading -> {
                    LoadingState()
                }
                is EventDetailUiState.Success -> {
                    EventDetailContent(
                        event = state.event,
                        qrBitmap = state.qrBitmap,
                        currentUserId = currentUserId,
                        onLikeClick = { viewModel.toggleLike(eventId) },
                        onSubscribeClick = { viewModel.toggleSubscription(eventId) },
                        onDismiss = onDismiss
                    )
                }
                is EventDetailUiState.Error -> {
                    ErrorState(message = state.message)
                }
            }
        }
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    qrBitmap: android.graphics.Bitmap?,
    currentUserId: String?,
    onLikeClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "ES")) }

    val isLiked = currentUserId?.let { event.likes.containsKey(it) } ?: false
    val isSubscribed = currentUserId?.let { event.subscriptions.containsKey(it) } ?: false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        EventHeaderSection(
            title = event.title,
            category = event.category
        )

        EventDetailsSection(
            event = event,
            dateFormat = dateFormat,
            qrBitmap = qrBitmap,
            isLiked = isLiked,
            isSubscribed = isSubscribed,
            onLikeClick = onLikeClick,
            onSubscribeClick = onSubscribeClick,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun EventDetailsSection(
    event: Event,
    dateFormat: SimpleDateFormat,
    qrBitmap: android.graphics.Bitmap?,
    isLiked: Boolean,
    isSubscribed: Boolean,
    onLikeClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        EventDescription(description = event.description)

        Spacer(modifier = Modifier.height(24.dp))

        EventInformation(
            event = event,
            dateFormat = dateFormat
        )

        Spacer(modifier = Modifier.height(24.dp))

        QrCodeSection(qrBitmap = qrBitmap)

        Spacer(modifier = Modifier.height(24.dp))

        EventActionButtons(
            isLiked = isLiked,
            isSubscribed = isSubscribed,
            onLikeClick = onLikeClick,
            onSubscribeClick = onSubscribeClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        DismissButton(onClick = onDismiss)
    }
}

@Composable
private fun EventDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
private fun EventInformation(
    event: Event,
    dateFormat: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        EventInfoCard(
            icon = Icons.Default.LocationOn,
            label = "Ubicación",
            value = event.location
        )

        EventInfoCard(
            icon = Icons.Default.CalendarToday,
            label = "Fecha de inicio",
            value = dateFormat.format(event.creationDate.toDate())
        )

        EventInfoCard(
            icon = Icons.Default.EventAvailable,
            label = "Fecha de cierre",
            value = dateFormat.format(event.closingDate.toDate())
        )
    }
}

@Composable
private fun DismissButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Cerrar")
    }
}

