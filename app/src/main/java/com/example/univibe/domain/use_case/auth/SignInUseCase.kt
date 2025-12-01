package com.example.univibe.domain.use_case.auth

import com.example.univibe.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = repository.signIn(email, password)
}
