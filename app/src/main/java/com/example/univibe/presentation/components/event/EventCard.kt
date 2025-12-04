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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary
import com.example.univibe.presentation.theme.TextGray
import java.text.SimpleDateFormat
import java.util.*

/**
 * EventCard - Componente reutilizable para mostrar eventos con diseño moderno.
 * Diseño Material Design 3 con barra lateral indicadora de estado.
 */
@Composable
fun EventCard(
    event: Event,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    isSubscribed: Boolean = false,
    isProcessing: Boolean = false,
    onCardClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM", Locale.forLanguageTag("es-ES")) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-ES")) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(event) },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Barra lateral indicadora de estado
            EventCardSideBar(isSubscribed)

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header: Título // Categoría // Icono de favorito
                EventCardHeader(
                    title = event.title,
                    category = event.category,
                    isFavorite = isFavorite,
                    isProcessing = isProcessing,
                    onLikeClick = { onLikeClick(event.id) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Información del evento: Fecha, Hora, Ubicación
                EventCardInfoSection(
                    date = dateFormat.format(event.creationDate.toDate()),
                    time = timeFormat.format(event.closingDate.toDate()),
                    location = event.location
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de acción: Suscribirse/Suscrito
                EventCardSubscribeButton(
                    isSubscribed = isSubscribed,
                    isProcessing = isProcessing,
                    onClick = { onSubscribeClick(event.id) }
                )
            }
        }
    }
}

// --- Componentes Internos ---

/**
 * Barra lateral vertical que indica el estado del evento.
 * BtnSecondary para eventos suscritos, BtnPrimary para eventos no suscritos.
 */
@Composable
private fun EventCardSideBar(isSubscribed: Boolean) {
    Box(
        modifier = Modifier
            .width(6.dp)
            .fillMaxHeight()
            .background(
                color = if (isSubscribed) BtnSecondary else BtnPrimary,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    bottomStart = 16.dp
                )
            )
    )
}

/**
 * Header del card: Título, Categoría e Icono de favorito
 */
@Composable
private fun EventCardHeader(
    title: String,
    category: String,
    isFavorite: Boolean,
    isProcessing: Boolean,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título y categoría
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp
            )

            // Badge de categoría
            Text(
                text = category.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall,
                color = BtnPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier
                    .background(
                        color = BtnPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        // Icono de favorito
        IconButton(
            onClick = onLikeClick,
            modifier = Modifier.size(40.dp),
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

/**
 * Sección de información: Fecha, Hora, Ubicación
 */
@Composable
private fun EventCardInfoSection(
    date: String,
    time: String,
    location: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EventCardInfoRow(
            icon = Icons.Filled.DateRange,
            label = "Fecha",
            value = date
        )

        EventCardInfoRow(
            icon = Icons.Filled.AccessTime,
            label = "Hora",
            value = time
        )

        EventCardInfoRow(
            icon = Icons.Filled.LocationOn,
            label = "Ubicación",
            value = location
        )
    }
}

/**
 * Fila de información con icono, label y valor
 */
@Composable
private fun EventCardInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = BtnPrimary,
            modifier = Modifier.size(18.dp)
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontSize = 10.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Botón de suscripción
 */
@Composable
private fun EventCardSubscribeButton(
    isSubscribed: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        enabled = !isProcessing,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSubscribed) Color.Gray else BtnSecondary,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray.copy(alpha = 0.6f),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = if (isSubscribed) "Suscrito" else "Suscribirme",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

