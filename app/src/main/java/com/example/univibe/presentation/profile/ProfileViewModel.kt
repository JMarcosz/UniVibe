package com.example.univibe.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.univibe.base.navigation.NavRoute
import com.example.univibe.base.navigation.NavigationManager
import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.model.User
import com.example.univibe.domain.use_case.auth.SignOutUseCase
import com.example.univibe.domain.use_case.user.GetCurrentUserUseCase
import com.example.univibe.domain.use_case.user.UpdateUserProfileUseCase
import com.example.univibe.domain.use_case.user.ValidateUserDataUseCase
import com.example.univibe.domain.use_case.user.UploadProfilePhotoUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val validateUserDataUseCase: ValidateUserDataUseCase,
    private val uploadProfilePhotoUseCase: UploadProfilePhotoUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    companion object { private const val TAG = "ProfileViewModel" }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Almacenar el usuario original para poder cancelar cambios
    private var originalUser: User? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                Log.d(TAG, "=== Iniciando carga de perfil ===")
                Log.d(TAG, "Usuario autenticado: ${auth.currentUser?.uid}")
                Log.d(TAG, "Email: ${auth.currentUser?.email}")
                Log.d(TAG, "DisplayName: ${auth.currentUser?.displayName}")

                getCurrentUserUseCase().collect { user ->
                    Log.d(TAG, "=== Flow emitió un valor ===")
                    if (user != null) {
                        Log.d(TAG, "Usuario cargado exitosamente:")
                        Log.d(TAG, "  - userId: ${user.userId}")
                        Log.d(TAG, "  - email: ${user.email}")
                        Log.d(TAG, "  - firstName: ${user.firstName}")
                        Log.d(TAG, "  - lastName: ${user.lastName}")
                        Log.d(TAG, "  - phone: ${user.phone}")
                        Log.d(TAG, "  - displayName: ${user.displayName}")

                        originalUser = user
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                firstName = user.firstName,
                                lastName = user.lastName,
                                email = user.email,
                                phone = user.phone,
                                photoUrl = user.photoUrl,
                                subscribedEventsCount = 0 // TODO: Obtener de eventos suscritos
                            )
                        }
                    } else {
                        Log.e(TAG, "El Flow emitió NULL - usuario no encontrado")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "No se pudo cargar el perfil"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "=== EXCEPCIÓN al cargar perfil ===", e)
                Log.e(TAG, "Tipo: ${e.javaClass.simpleName}")
                Log.e(TAG, "Mensaje: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar perfil: ${e.message}"
                    )
                }
            }
        }
    }

    fun onFirstNameChange(value: String) {
        _uiState.update {
            it.copy(
                firstName = value,
                errors = it.errors - "firstName"
            )
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(
                lastName = value,
                errors = it.errors - "lastName"
            )
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                errors = it.errors - "email"
            )
        }
    }

    fun onPhoneChange(value: String) {
        _uiState.update {
            it.copy(
                phone = value,
                errors = it.errors - "phone"
            )
        }
    }

    fun toggleEditMode() {
        val currentState = _uiState.value
        if (currentState.isEditing) {
            // Cancelar edición - restaurar datos originales
            originalUser?.let { user ->
                _uiState.update {
                    it.copy(
                        isEditing = false,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        phone = user.phone,
                        errors = emptyMap()
                    )
                }
            }
        } else {
            // Activar modo edición
            _uiState.update { it.copy(isEditing = true) }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value

                // Validar datos
                val validationResult = validateUserDataUseCase(
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    email = currentState.email,
                    phone = currentState.phone
                )

                if (!validationResult.isValid) {
                    _uiState.update {
                        it.copy(
                            errors = validationResult.errors,
                            errorMessage = "Por favor corrige los errores"
                        )
                    }
                    return@launch
                }

                // Guardar
                _uiState.update { it.copy(isSaving = true) }
                Log.d(TAG, "Guardando perfil...")

                val currentUserId = auth.currentUser?.uid ?: ""
                val updatedUser = User(
                    userId = currentUserId,
                    email = currentState.email,
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    phone = currentState.phone,
                    photoUrl = currentState.photoUrl
                )

                val result = updateUserProfileUseCase(updatedUser)

                result.onSuccess {
                    Log.d(TAG, "Perfil guardado correctamente")
                    originalUser = updatedUser
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isEditing = false,
                            successMessage = "Perfil actualizado correctamente",
                            errors = emptyMap()
                        )
                    }
                    // Limpiar mensaje después de 3 segundos
                    kotlinx.coroutines.delay(3000)
                    _uiState.update { it.copy(successMessage = null) }
                }.onFailure { error ->
                    Log.e(TAG, "Error guardando perfil", error)
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = "Error al guardar: ${error.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception guardando perfil", e)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            val result = signOutUseCase()
            when (result) {
                is AuthResult.Unauthenticated -> {
                    Log.d(TAG, "onSignOutClick: sign out successful, navigating to auth via NavigationManager")
                    NavigationManager.navigateTo(NavRoute.Auth)
                }
                is AuthResult.Error -> {
                    Log.d(TAG, "onSignOutClick: sign out error: ${result.message}")
                    _uiState.update {
                        it.copy(errorMessage = "Error al cerrar sesión: ${result.message}")
                    }
                }
                else -> {
                    Log.d(TAG, "onSignOutClick: unexpected sign out result: $result")
                }
            }
        }
    }

    fun uploadProfilePhoto(imageUri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true) }
                Log.d(TAG, "Subiendo foto de perfil...")

                val currentUserId = auth.currentUser?.uid ?: return@launch
                val result = uploadProfilePhotoUseCase(currentUserId, imageUri)

                result.onSuccess { photoUrl ->
                    Log.d(TAG, "Foto subida exitosamente: $photoUrl")
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            photoUrl = photoUrl,
                            successMessage = "Foto actualizada correctamente"
                        )
                    }
                    // Limpiar mensaje después de 3 segundos
                    kotlinx.coroutines.delay(3000)
                    _uiState.update { it.copy(successMessage = null) }
                }.onFailure { error ->
                    Log.e(TAG, "Error subiendo foto", error)
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = "Error al subir foto: ${error.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception subiendo foto", e)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun onEditPhotoClick() {
        _uiState.update { it.copy(showPhotoDialog = true) }
    }

    fun dismissPhotoDialog() {
        _uiState.update { it.copy(showPhotoDialog = false) }
    }
}
