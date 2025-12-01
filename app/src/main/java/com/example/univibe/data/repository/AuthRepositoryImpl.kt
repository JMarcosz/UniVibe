package com.example.univibe.data.repository

import com.example.univibe.domain.model.AuthResult
import com.example.univibe.domain.model.User
import com.example.univibe.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomainUser()
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Error: User object is null after sign up.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomainUser()
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Error: User object is null after sign in.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user?.toDomainUser()
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Error: User object is null after Google sign in.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun signOut(): AuthResult {
        return try {
            firebaseAuth.signOut()
            AuthResult.Unauthenticated
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error signing out.")
        }
    }

    private fun FirebaseUser.toDomainUser(): User {
        return User(
            userId = this.uid,
            email = this.email ?: "",
            firstName = "",
            lastName = "",
            phone = "",
            photoUrl = this.photoUrl?.toString() ?: "",
            displayName = this.displayName ?: ""
        )
    }
    override fun isAuthenticatedFlow(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

}
