package com.example.univibe.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.univibe.presentation.theme.TextGray

// Modelo de datos para eventos
data class EventCard(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val dayOfWeek: String,
    val backgroundColor: Color,
    val isFavorite: Boolean = false
)

// Datos estáticos iniciales
object HomeScreenState {
    val suggestionsEvents = listOf(
        EventCard(
            id = "1",
            title = "Visita a orfanato",
            date = "15/11",
            time = "07:30 AM",
            dayOfWeek = "Sábado",
            backgroundColor = Color(0xFFB71C1C),
            isFavorite = true
        ),
        EventCard(
            id = "2",
            title = "Actividad Comunitaria",
            date = "16/11",
            time = "09:00 AM",
            dayOfWeek = "Domingo",
            backgroundColor = Color(0xFF8B0000),
            isFavorite = false
        ),
        EventCard(
            id = "3",
            title = "Jornada de Voluntariado",
            date = "17/11",
            time = "08:30 AM",
            dayOfWeek = "Lunes",
            backgroundColor = Color(0xFFC62828),
            isFavorite = false
        ),
        EventCard(
            id = "4",
            title = "Evento Especial",
            date = "18/11",
            time = "06:00 PM",
            dayOfWeek = "Martes",
            backgroundColor = Color(0xFFD32F2F),
            isFavorite = false
        )
    )

    val favoriteEvents = listOf(
        EventCard(
            id = "5",
            title = "Limpieza de parques",
            date = "15/11",
            time = "07:30 AM",
            dayOfWeek = "Viernes",
            backgroundColor = Color(0xFF1B5E20),
            isFavorite = true
        ),
        EventCard(
            id = "6",
            title = "Recolección de basura en Boca Chica",
            date = "15/11",
            time = "07:30 AM",
            dayOfWeek = "Sábado",
            backgroundColor = Color(0xFF37474F),
            isFavorite = true
        ),
        EventCard(
            id = "7",
            title = "Charla para emprendedores",
            date = "15/11",
            time = "04:30 PM",
            dayOfWeek = "Martes",
            backgroundColor = Color(0xFFC62828),
            isFavorite = true
        )
    )
}

@Preview
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Sugerencias Section
        SuggestionsSection(events = HomeScreenState.suggestionsEvents)

        Spacer(modifier = Modifier.height(32.dp))

        // Favoritos Section
        FavoritesSection(events = HomeScreenState.favoriteEvents)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Universidad Dominico Americano",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextGray
        )
    }
}

@Composable
fun SuggestionsSection(events: List<EventCard>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sugerencias",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = TextGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCardItem(
                    event = event,
                    modifier = Modifier.size(width = 280.dp, height = 140.dp)
                )
            }
        }

        // Pagination dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(events.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == 0) TextGray else Color.LightGray
                        )
                )
                if (index < events.size - 1) Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}

@Composable
fun FavoritesSection(events: List<EventCard>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Favoritos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        events.forEach { event ->
            EventCardItem(
                event = event,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )
        }
    }
}

@Composable
fun EventCardItem(
    event: EventCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = event.backgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Contenido del card
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(0.85f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = event.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${event.dayOfWeek} - ${event.date} a las ${event.time}",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2
                )
            }

            // Ícono de favorito
            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = if (event.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White.copy(alpha = 0.9f)
                )
            }

            // Decoración de onda en la parte inferior (placeholder para futuras imágenes)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(60.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(30.dp)
                    )
            )
        }
    }
}