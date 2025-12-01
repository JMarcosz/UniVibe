package com.example.univibe.domain.repository

import com.example.univibe.domain.model.Event
import kotlinx.coroutines.flow.Flow


interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun addEvent(event: Event): Result<Unit>
    suspend fun toggleLike(eventId: String): Result<Unit>
    suspend fun toggleSubscription(eventId: String): Result<Unit>
    suspend fun seedInitialEvents(): Result<Unit>
}
