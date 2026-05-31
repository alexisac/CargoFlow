package com.example.officeapp.appRoutes

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.officeapp.viewModels.TripViewModel
import com.example.officeapp.screens.addTrip.AddNewTripScreen
import com.example.officeapp.viewModels.VehicleViewModel
import com.example.officeapp.screens.addVehicle.AddNewVehicleScreen
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.authentication.AddNewUserScreen
import com.example.officeapp.screens.authentication.AuthCheckScreen
import com.example.officeapp.screens.HomeScreen
import com.example.officeapp.screens.assignDriver.AssignDriverScreen
import com.example.officeapp.screens.authentication.LoginScreen
import com.example.officeapp.screens.currentDriverTrip.DriverCompletedTripsScreen
import com.example.officeapp.screens.manageUsers.ManageUsersScreen
import com.example.officeapp.screens.manageVehicles.ManageVehiclesScreen
import com.example.officeapp.screens.searchTrips.TripDetailsScreen
import com.example.officeapp.screens.searchTrips.TripSearchScreen
import com.example.officeapp.viewModels.TripAssignmentViewModel

@Composable
fun AppNavHost(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val vehicleViewModel: VehicleViewModel = hiltViewModel()
    val tripViewModel: TripViewModel = hiltViewModel()
    val tripAssignmentViewModel: TripAssignmentViewModel = hiltViewModel()

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
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
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
                tripViewModel = tripViewModel,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
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
                },
                onGoToSearchTrips = {
                    navController.navigate(AppRoutes.SEARCH_TRIPS_ROUTE)
                },
                onGoToDriverCompletedTrips = {
                    navController.navigate(AppRoutes.DRIVER_COMPLETED_TRIPS_ROUTE)
                },
                onGoToManageUsers = {
                    navController.navigate(AppRoutes.MANAGE_USERS_ROUTE)
                },
                onGoToManageVehicles = {
                    navController.navigate(AppRoutes.MANAGE_VEHICLES_ROUTE)
                }
            )
        }

        composable(AppRoutes.ADD_USER_ROUTE) {
            AddNewUserScreen(
                viewModel = authenticationViewModel,
                isDarkTheme = isDarkTheme,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.ADD_VEHICLE_ROUTE) {
            AddNewVehicleScreen(
                viewModel = vehicleViewModel,
                isDarkTheme = isDarkTheme,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.ADD_TRIP_ROUTE) {
            AddNewTripScreen(
                viewModel = tripViewModel,
                isDarkTheme = isDarkTheme,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.SEARCH_TRIPS_ROUTE) {
            TripSearchScreen(
                viewModel = tripViewModel,
                onTripClick = { tripId ->
                    navController.navigate("${AppRoutes.TRIP_DETAILS_BASE_ROUTE}/$tripId")
                },
                onAssignDriver = { tripId ->
                    navController.navigate("${AppRoutes.ASSIGN_DRIVER_BASE_ROUTE}/$tripId")
                }
            )
        }

        composable(
            route = AppRoutes.TRIP_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(AppRoutes.TRIP_ID_ARGUMENT) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong(AppRoutes.TRIP_ID_ARGUMENT)

            if (tripId != null) {
                TripDetailsScreen(
                    viewModel = tripViewModel,
                    tripId = tripId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = AppRoutes.ASSIGN_DRIVER_ROUTE,
            arguments = listOf(
                navArgument(AppRoutes.TRIP_ID_ARGUMENT) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getLong(AppRoutes.TRIP_ID_ARGUMENT)

            if (tripId != null) {
                AssignDriverScreen(
                    tripId = tripId,
                    viewModel = tripAssignmentViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(AppRoutes.DRIVER_COMPLETED_TRIPS_ROUTE) {
            DriverCompletedTripsScreen(
                viewModel = tripViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.MANAGE_USERS_ROUTE) {
            ManageUsersScreen(
                viewModel = authenticationViewModel,
                isDarkTheme = isDarkTheme,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.MANAGE_VEHICLES_ROUTE) {
            ManageVehiclesScreen(
                viewModel = vehicleViewModel,
                isDarkTheme = isDarkTheme,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}