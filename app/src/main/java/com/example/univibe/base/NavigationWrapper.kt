package com.example.univibe.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.univibe.presentation.auth.AuthScreen
import com.example.univibe.presentation.auth.AuthViewModel
import com.example.univibe.presentation.home.HomeScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    // Start destination basado en el estado actual (primer render)
    val startDestination = if (uiState.value.isAuthenticated) "home" else "auth"

    // Recolectar eventos one-shot para navegación
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthViewModel.AuthUiEvent.NavigateToHome -> {
                    navHostController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                is AuthViewModel.AuthUiEvent.NavigateToAuth -> {
                    navHostController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable("auth") {
            AuthScreen(navHostController)
        }
        composable("home") {
            HomeScreen()
        }
    }
}