package com.example.univibe.domain.model

data class User(
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val photoUrl: String = "", // URL de la foto guardada en Firebase Storage
    val googlePhotoUrl: String = "", // URL de la foto de Google (para sincronización)
    val displayName: String = "", // Computed: firstName + lastName
    val hasCustomPhoto: Boolean = false // Indica si el usuario subió una foto personalizada
) {
    fun getFullName(): String = "$firstName $lastName".trim()

    /**
     * Retorna la URL de la foto a mostrar.
     * Prioriza foto personalizada, luego foto de Google, luego null para mostrar inicial
     */
    fun getDisplayPhotoUrl(): String {
        return when {
            hasCustomPhoto && photoUrl.isNotEmpty() -> photoUrl
            googlePhotoUrl.isNotEmpty() -> googlePhotoUrl
            else -> ""
        }
    }

    /**
     * Retorna la inicial del nombre para el avatar por defecto
     */
    fun getInitial(): String {
        return when {
            firstName.isNotEmpty() -> firstName.first().uppercase()
            displayName.isNotEmpty() -> displayName.first().uppercase()
            email.isNotEmpty() -> email.first().uppercase()
            else -> "U"
        }
    }
}
