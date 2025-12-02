package com.example.univibe.presentation.components.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.components.utils.formatDate
import com.example.univibe.presentation.components.utils.formatTime
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary

@Composable
fun EventCard(
    event: Event,
    isFavorite: Boolean,
    isSubscribed: Boolean = false,
    isProcessing: Boolean = false,
    onCardClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(event) },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Barra lateral indicadora
            EventCardSideBar(isSubscribed)

            // Contenido principal
            EventCardContent(
                event = event,
                isFavorite = isFavorite,
                isSubscribed = isSubscribed,
                isProcessing = isProcessing,
                onLikeClick = onLikeClick,
                onSubscribeClick = onSubscribeClick
            )
        }
    }
}

@Composable
private fun EventCardSideBar(isSubscribed : Boolean = false) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .height(200.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    bottomStart = 12.dp
                )
            )
            .background(if(isSubscribed) BtnSecondary else BtnPrimary)
    )
}

@Composable
private fun EventCardContent(
    event: Event,
    isFavorite: Boolean,
    isSubscribed: Boolean,
    isProcessing: Boolean,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        EventCardHeader(
            title = event.title,
            isFavorite = isFavorite,
            isProcessing = isProcessing,
            onLikeClick = { onLikeClick(event.id) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        EventCardInfo(event = event)

        Spacer(modifier = Modifier.height(12.dp))

        EventCardSubscribeButton(
            isSubscribed = isSubscribed,
            isProcessing = isProcessing,
            onClick = { onSubscribeClick(event.id) }
        )
    }
}

@Composable
private fun EventCardHeader(
    title: String,
    isFavorite: Boolean,
    isProcessing: Boolean,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        IconButton(
            onClick = onLikeClick,
            modifier = Modifier.size(32.dp),
            enabled = !isProcessing
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos",
                tint = if (isFavorite) Color(0xFFFFC107) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun EventCardInfo(event: Event) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EventCardInfoRow(
            icon = Icons.Filled.DateRange,
            text = formatDate(event.creationDate),
            contentDescription = "Fecha"
        )

        EventCardInfoRow(
            icon = Icons.Filled.AccessTime,
            text = formatTime(event.closingDate),
            contentDescription = "Hora"
        )

        EventCardInfoRow(
            icon = Icons.Filled.LocationOn,
            text = event.location,
            contentDescription = "Ubicación"
        )
    }
}

@Composable
private fun EventCardInfoRow(
    icon: ImageVector,
    text: String,
    contentDescription: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
private fun EventCardSubscribeButton(
    isSubscribed: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isProcessing,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSubscribed) BtnSecondary else BtnPrimary,
            contentColor = Color.White
        )
    ) {
        Text(text = if (isSubscribed) "Suscrito" else "Suscribirse")
    }
}

