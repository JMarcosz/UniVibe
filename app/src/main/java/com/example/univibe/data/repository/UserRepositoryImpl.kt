package com.example.univibe.data.repository

import android.net.Uri
import android.util.Log
import com.example.univibe.domain.model.User
import com.example.univibe.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
        private const val USERS_COLLECTION = "Users"
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId == null) {
            Log.d(TAG, "No hay usuario autenticado")
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = firestore.collection(USERS_COLLECTION)
            .document(currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error escuchando cambios del usuario", error)
                    trySend(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val user = User(
                            userId = snapshot.id,
                            email = snapshot.getString("email") ?: "",
                            firstName = snapshot.getString("firstName") ?: "",
                            lastName = snapshot.getString("lastName") ?: "",
                            phone = snapshot.getString("phone") ?: "",
                            photoUrl = snapshot.getString("photoUrl") ?: "",
                            googlePhotoUrl = snapshot.getString("googlePhotoUrl") ?: "",
                            displayName = snapshot.getString("displayName") ?: "",
                            hasCustomPhoto = snapshot.getBoolean("hasCustomPhoto") ?: false
                        )
                        Log.d(TAG, "Usuario cargado desde Firestore: ${user.email}, ${user.firstName} ${user.lastName}")
                        trySend(user)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parseando usuario", e)
                        trySend(null)
                    }
                } else {
                    Log.d(TAG, "Documento de usuario no existe, creando perfil desde Google Auth...")
                    // Crear perfil desde datos de Google Auth
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        // Extraer firstName y lastName del displayName
                        val displayName = firebaseUser.displayName ?: ""
                        val nameParts = displayName.trim().split(" ", limit = 2)
                        val firstName = nameParts.getOrNull(0) ?: ""
                        val lastName = nameParts.getOrNull(1) ?: ""
                        val googlePhotoUrl = firebaseUser.photoUrl?.toString() ?: ""

                        val newUser = User(
                            userId = currentUserId,
                            email = firebaseUser.email ?: "",
                            firstName = firstName,
                            lastName = lastName,
                            phone = "",
                            photoUrl = "", // Vacío inicialmente, se llenará si sube foto personalizada
                            googlePhotoUrl = googlePhotoUrl,
                            displayName = displayName,
                            hasCustomPhoto = false // No tiene foto personalizada al inicio
                        )

                        // Guardar en Firestore de forma asíncrona
                        val userMap = hashMapOf(
                            "userId" to newUser.userId,
                            "email" to newUser.email,
                            "firstName" to newUser.firstName,
                            "lastName" to newUser.lastName,
                            "phone" to newUser.phone,
                            "photoUrl" to newUser.photoUrl,
                            "googlePhotoUrl" to newUser.googlePhotoUrl,
                            "displayName" to newUser.displayName,
                            "hasCustomPhoto" to newUser.hasCustomPhoto
                        )

                        firestore.collection(USERS_COLLECTION)
                            .document(currentUserId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d(TAG, "Perfil creado en Firestore: ${newUser.email}, ${newUser.firstName} ${newUser.lastName}")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error creando perfil en Firestore", e)
                            }

                        // Enviar el usuario inmediatamente
                        trySend(newUser)
                    } else {
                        Log.e(TAG, "FirebaseUser es null")
                        trySend(null)
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            val currentUserId = auth.currentUser?.uid
            if (currentUserId == null) {
                Log.e(TAG, "No hay usuario autenticado")
                return Result.failure(Exception("Usuario no autenticado"))
            }

            val userMap = hashMapOf(
                "userId" to user.userId,
                "email" to user.email,
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "phone" to user.phone,
                "photoUrl" to user.photoUrl,
                "googlePhotoUrl" to user.googlePhotoUrl,
                "displayName" to user.getFullName(),
                "hasCustomPhoto" to user.hasCustomPhoto
            )

            firestore.collection(USERS_COLLECTION)
                .document(currentUserId)
                .set(userMap)
                .await()

            Log.d(TAG, "Perfil actualizado correctamente")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando perfil", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (snapshot.exists()) {
                val user = User(
                    userId = snapshot.id,
                    email = snapshot.getString("email") ?: "",
                    firstName = snapshot.getString("firstName") ?: "",
                    lastName = snapshot.getString("lastName") ?: "",
                    phone = snapshot.getString("phone") ?: "",
                    photoUrl = snapshot.getString("photoUrl") ?: "",
                    googlePhotoUrl = snapshot.getString("googlePhotoUrl") ?: "",
                    displayName = snapshot.getString("displayName") ?: "",
                    hasCustomPhoto = snapshot.getBoolean("hasCustomPhoto") ?: false
                )
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo usuario por ID", e)
            Result.failure(e)
        }
    }

    override suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): Result<String> {
        return try {
            Log.d(TAG, "Subiendo foto de perfil para usuario: $userId")

            // Referencia al storage
            val storageRef = storage.reference
            val photoRef = storageRef.child("profile_photos/$userId.jpg")

            // Subir la imagen
            photoRef.putFile(imageUri).await()
            Log.d(TAG, "Imagen subida exitosamente")

            // Obtener la URL de descarga
            val downloadUrl = photoRef.downloadUrl.await().toString()
            Log.d(TAG, "URL de descarga obtenida: $downloadUrl")

            // Actualizar Firestore con la nueva URL y marcar que tiene foto personalizada
            val updateMap = hashMapOf<String, Any>(
                "photoUrl" to downloadUrl,
                "hasCustomPhoto" to true
            )

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update(updateMap)
                .await()

            Log.d(TAG, "Firestore actualizado con nueva foto")
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Log.e(TAG, "Error subiendo foto de perfil", e)
            Result.failure(e)
        }
    }
}
