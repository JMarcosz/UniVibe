package com.example.univibe.domain.use_case.user

import com.example.univibe.domain.model.User
import com.example.univibe.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        // Validaciones antes de actualizar
        if (user.firstName.isBlank()) {
            return Result.failure(Exception("El nombre es requerido"))
        }
        if (user.lastName.isBlank()) {
            return Result.failure(Exception("El apellido es requerido"))
        }
        if (user.email.isBlank()) {
            return Result.failure(Exception("El email es requerido"))
        }

        return repository.updateUserProfile(user)
    }
}

