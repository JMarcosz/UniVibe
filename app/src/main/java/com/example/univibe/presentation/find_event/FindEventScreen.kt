package com.example.univibe.presentation.find_event

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.domain.model.Event
import com.example.univibe.presentation.components.event.EventCard
import com.example.univibe.presentation.event_details.EventDetailDialog
import com.example.univibe.domain.use_case.event.DateFilter

// --- Paleta de Colores ---
val TextGray = Color(0xFF262626)
val BtnSecondary = Color(0XFF031C34) // Usado para botones y tabs activos
val BackgroundWhite = Color.White
val TextFieldColor = Color(0xFFF9F9F9)
val LightGray = Color(0xFFF7F7F7)

@Composable
fun FindEventScreen(
    viewModel: FindEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredEvents by viewModel.filteredEvents.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val eventCategories by viewModel.eventCategories.collectAsState()

    // Estados locales para filtros adicionales (UI Only)
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Fecha, 1: Categoría
    var selectedFilterChip by remember { mutableStateOf("Todos") }

    // Lógica de filtrado local adicional (sobre los ya filtrados por búsqueda en VM)
    val finalFilteredEvents = remember(filteredEvents, selectedFilterChip, selectedTab) {
        when {
            selectedFilterChip == "Todos" -> filteredEvents
            selectedTab == 0 -> {
                // Filtro por fecha usando el caso de uso
                val dateFilter = when (selectedFilterChip) {
                    "Hoy" -> DateFilter.TODAY
                    "Mañana" -> DateFilter.TOMORROW
                    "Esta semana" -> DateFilter.THIS_WEEK
                    "Próximo mes" -> DateFilter.NEXT_MONTH
                    else -> DateFilter.ALL
                }
                viewModel.filterByDate(dateFilter)
            }
            else -> {
                // Filtro por categoría usando el caso de uso
                viewModel.filterByCategory(selectedFilterChip)
            }
        }
    }

    Scaffold(
        containerColor = BackgroundWhite,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundWhite)
                    .padding(top = 20.dp, bottom = 8.dp)
            ) {
                // Título
                Text(
                    text = "Explorar Eventos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buscador
                SearchBarCustom(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs de Filtros (Fecha / Categoría)
                FilterTabsSection(
                    selectedTab = selectedTab,
                    onTabSelected = {
                        selectedTab = it
                        selectedFilterChip = "Todos" // Reset chip al cambiar tab
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Chips dinámicos según el Tab seleccionado
                FilterChipsRow(
                    currentTab = selectedTab,
                    selectedChip = selectedFilterChip,
                    onChipSelected = { selectedFilterChip = it },
                    categories = eventCategories
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundWhite)
        ) {
            when {
                uiState.isLoading && filteredEvents.isEmpty() -> LoadingView()
                uiState.errorMessage != null && filteredEvents.isEmpty() -> ErrorView(
                    uiState.errorMessage ?: "",
                    onRetry = { /* Lógica de reintento si es necesaria */ }
                )

                // Lista Vacia tras filtrar
                finalFilteredEvents.isEmpty() && uiState.searchQuery.isNotEmpty() -> EmptySearchView()

                // Lista Vacia general
                filteredEvents.isEmpty() -> EmptyDataView()

                else -> {
                    EventListContent(
                        events = finalFilteredEvents,
                        processingEventIds = uiState.processingEventIds,
                        onEventClick = viewModel::selectEvent,
                        onSubscribeClick = { event -> viewModel.toggleSubscription(event.id) },
                        isFavorite = viewModel::isFavorite,
                        isSubscribed = viewModel::isSubscribed
                    )
                }
            }
        }

        // Modal
        selectedEvent?.let { event ->
            EventDetailDialog(
                eventId = event.id,
                onDismiss = viewModel::closeModal
            )
        }
    }
}

// --- Componentes de UI ---

@Composable
fun SearchBarCustom(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar eventos, categorías...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BtnSecondary) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Borrar", tint = Color.Gray)
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TextFieldColor,
            unfocusedContainerColor = TextFieldColor,
            disabledContainerColor = TextFieldColor,
            cursorColor = BtnSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp)
    )
}

@Composable
fun FilterTabsSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        FilterTabItem(
            text = "Por Fecha",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        FilterTabItem(
            text = "Por Categoría",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) BtnSecondary else LightGray)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextGray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun FilterChipsRow(
    currentTab: Int,
    selectedChip: String,
    onChipSelected: (String) -> Unit,
    categories: List<String>
) {
    // Definimos las opciones según el tab seleccionado
    val options = if (currentTab == 0) {
        listOf("Todos", "Hoy", "Mañana", "Esta semana", "Próximo mes")
    } else {
        categories // Usar categorías dinámicas desde Firebase
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(options) { option ->
            val isSelected = selectedChip == option
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) BtnSecondary.copy(alpha = 0.1f) else Color.Transparent,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) BtnSecondary else Color.LightGray
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onChipSelected(option) }
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) BtnSecondary else TextGray.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun EventListContent(
    events: List<Event>,
    processingEventIds: Set<String>,
    onEventClick: (Event) -> Unit,
    onSubscribeClick: (Event) -> Unit,
    isFavorite: (Event) -> Boolean,
    isSubscribed: (String) -> Boolean
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Lista de eventos
        items(events, key = { it.id }) { event ->
            EventCard(
                event = event,
                isFavorite = isFavorite(event),
                isSubscribed = isSubscribed(event.id),
                isProcessing = event.id in processingEventIds,
                onCardClick = onEventClick,
                onLikeClick = { eventId -> /* Manejar like */ },
                onSubscribeClick = { eventId -> onSubscribeClick(event) }
            )
        }
        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}


// --- Estados de Carga y Error ---

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = BtnSecondary)
    }
}

@Composable
fun ErrorView(msg: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error: $msg", color = Color.Red)
        TextButton(onClick = onRetry) { Text("Reintentar", color = BtnSecondary) }
    }
}

@Composable
fun EmptySearchView() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.SearchOff, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No se encontraron eventos", color = TextGray)
    }
}

@Composable
fun EmptyDataView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay eventos disponibles", color = TextGray)
    }
}


