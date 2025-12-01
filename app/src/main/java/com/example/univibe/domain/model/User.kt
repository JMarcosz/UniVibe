package com.example.univibe.domain.model

data class User(
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val displayName: String = "" // Computed: firstName + lastName
) {
    fun getFullName(): String = "$firstName $lastName".trim()
}
