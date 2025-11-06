package com.example.univibe.domain.use_case

import com.example.univibe.domain.repository.AuthRepository
import javax.inject.Inject // <-- ¡ESTA ES LA LÍNEA QUE FALTABA!

// Clase contenedora como en FluxNews
data class AuthUseCases(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val googleSignIn: GoogleSignInUseCase
)

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String) = repository.loginWithEmail(email, pass)
}

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String) = repository.registerWithEmail(email, pass)
}

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repository.signInWithGoogle(idToken)
}