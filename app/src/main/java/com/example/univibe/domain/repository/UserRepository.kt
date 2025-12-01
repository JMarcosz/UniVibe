package com.example.univibe.domain.repository

import android.net.Uri
import com.example.univibe.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun updateUserProfile(user: User): Result<Unit>
    suspend fun getUserById(userId: String): Result<User?>
    suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): Result<String>
}

