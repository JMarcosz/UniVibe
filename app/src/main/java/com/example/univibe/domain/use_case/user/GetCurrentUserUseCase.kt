package com.example.univibe.domain.use_case.user

import com.example.univibe.domain.model.User
import com.example.univibe.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        return repository.getCurrentUser()
    }
}

