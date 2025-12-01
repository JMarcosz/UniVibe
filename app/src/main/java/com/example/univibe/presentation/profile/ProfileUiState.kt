package com.example.univibe.presentation.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val subscribedEventsCount: Int = 0,
    val errors: Map<String, String> = emptyMap(),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val showPhotoDialog: Boolean = false
) {
    fun hasErrors(): Boolean = errors.isNotEmpty()

    fun getFullName(): String = "$firstName $lastName".trim()
}

