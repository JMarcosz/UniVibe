package com.example.univibe.domain.usecase

import android.net.Uri
import com.example.univibe.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para subir foto de perfil del usuario.
 * Gestiona la subida de la imagen a Firebase Storage y actualiza el perfil del usuario.
 */
class UploadProfilePhotoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Sube una foto de perfil para el usuario actual
     * @param userId ID del usuario
     * @param imageUri URI de la imagen seleccionada
     * @return Result con la URL de la imagen subida o un error
     */
    suspend operator fun invoke(userId: String, imageUri: Uri): Result<String> {
        return try {
            userRepository.uploadProfilePhoto(userId, imageUri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

