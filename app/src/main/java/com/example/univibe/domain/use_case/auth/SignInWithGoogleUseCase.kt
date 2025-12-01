package com.example.univibe.domain.use_case.auth

import com.example.univibe.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = repository.signInWithGoogle(idToken)
}
