package com.example.univibe.base.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.univibe.presentation.auth.AuthScreen
import com.example.univibe.presentation.auth.AuthViewModel
import com.example.univibe.presentation.home.HomeScreen
import com.example.univibe.presentation.home.HomeViewModel
import com.example.univibe.domain.repository.AuthRepository


@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    authRepository: AuthRepository
) {

    val isAuthenticated by authRepository.isAuthenticatedFlow().collectAsState(initial = false)
    val startDestination = if (isAuthenticated) NavRoute.Home.route else NavRoute.Auth.route

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(NavRoute.Auth.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            AuthScreen(viewModel = authViewModel)
        }
        composable(NavRoute.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(viewModel = homeViewModel)
        }
    }

    LaunchedEffect(isAuthenticated) {
        navHostController.navigate(startDestination) {
            navHostController.graph.startDestinationRoute?.let { startRoute ->
                popUpTo(startRoute) { inclusive = true }
            }
            launchSingleTop = true
        }
    }
}