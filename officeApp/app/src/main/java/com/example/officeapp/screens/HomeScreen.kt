package com.example.officeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.models.user.UserRole
import com.example.officeapp.screens.currentDriverTrip.CurrentDriverTripCard
import com.example.officeapp.screens.reusableComponents.ThemeToggle
import com.example.officeapp.viewModels.TripViewModel

@Composable
fun HomeScreen(
    viewModel: AuthenticationViewModel,
    tripViewModel: TripViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onGoToAddUser: () -> Unit,
    onGoToAddVehicle: () -> Unit,
    onGoToAddTrip: () -> Unit,
    onGoToSearchTrips: () -> Unit,
    onGoToDriverCompletedTrips: () -> Unit,
    onGoToManageUsers: () -> Unit,
    onGoToManageVehicles: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val tripUiState by tripViewModel.uiState.collectAsState()

    val isDriver = uiState.userRole == UserRole.DRIVER.name
    val firstName = uiState.userFirstName.orEmpty()
    val lastName = uiState.userLastName.orEmpty()
    val role = uiState.userRole.orEmpty()

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    LaunchedEffect(isDriver) {
        if (isDriver) {
            tripViewModel.getCurrentTrip()
        }
    }

    if (isDriver) {
        DriverHomeContent(
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange,
            firstName = firstName,
            lastName = lastName,
            role = role,
            isLoading = tripUiState.isLoading,
            errorMessage = tripUiState.errorMessage,
            hasCurrentTrip = tripUiState.currentDriverTrip != null,
            currentTripContent = {
                tripUiState.currentDriverTrip?.let { currentTrip ->
                    CurrentDriverTripCard(
                        trip = currentTrip,
                        isDarkTheme = isDarkTheme
                    )
                }
            },
            onRefresh = {
                tripViewModel.clearMessage()
                tripViewModel.getCurrentTrip()
            },
            onGoToDriverCompletedTrips = onGoToDriverCompletedTrips,
            onLogout = onLogout
        )

        return
    }

    Box {
        ThemeToggle(
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 42.dp,
                    end = 24.dp
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Dashboard - ${uiState.userFirstName} ${uiState.userLastName} - ${uiState.userRole}")

            OldFormMessages(
                errorMessage = uiState.errorMessage,
                successMessage = uiState.successMessage,
                modifier = Modifier.padding(top = 16.dp),
                onMessageShown = { viewModel.clearMessages() }
            )

            OfficeHomeMenu(
                userRole = uiState.userRole,
                onGoToAddUser = onGoToAddUser,
                onGoToAddVehicle = onGoToAddVehicle,
                onGoToAddTrip = onGoToAddTrip,
                onGoToSearchTrips = onGoToSearchTrips,
                onGoToManageUsers = onGoToManageUsers,
                onGoToManageVehicles = onGoToManageVehicles,
                onLogout = onLogout
            )
        }
    }
}