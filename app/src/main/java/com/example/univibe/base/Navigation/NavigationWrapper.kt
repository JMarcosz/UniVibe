package com.example.univibe.base.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.univibe.presentation.auth.AuthScreen
import com.example.univibe.presentation.auth.AuthViewModel
import com.example.univibe.presentation.home.HomeScreen
import com.example.univibe.domain.repository.AuthRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.runtime.LaunchedEffect


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
            HomeScreen()
        }
    }
    LaunchedEffect(Unit) {
        val authFlow = authRepository.isAuthenticatedFlow()
            .distinctUntilChanged()
            .map { isAuth -> if (isAuth) NavRoute.Home else NavRoute.Auth }

        merge(authFlow, NavigationManager.events).collect { route ->
            navHostController.navigate(route.route) {
                navHostController.graph.startDestinationRoute?.let { startRoute ->
                    popUpTo(startRoute) { inclusive = true }
                }
                launchSingleTop = true
            }
        }
    }
}