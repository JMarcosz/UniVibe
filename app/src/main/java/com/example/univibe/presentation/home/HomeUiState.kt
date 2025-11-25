package com.example.univibe.presentation.home

import com.example.univibe.domain.model.Event


data class HomeUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isModalOpen: Boolean = false,
    val processingEventIds: Set<String> = emptySet(),
    val successMessage: String? = null
)

data class CreateEventForm(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val category: String = ""
)

