package com.example.univibe.domain.use_case.Events

import com.example.univibe.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke() = repository.getEvents()
}
