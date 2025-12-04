package com.example.univibe.domain.use_case.event

import com.example.univibe.domain.model.Event
import javax.inject.Inject

/**
 * Caso de uso para filtrar eventos por categoría.
 */
class FilterEventsByCategoryUseCase @Inject constructor() {

    operator fun invoke(events: List<Event>, category: String): List<Event> {
        return if (category.equals("Todos", ignoreCase = true)) {
            events
        } else {
            events.filter { event ->
                event.category.equals(category, ignoreCase = true)
            }
        }
    }
}

