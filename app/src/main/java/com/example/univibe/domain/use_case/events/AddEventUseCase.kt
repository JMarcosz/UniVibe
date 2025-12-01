package com.example.univibe.domain.use_case.events

import com.example.univibe.domain.model.Event
import com.example.univibe.domain.repository.EventRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) = repository.addEvent(event)
}
