package com.example.univibe.domain.model

import com.google.firebase.Timestamp

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val creationDate: Timestamp = Timestamp.now(),
    val closingDate: Timestamp = Timestamp.now(),
    val category: String = "",
    val location: String = "",
    val qrCodeUrl: String = "",
    val likes: Map<String, Boolean> = emptyMap(),
    val subscriptions: Map<String, Boolean> = emptyMap()
)
