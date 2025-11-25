package com.example.univibe.presentation.menu

import com.example.univibe.R
import com.example.univibe.base.Navigation.NavRoute

/**
 * Modelo para representar un tab del NavBar.
 * Mantener aquí la definición permite añadir propiedades (badge, visible, order, meta) sin tocar el UI.
 */
data class NavTab(
    val route: String,
    val iconRes: Int,
    val label: String
)

object NavTabs {
    fun mainTabs(): List<NavTab> = listOf(
        NavTab(route = NavRoute.Home.route, iconRes = R.drawable.home_icon, label = "INICIO"),
        NavTab(route = NavRoute.Find.route, iconRes = R.drawable.find_icon, label = "BUSCAR"),
        NavTab(route = NavRoute.Notes.route, iconRes = R.drawable.notes_icon, label = "EVENTOS"),
        NavTab(route = NavRoute.Profile.route, iconRes = R.drawable.profile_icon, label = "PERFIL")
    )
}
