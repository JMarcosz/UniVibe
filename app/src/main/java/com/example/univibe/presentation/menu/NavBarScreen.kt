package com.example.univibe.presentation.menu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.univibe.presentation.theme.*

/**
 * Barra de navegación inferior moderna y plana (Flat Design).
 * - Elimina el efecto ripple para una sensación más "nativa" y limpia.
 * - Usa transiciones de color suaves.
 * - Tipografía clara y jerarquía visual mediante color.
 * - Respeta los insets del sistema para evitar superposición con botones de navegación.
 */
@Composable
fun NavBarScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val items = NavTabs.mainTabs()

    // Obtener el padding de los navigation bars del sistema
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()

    // Contenedor principal
    // Usamos una sombra muy sutil (spotColor con alpha bajo) para separar del contenido
    // sin usar bordes duros, manteniendo el estilo "Clean".
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, spotColor = Color.Black.copy(alpha = 0.04f))
            .background(color = BackgroundWhite)
            // Agregamos el padding bottom de los navigation bars para que no se superponga
            .padding(bottom = navigationBarsPadding.calculateBottomPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp), // Altura del contenido de la barra
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route

                // Animación suave de color entre estados activo/inactivo
                val contentColor by animateColorAsState(
                    targetValue = if (selected) BtnPrimary else LightGray,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "TabColorAnimation"
                )

                // InteractionSource nos permite interceptar el click y quitar el efecto ripple
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(65.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null // null elimina el efecto visual de "onda" al hacer click
                        ) {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
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

                    // Icono
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        tint = contentColor,
                        modifier = Modifier
                            .size(24.dp) // Tamaño estándar y limpio
                            .padding(bottom = 4.dp)
                    )

                    // Texto
                    Text(
                        text = item.label,
                        fontSize = 11.sp, // Texto pequeño y minimalista
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = contentColor
                    )
                }
            }
        }
    }
}