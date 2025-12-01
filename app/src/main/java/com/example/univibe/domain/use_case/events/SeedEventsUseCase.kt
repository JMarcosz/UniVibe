package com.example.univibe.domain.use_case.events

import com.example.univibe.domain.repository.EventRepository
import javax.inject.Inject

class SeedEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke() = repository.seedInitialEvents()
}

