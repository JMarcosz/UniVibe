package com.example.univibe.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

// Formatea la fecha desde Firestore Timestamp
fun formatDate(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(date)
}

// Formatea la hora desde Firestore Timestamp
fun formatTime(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(date)
}

@Composable
fun EventCard(
    event: Event,
    isFavorite: Boolean,
    onCardClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSubscribed: Boolean = false,
    isProcessing: Boolean = false
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
            // Barra azul redondeada en el lado izquierdo
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp
                    ))
                    .background(Color(0xFF1976D2))  // Azul primario
            )

            // Contenido del card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Encabezado con título y estrella
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 2
                    )

                    IconButton(
                        onClick = { onLikeClick(event.id) },
                        modifier = Modifier.size(32.dp),
                        enabled = !isProcessing
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFFFC107) else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Fecha",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = formatDate(event.creationDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = "Hora",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = formatTime(event.closingDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ubicación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ubicación",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de suscribirse
                Button(
                    onClick = { onSubscribeClick(event.id) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    Text(if (isSubscribed) "Cancelar" else "Suscribirse")
                }
            }
        }
    }
}

@Composable
fun EventDetailModal(
    event: Event?,
    isFavorite: Boolean,
    isSubscribed: Boolean = false,
    isProcessing: Boolean = false,
    onDismiss: () -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit
) {
    if (event != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Encabezado con botón cerrar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Detalles del Evento",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Gray
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Título
                        Column {
                            Text(
                                text = "Título",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Descripción
                        Column {
                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Fecha
                        Column {
                            Text(
                                text = "Fecha",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = formatDate(event.creationDate),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Hora
                        Column {
                            Text(
                                text = "Hora",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = formatTime(event.closingDate),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Ubicación
                        Column {
                            Text(
                                text = "Ubicación",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = event.location,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón de favorito
                            Button(
                                onClick = { onLikeClick(event.id) },
                                modifier = Modifier
                                    .weight(1f),
                                enabled = !isProcessing
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorito",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(if (isFavorite) "Favorito" else "Añadir")
                            }

                            // Botón de suscribirse
                            Button(
                                onClick = { onSubscribeClick(event.id) },
                                modifier = Modifier
                                    .weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(isSubscribed) BtnPrimary else BtnSecondary
                                ),
                                enabled = !isProcessing
                            ) {
                                Text(if (isSubscribed) "Cancelar" else "Suscribirse")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionsSection(
    events: List<Event>,
    favoriteEventIds: Set<String>,
    subscribedEventIds: Set<String>,
    processingEventIds: Set<String>,
    onEventClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Sugerencias",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
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

@Composable
fun FavoritesSection(
    events: List<Event>,
    favoriteEventIds: Set<String>,
    subscribedEventIds: Set<String>,
    processingEventIds: Set<String>,
    onEventClick: (Event) -> Unit,
    onLikeClick: (String) -> Unit,
    onSubscribeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Favoritos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            Text(
                text = "No hay eventos favoritos aún",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = TextAlign.Center
            )
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

