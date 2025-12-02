package com.example.univibe.domain.use_case.events

import android.util.Log
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.repository.EventRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para obtener solo los eventos a los que el usuario está suscrito.
 *
 * Filtra los eventos basándose en el campo `subscriptions` de cada evento,
 * verificando si el userId actual está presente en el mapa de suscripciones.
 */
class GetSubscribedEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val auth: FirebaseAuth
) {
    /**
     * Obtiene un Flow de eventos suscritos por el usuario actual.
     *
     * @return Flow<List<Event>> - Lista de eventos donde el usuario está suscrito
     */
    operator fun invoke(): Flow<List<Event>> {
        val currentUserId = auth.currentUser?.uid

        Log.d("GetSubscribedEvents", "Usuario actual: $currentUserId")

        return if (currentUserId != null) {
            eventRepository.getEvents().map { events ->
                Log.d("GetSubscribedEvents", "Total de eventos recibidos: ${events.size}")

                val subscribedEvents = events.filter { event ->
                    val isSubscribed = event.subscriptions.containsKey(currentUserId)
                    Log.d("GetSubscribedEvents", "Evento: ${event.title}, Suscripciones: ${event.subscriptions}, Usuario suscrito: $isSubscribed")
                    isSubscribed
                }

                Log.d("GetSubscribedEvents", "Eventos suscritos filtrados: ${subscribedEvents.size}")
                subscribedEvents
            }
        } else {
            Log.w("GetSubscribedEvents", "No hay usuario logueado, retornando lista vacía")
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }
}

