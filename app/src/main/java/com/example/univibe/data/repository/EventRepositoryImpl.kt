package com.example.univibe.data.repository

import android.util.Log
import com.example.univibe.domain.model.Event
import com.example.univibe.domain.repository.EventRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : EventRepository {

    private val eventsCollection = firestore.collection("Events")

    override fun getEvents(): Flow<List<Event>> = callbackFlow {
        Log.d("EventRepo", "Iniciando listener de eventos")
        val listener = eventsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("EventRepo", "Error en listener", error)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                try {
                    val events = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<Event>()?.copy(id = doc.id)
                    }
                    Log.d("EventRepo", "Eventos recibidos: ${events.size}")
                    events.forEach { event ->
                        Log.d("EventRepo", "Evento: ${event.title}, Likes: ${event.likes}")
                    }
                    trySend(events).isSuccess
                } catch (e: Exception) {
                    Log.e("EventRepo", "Error mapeando eventos", e)
                }
            } else {
                Log.d("EventRepo", "Snapshot es null")
            }
        }
        awaitClose {
            Log.d("EventRepo", "Cerrando listener")
            listener.remove()
        }
    }

    override suspend fun addEvent(event: Event): Result<Unit> {
        return try {
            eventsCollection.add(event).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error agregando evento", e)
            Result.failure(e)
        }
    }

    override suspend fun toggleLike(eventId: String): Result<Unit> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))
        
        Log.d("EventRepo", "Toggle like - EventId: $eventId, UserId: $userId")

        return try {
            val eventRef = eventsCollection.document(eventId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(eventRef)
                if (!snapshot.exists()) {
                    Log.e("EventRepo", "El evento no existe: $eventId")
                    throw Exception("Evento no encontrado")
                }

                @Suppress("UNCHECKED_CAST")
                val currentLikes = (snapshot.get("likes") as? Map<String, Boolean>)?.toMutableMap() ?: mutableMapOf()

                Log.d("EventRepo", "Likes actuales: $currentLikes")

                if (currentLikes.containsKey(userId)) {
                    Log.d("EventRepo", "Removiendo like")
                    currentLikes.remove(userId)
                } else {
                    Log.d("EventRepo", "Agregando like")
                    currentLikes[userId] = true
                }

                transaction.update(eventRef, "likes", currentLikes)
                Log.d("EventRepo", "Likes actualizados a: $currentLikes")
                null
            }.await()

            Log.d("EventRepo", "Toggle like completado exitosamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error en toggleLike", e)
            Result.failure(e)
        }
    }

    override suspend fun toggleSubscription(eventId: String): Result<Unit> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))

        Log.d("EventRepo", "Toggle subscription - EventId: $eventId, UserId: $userId")

        return try {
            val eventRef = eventsCollection.document(eventId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(eventRef)
                if (!snapshot.exists()) {
                    Log.e("EventRepo", "El evento no existe: $eventId")
                    throw Exception("Evento no encontrado")
                }

                @Suppress("UNCHECKED_CAST")
                val currentSubs = (snapshot.get("subscriptions") as? Map<String, Boolean>)?.toMutableMap() ?: mutableMapOf()

                Log.d("EventRepo", "Suscripciones actuales: $currentSubs")

                if (currentSubs.containsKey(userId)) {
                    Log.d("EventRepo", "Removiendo suscripción")
                    currentSubs.remove(userId)
                } else {
                    Log.d("EventRepo", "Agregando suscripción")
                    currentSubs[userId] = true
                }

                transaction.update(eventRef, "subscriptions", currentSubs)
                Log.d("EventRepo", "Suscripciones actualizadas a: $currentSubs")
                null
            }.await()

            Log.d("EventRepo", "Toggle subscription completado exitosamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error en toggleSubscription", e)
            Result.failure(e)
        }
    }

    override suspend fun seedInitialEvents(): Result<Unit> {
        return try {
            Log.d("EventRepo", "Iniciando seed de eventos")

            // Verificar si ya existen eventos
            val snapshot = eventsCollection.limit(1).get().await()
            if (!snapshot.isEmpty) {
                Log.d("EventRepo", "Eventos ya existen, no se cargarán de nuevo")
                return Result.success(Unit)
            }

            Log.d("EventRepo", "Creando 10 eventos")
            val eventos = listOf(
                Event(
                    id = "evento_01",
                    title = "Visita a orfanato",
                    description = "Jornada de voluntariado en el orfanato local. Ayuda a los niños y sé parte del cambio positivo en la comunidad.",
                    creationDate = Timestamp(1731646200, 0),
                    closingDate = Timestamp(1731657000, 0),
                    location = "Av. México #43",
                    category = "Voluntariado",
                    qrCodeUrl = "univibe://event/evento_01",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_02",
                    title = "Charla vocacional",
                    description = "Aprende sobre las diferentes carreras profesionales con expertos del área. Sesión de preguntas y respuestas incluida.",
                    creationDate = Timestamp(1731646200, 0),
                    closingDate = Timestamp(1731657000, 0),
                    location = "UNICIDA",
                    category = "Educación",
                    qrCodeUrl = "univibe://event/evento_02",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_03",
                    title = "Recolecta de basura",
                    description = "Limpieza ambiental en las playas y parques cercanos. ¡Protejamos nuestro planeta! Se proporcionarán guantes y bolsas.",
                    creationDate = Timestamp(1731646200, 0),
                    closingDate = Timestamp(1731657000, 0),
                    location = "Mirador Sur",
                    category = "Medio Ambiente",
                    qrCodeUrl = "univibe://event/evento_03",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_04",
                    title = "Actividad deportiva",
                    description = "Torneo de fútbol y voleibol para todos los estudiantes. Categorías por nivel de habilidad.",
                    creationDate = Timestamp(1731646800, 0),
                    closingDate = Timestamp(1731668400, 0),
                    location = "Cancha Deportiva UNICIDA",
                    category = "Deporte",
                    qrCodeUrl = "univibe://event/evento_04",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_05",
                    title = "Taller de Programación",
                    description = "Aprende los conceptos básicos de programación en Python y crea tu primer aplicativo desde cero.",
                    creationDate = Timestamp(1731732600, 0),
                    closingDate = Timestamp(1731741600, 0),
                    location = "Laboratorio de Sistemas",
                    category = "Tecnología",
                    qrCodeUrl = "univibe://event/evento_05",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_06",
                    title = "Conferencia de Sostenibilidad",
                    description = "Charla sobre desarrollo sostenible y cómo las universidades pueden contribuir a un futuro mejor.",
                    creationDate = Timestamp(1731732600, 0),
                    closingDate = Timestamp(1731739800, 0),
                    location = "Auditorio Principal",
                    category = "Sostenibilidad",
                    qrCodeUrl = "univibe://event/evento_06",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_07",
                    title = "Día de Integración",
                    description = "Actividades recreativas, comida y entretenimiento para toda la comunidad universitaria. ¡Ven y diviértete!",
                    creationDate = Timestamp(1731819000, 0),
                    closingDate = Timestamp(1731839400, 0),
                    location = "Parque Central de UNICIDA",
                    category = "Integración",
                    qrCodeUrl = "univibe://event/evento_07",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_08",
                    title = "Taller de Liderazgo",
                    description = "Desarrolla tus habilidades de liderazgo con dinámicas interactivas y expertos en el tema.",
                    creationDate = Timestamp(1731819000, 0),
                    closingDate = Timestamp(1731826200, 0),
                    location = "Sala de Conferencias A",
                    category = "Desarrollo Personal",
                    qrCodeUrl = "univibe://event/evento_08",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_09",
                    title = "Hackathon 2025",
                    description = "Compite con otros desarrolladores en un maratón de 24 horas de programación. Premios incluidos.",
                    creationDate = Timestamp(1731905400, 0),
                    closingDate = Timestamp(1731948600, 0),
                    location = "Centro de Innovación",
                    category = "Tecnología",
                    qrCodeUrl = "univibe://event/evento_09",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                ),
                Event(
                    id = "evento_10",
                    title = "Concierto de Música en Vivo",
                    description = "Disfruta de un concierto con artistas locales. Entrada libre para estudiantes y personal de la universidad.",
                    creationDate = Timestamp(1731905400, 0),
                    closingDate = Timestamp(1731921600, 0),
                    location = "Anfiteatro Universitario",
                    category = "Cultura",
                    qrCodeUrl = "univibe://event/evento_10",
                    likes = emptyMap(),
                    subscriptions = emptyMap()
                )
            )

            // Agregar eventos a Firestore usando batch write
            val batch = firestore.batch()
            eventos.forEach { event ->
                val docRef = eventsCollection.document(event.id)
                batch.set(docRef, event)
                Log.d("EventRepo", "Evento preparado: ${event.id}")
            }
            batch.commit().await()

            Log.d("EventRepo", "Eventos cargados exitosamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EventRepo", "Error en seedInitialEvents", e)
            Result.failure(e)
        }
    }
}
