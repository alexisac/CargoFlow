package com.example.officeapp.appRoutes

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.officeapp.features.authentication.AuthenticationViewModel
import com.example.officeapp.features.authentication.screens.AddNewUserScreen
import com.example.officeapp.features.authentication.screens.AuthCheckScreen
import com.example.officeapp.features.authentication.screens.HomeScreen
import com.example.officeapp.features.authentication.screens.LoginScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.AUTH_CHECK_ROUTE
    ) {
        composable(AppRoutes.AUTH_CHECK_ROUTE) {
            AuthCheckScreen(
                viewModel = authenticationViewModel,
                onAuthenticated = {
                    navController.navigate(AppRoutes.HOME_ROUTE) {
                        popUpTo(AppRoutes.AUTH_CHECK_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onUnauthenticated = {
                    navController.navigate(AppRoutes.LOGIN_ROUTE) {
                        popUpTo(AppRoutes.AUTH_CHECK_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN_ROUTE) {
            LoginScreen(
                viewModel = authenticationViewModel,
                onLoggedIn = {
                    navController.navigate(AppRoutes.HOME_ROUTE) {
                        popUpTo(AppRoutes.LOGIN_ROUTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoutes.HOME_ROUTE) {
            HomeScreen(
                viewModel = authenticationViewModel,
                onLogout = {
                    authenticationViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN_ROUTE) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onGoToAddUser = {
                    navController.navigate(AppRoutes.ADD_USER_ROUTE)
                }
            )
        }

        composable(AppRoutes.ADD_USER_ROUTE) {
            AddNewUserScreen(
                viewModel = authenticationViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}