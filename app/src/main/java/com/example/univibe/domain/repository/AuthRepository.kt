package com.example.univibe.domain.repository

import com.example.univibe.util.Resource // Usaremos la clase Resource como en FluxNews
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun loginWithEmail(email: String, pass: String): Resource<FirebaseUser>
    suspend fun registerWithEmail(email: String, pass: String): Resource<FirebaseUser>
    suspend fun signInWithGoogle(idToken: String): Resource<FirebaseUser>
}