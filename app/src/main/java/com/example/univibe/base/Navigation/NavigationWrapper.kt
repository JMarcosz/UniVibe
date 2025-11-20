package com.example.univibe.base.Navigation

import android.util.Log
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
import com.example.univibe.presentation.home.HomeViewModel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.map

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val TAG = "NavigationWrapper"
    val uiState = authViewModel.uiState.collectAsState()

    val startDestination =
        if (uiState.value.isAuthenticated) NavRoute.Home.route else NavRoute.Auth.route

    LaunchedEffect(Unit) {
        val authRoutes = authViewModel.uiEvent.map { event ->
            when (event) {
                is AuthViewModel.AuthUiEvent.NavigateToHome -> NavRoute.Home
                is AuthViewModel.AuthUiEvent.NavigateToAuth -> NavRoute.Auth
            }
        }

        val merged = merge(authRoutes, NavigationManager.events)

        merged.collect { navRoute ->
            val currentRoute = navHostController.currentDestination?.route
            if (currentRoute == navRoute.route) {
                Log.d(TAG, "Already on route ${navRoute.route}; skipping navigation")
            } else {
                navHostController.navigate(navRoute.route) {
                    when (navRoute) {
                        is NavRoute.Home -> popUpTo(NavRoute.Auth.route) { inclusive = true }
                        is NavRoute.Auth -> popUpTo(NavRoute.Home.route) { inclusive = true }
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(NavRoute.Auth.route) {
            AuthScreen(viewModel = authViewModel)
        }
        composable(NavRoute.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(viewModel = homeViewModel)
        }
    }
}