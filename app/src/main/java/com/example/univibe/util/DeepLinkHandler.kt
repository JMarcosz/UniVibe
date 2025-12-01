package com.example.univibe.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DeepLinkHandler {
    private val _eventId = MutableStateFlow<String?>(null)
    val eventId = _eventId.asStateFlow()

    fun setEventId(eventId: String?) {
        _eventId.value = eventId
    }
}
