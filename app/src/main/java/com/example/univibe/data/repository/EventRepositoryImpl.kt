package com.example.univibe.data.repository

import com.example.univibe.domain.model.Event
import com.example.univibe.domain.repository.EventRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : EventRepository {

    private val eventsCollection = firestore.collection("events")

    override fun getEvents(): Flow<List<Event>> = callbackFlow {
        val listener = eventsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Cierra el flow con error
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val events = snapshot.toObjects<Event>()
                trySend(events).isSuccess // Envía la lista al flow
            }
        }
        awaitClose { listener.remove() } // Cancela el listener cuando el flow se cierra
    }

    override suspend fun addEvent(event: Event): Result<Unit> {
        return try {
            eventsCollection.add(event).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleLike(eventId: String): Result<Unit> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))
        
        return try {
            val eventRef = eventsCollection.document(eventId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(eventRef)
                val currentLikes = snapshot.get("likes") as? Map<String, Boolean> ?: emptyMap()

                if (currentLikes.containsKey(userId)) {
                    // Si ya le dio like, quitarlo (unlike)
                    transaction.update(eventRef, "likes.$userId", FieldValue.delete())
                } else {
                    // Si no le ha dado like, añadirlo (like)
                    transaction.update(eventRef, "likes.$userId", true)
                }
                null // La transacción de Firestore requiere que se devuelva algo o null
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
