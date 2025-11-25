package com.example.univibe.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Barra de navegación inferior (tabs) que se desacopla de estado local y
 * utiliza la ruta actual del NavHostController para determinar el tab activo.
 * - Cada tab muestra un pequeño indicador superior cuando está activo.
 * - Al hacer click navega a la ruta asociada y usa launchSingleTop.
 */
@Composable
fun NavBarScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val items = NavTabs.mainTabs()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                // Evita crear múltiples entradas iguales
                                navController.graph.startDestinationRoute?.let { startRoute ->
                                    popUpTo(startRoute) { saveState = true }
                                }
                                restoreState = true
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Indicador superior
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(6.dp)
                        .background(
                            color = if (selected) Color.Black else Color.Transparent,
                        )
                )

                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    tint = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(28.dp)
                )

                Text(
                    text = item.label,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}