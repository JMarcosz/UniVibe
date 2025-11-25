package com.example.univibe.domain.use_case.Account

import com.example.univibe.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = repository.signUp(email, password)
}