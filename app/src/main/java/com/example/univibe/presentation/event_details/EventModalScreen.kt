package com.example.univibe.presentation.event_details

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.theme.BackgroundWhite
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary
import com.example.univibe.presentation.theme.BtnTerciary
import com.example.univibe.presentation.theme.SurfaceLight
import com.example.univibe.presentation.theme.TerciaryBlue
import com.example.univibe.presentation.theme.TextGray
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*



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

    // Usamos Dialog con propiedades full-width para simular una pantalla modal moderna
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // Contenedor principal con forma redondeada y padding para efecto "tarjeta flotante grande"
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = BackgroundWhite,
            tonalElevation = 8.dp
        ) {
            Scaffold(
                containerColor = BackgroundWhite,
                topBar = {
                    CustomModalTopBar(onDismiss)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (val state = uiState) {
                        is EventDetailUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = BtnPrimary)
                            }
                        }
                        is EventDetailUiState.Success -> {
                            EventDetailBody(
                                event = state.event,
                                qrBitmap = state.qrBitmap,
                                currentUserId = currentUserId,
                                onLikeClick = { viewModel.toggleLike(eventId) },
                                onSubscribeClick = { viewModel.toggleSubscription(eventId) },
                                onDismiss = onDismiss
                            )
                        }
                        is EventDetailUiState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = state.message, color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Barra superior simple y limpia (Sin API Experimental)
@Composable
fun CustomModalTopBar(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Detalles del Evento",
            style = MaterialTheme.typography.headlineMedium,
            color = com.example.univibe.presentation.find_event.TextGray,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cerrar",
                tint = TextGray
            )
        }
    }
}

@Composable
private fun EventDetailBody(
    event: Event,
    qrBitmap: Bitmap?,
    currentUserId: String?,
    onLikeClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale("es", "ES")) }
    val timeFormat = remember { SimpleDateFormat("HH:mm a", Locale("es", "ES")) }

    val isLiked = currentUserId?.let { event.likes.containsKey(it) } ?: false
    val isSubscribed = currentUserId?.let { event.subscriptions.containsKey(it) } ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        // 1. Título y Categoría

        Text(
            text = event.title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextGray,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = event.category.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = BtnPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(BtnPrimary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Información Rápida (Fecha, Hora, Lugar)
        Row(modifier = Modifier.fillMaxWidth()) {
            // Columna Izquierda: Fechas
            Column(modifier = Modifier.weight(1f)) {
                InfoItemModern(
                    icon = Icons.Outlined.CalendarToday,
                    title = "Fecha",
                    subtitle = dateFormat.format(event.creationDate.toDate())
                )
                Spacer(modifier = Modifier.height(16.dp))
                InfoItemModern(
                    icon = Icons.Default.AccessTime,
                    title = "Hora",
                    subtitle = timeFormat.format(event.closingDate.toDate())
                )
            }

            // Columna Derecha: Ubicación
            Column(modifier = Modifier.weight(1f)) {
                InfoItemModern(
                    icon = Icons.Outlined.LocationOn,
                    title = "Ubicación",
                    subtitle = event.location
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp), color = SurfaceLight)

        // 3. Descripción
        Text(
            text = "Sobre el evento",
            style = MaterialTheme.typography.titleMedium,
            color = TextGray,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = event.description,
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray.copy(alpha = 0.8f),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Código QR
        if (qrBitmap != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = BtnPrimary.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Escanea para acceder",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. Botones de Acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón Me Gusta
            Button(
                onClick = onLikeClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLiked) BtnTerciary.copy(alpha = 0.8f) else BtnPrimary.copy(alpha = 0.1f),
                    contentColor = if (isLiked) Color.White else TextGray
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (isLiked) "Te gusta" else "Me gusta")
            }

            // Botón Suscribir
            Button(
                onClick = onSubscribeClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSubscribed) BtnSecondary.copy(alpha = 0.8f) else BtnSecondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isSubscribed) "Suscrito" else "Suscribirme",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Componente auxiliar para ítems de información (Diseño Plano)
@Composable
fun InfoItemModern(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BtnPrimary,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TextGray.copy(alpha = 0.6f)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}