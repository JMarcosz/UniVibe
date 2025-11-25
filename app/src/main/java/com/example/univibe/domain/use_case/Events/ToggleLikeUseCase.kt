package com.example.univibe.domain.use_case.Events

import com.example.univibe.domain.repository.EventRepository
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String) = repository.toggleLike(eventId)
}
