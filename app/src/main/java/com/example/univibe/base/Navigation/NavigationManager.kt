package com.example.univibe.base.Navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NavigationManager {
    private val _events = MutableSharedFlow<NavRoute>()
    val events = _events.asSharedFlow()

    suspend fun navigateTo(route: NavRoute) {
        _events.emit(route)
    }
}
