package com.example.univibe.domain.use_case.user

import android.net.Uri
import com.example.univibe.domain.repository.UserRepository
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, imageUri: Uri): Result<String> {
        return repository.uploadProfilePhoto(userId, imageUri)
    }
}

