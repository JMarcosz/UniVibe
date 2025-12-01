package com.example.univibe.domain.use_case.events

import com.example.univibe.domain.model.Event
import javax.inject.Inject

class SearchEventsUseCase @Inject constructor() {
    operator fun invoke(events: List<Event>, query: String): List<Event> {
        if (query.isBlank()) return events

        val lowercaseQuery = query.lowercase()
        return events.filter { event ->
            event.title.lowercase().contains(lowercaseQuery) ||
            event.description.lowercase().contains(lowercaseQuery) ||
            event.location.lowercase().contains(lowercaseQuery) ||
            event.category.lowercase().contains(lowercaseQuery)
        }
    }
}

