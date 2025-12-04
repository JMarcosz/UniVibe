package com.example.univibe.presentation.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "", // URL de foto personalizada en Firebase Storage
    val googlePhotoUrl: String = "", // URL de foto de Google
    val hasCustomPhoto: Boolean = false, // Indica si el usuario subió una foto personalizada
    val subscribedEventsCount: Int = 0,
    val errors: Map<String, String> = emptyMap(),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val showPhotoDialog: Boolean = false
) {
    fun hasErrors(): Boolean = errors.isNotEmpty()

    fun getFullName(): String = "$firstName $lastName".trim()

    /**
     * Retorna la URL de la foto a mostrar.
     * Prioriza foto personalizada, luego foto de Google, luego string vacío para mostrar inicial
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
            email.isNotEmpty() -> email.first().uppercase()
            else -> "U"
        }
    }
}


