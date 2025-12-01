package com.example.univibe.domain.use_case.events

import com.example.univibe.domain.repository.EventRepository
import javax.inject.Inject

class ToggleEventSubscriptionUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: String): Result<Unit> {
        return repository.toggleSubscription(eventId)
    }
}

