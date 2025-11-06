package com.example.univibe.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.univibe.presentation.auth.LoginScreen

@Composable
fun NavGraph(
    startDestination: String = Route.LOGIN
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        // --- Pantalla de Login ---
        composable(route = Route.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Route.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Route.HOME) {
                        // Limpia el stack para que el usuario no pueda volver al login
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantalla de Registro (Placeholder) ---
        composable(route = Route.REGISTER) {
            // Aquí iría tu futura RegisterScreen()
            Text("Pantalla de Registro (WIP)")
        }

        // --- Pantalla Principal (Placeholder) ---
        composable(route = Route.HOME) {
            // Aquí iría tu futura HomeScreen()
            Text("¡Login Exitoso! Bienvenido a Home.")
        }
    }
}