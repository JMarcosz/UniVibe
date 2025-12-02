package com.example.univibe.base.navigation

sealed class NavRoute(val route: String) {
    object Auth : NavRoute("auth")
    object Home : NavRoute("home")
    object Find : NavRoute("find")
    object Events : NavRoute("events")
    object Profile : NavRoute("profile")
}
