package com.example.officeapp.appRoutes

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.officeapp.viewModels.TripViewModel
import com.example.officeapp.screens.addTrip.AddNewTripScreen
import com.example.officeapp.viewModels.VehicleViewModel
import com.example.officeapp.screens.addVehicle.AddNewVehicleScreen
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.authentication.AddNewUserScreen
import com.example.officeapp.screens.authentication.AuthCheckScreen
import com.example.officeapp.screens.authentication.HomeScreen
import com.example.officeapp.screens.authentication.LoginScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val vehicleViewModel: VehicleViewModel = hiltViewModel()
    val tripViewModel: TripViewModel = hiltViewModel()

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
                    navController.navigate(AppRoutes.LOGIN_ROUTE) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                    authenticationViewModel.logout()
                },
                onGoToAddUser = {
                    navController.navigate(AppRoutes.ADD_USER_ROUTE)
                },
                onGoToAddVehicle = {
                    navController.navigate(AppRoutes.ADD_VEHICLE_ROUTE)
                },
                onGoToAddTrip = {
                    navController.navigate(AppRoutes.ADD_TRIP_ROUTE)
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

        composable(AppRoutes.ADD_VEHICLE_ROUTE) {
            AddNewVehicleScreen(
                viewModel = vehicleViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.ADD_TRIP_ROUTE) {
            AddNewTripScreen(
                viewModel = tripViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}