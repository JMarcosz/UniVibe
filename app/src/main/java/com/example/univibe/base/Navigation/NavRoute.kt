package com.example.univibe.base.Navigation

sealed class NavRoute(val route: String) {
    object Auth : NavRoute("auth")
    object Home : NavRoute("home")
    object Find : NavRoute("find")
    object Notes : NavRoute("notes")
    object Profile : NavRoute("profile")
}
