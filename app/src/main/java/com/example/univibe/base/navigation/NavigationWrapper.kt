package com.example.univibe.base.navigation

import androidx.compose.material3.Scaffold
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
import com.example.univibe.presentation.menu.NavBarScreen
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.univibe.presentation.events.EventScreen
import com.example.univibe.presentation.find_event.FindEventScreen
import com.example.univibe.presentation.profile.ProfileScreen
import com.example.univibe.presentation.register.RegisterScreen
import com.example.univibe.presentation.recovery.RecoveryScreen

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    authRepository: AuthRepository,
    deepLinkEventId: String? = null
) {

    val isAuthenticated by authRepository.isAuthenticatedFlow().collectAsState(initial = false)
    val startDestination = if (isAuthenticated) NavRoute.Home.route else NavRoute.Auth.route

    // Decidir si mostrar la barra inferior según la ruta actual del NavController
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomBarRoutes = remember { listOf(NavRoute.Home.route, NavRoute.Find.route, NavRoute.Events.route, NavRoute.Profile.route) }
    val showBottomBar = currentRoute != null && bottomBarRoutes.contains(currentRoute)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavBarScreen(navController = navHostController)
            }
        }
    ) { innerPadding ->
        NavHost(navController = navHostController, startDestination = startDestination, modifier = Modifier.padding(innerPadding)) {
            composable(NavRoute.Auth.route) {
                val authViewModel: AuthViewModel = hiltViewModel()
                AuthScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navHostController.navigate(NavRoute.Register.route) },
                    onNavigateToRecovery = { navHostController.navigate(NavRoute.Recovery.route) }
                )
            }
            composable(NavRoute.Home.route) {
                HomeScreen(deepLinkEventId = deepLinkEventId)
            }
            // Nota: rutas Find/Notes/Profile deben ser manejadas por sus respectivos screens cuando se implementen
            composable(NavRoute.Find.route) { FindEventScreen() }
            composable(NavRoute.Events.route) { EventScreen()}
            composable(NavRoute.Profile.route) {
                ProfileScreen()
            }
            composable(NavRoute.Register.route) {
                RegisterScreen(
                    onRegistrationSuccess = {
                        navHostController.navigate(NavRoute.Auth.route) {
                            popUpTo(NavRoute.Register.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navHostController.popBackStack()
                    }
                )
            }
            composable(NavRoute.Recovery.route) {
                RecoveryScreen(
                    onEmailSent = {
                        navHostController.popBackStack()
                    },
                    onBackToLogin = {
                        navHostController.popBackStack()
                    }
                )
            }
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