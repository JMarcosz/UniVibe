package com.example.univibe.presentation.find_event

data class FindEventUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val errorMessage: String? = null,
    val processingEventIds: Set<String> = emptySet()
)

