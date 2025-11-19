package com.example.univibe.domain.repository

import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.model.User

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signInWithGoogle(idToken: String): AuthResult
    suspend fun signOut(): AuthResult
    fun getCurrentUser(): User?
}
