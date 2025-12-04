package com.example.univibe.domain.use_case.event

import com.example.univibe.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las categorías únicas de eventos.
 * Retorna una lista que siempre incluye "Todos" como primera opción.
 */
class GetEventCategoriesUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getEvents().map { events ->
            val categories = events
                .map { it.category }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

            // Siempre incluir "Todos" como primera opción
            listOf("Todos") + categories
        }
    }
}

