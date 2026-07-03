package com.example.officeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.models.dashboard.DashboardChartItem
import com.example.officeapp.models.trip.TripDashboardSummaryItem
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.models.user.UserRole
import com.example.officeapp.models.vehicle.VehicleDashboardSummaryItem
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.screens.currentDriverTrip.CurrentDriverTripCard
import com.example.officeapp.screens.dashboard.OfficeDashboardCharts
import com.example.officeapp.screens.reusableComponents.ThemeToggle
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.TripViewModel
import com.example.officeapp.viewModels.VehicleViewModel

@Composable
fun HomeScreen(
    viewModel: AuthenticationViewModel,
    tripViewModel: TripViewModel,
    vehicleViewModel: VehicleViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onGoToAddUser: () -> Unit,
    onGoToAddVehicle: () -> Unit,
    onGoToAddTrip: () -> Unit,
    onGoToSearchTrips: () -> Unit,
    onGoToDriverCompletedTrips: () -> Unit,
    onGoToManageUsers: () -> Unit,
    onGoToManageVehicles: () -> Unit,
    onGoToDriverLocationsMap: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val tripUiState by tripViewModel.uiState.collectAsState()
    val vehicleUiState by vehicleViewModel.uiState.collectAsState()

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
        } else {
            tripViewModel.getTripDashboardSummary()
            vehicleViewModel.getVehicleDashboardSummary()
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
                        isLoading = tripUiState.isLoading,
                        onAdvanceTripStatus = { tripId, currentStatus ->
                            tripViewModel.advanceTripStatus(
                                tripId = tripId,
                                currentStatus = currentStatus
                            )
                        },
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp,)
                .padding(top = 112.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dashboard - ${uiState.userFirstName} ${uiState.userLastName} - ${uiState.userRole}",
                color = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            OfficeDashboardCharts(
                tripsByStatusItems = mapTripDashboardItems(tripUiState.tripDashboardSummaryItems),
                vehicleUtilizationItems = mapVehicleDashboardItems(vehicleUiState.vehicleDashboardSummaryItems),
                cardColor = if (isDarkTheme) DarkCard else LightSurface,
                textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight,
                secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight,
                borderColor = if (isDarkTheme) BorderDark else BorderLight
            )

            OldFormMessages(
                errorMessage = tripUiState.errorMessage
                    ?: vehicleUiState.errorMessage
                    ?: uiState.errorMessage,
                successMessage = uiState.successMessage,
                modifier = Modifier.padding(top = 16.dp),
                onMessageShown = {
                    viewModel.clearMessages()
                    tripViewModel.clearMessage()
                    vehicleViewModel.clearMessage()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

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

        OfficeHomeMenu(
            userRole = uiState.userRole,
            isDarkTheme = isDarkTheme,
            onGoToAddUser = onGoToAddUser,
            onGoToAddVehicle = onGoToAddVehicle,
            onGoToAddTrip = onGoToAddTrip,
            onGoToSearchTrips = onGoToSearchTrips,
            onGoToManageUsers = onGoToManageUsers,
            onGoToManageVehicles = onGoToManageVehicles,
            onGoToDriverLocationsMap = onGoToDriverLocationsMap,
            onLogout = onLogout
        )
    }
}

private fun mapTripDashboardItems(
    items: List<TripDashboardSummaryItem>
): List<DashboardChartItem> {
    return items.map { item ->
        DashboardChartItem(
            label = when (item.tripStatus) {
                TripStatus.PLANNED -> "Planned"
                TripStatus.ASSIGNED -> "Assigned"
                TripStatus.IN_PROGRESS -> "In progress"
                TripStatus.COMPLETED -> "Completed"
                TripStatus.CANCELED -> "Canceled"
            },
            value = item.value.toFloat(),
            color = when (item.tripStatus) {
                TripStatus.PLANNED -> Color(0xFF3F6DFF)
                TripStatus.ASSIGNED -> Color(0xFF7C3AED)
                TripStatus.IN_PROGRESS -> Color(0xFF06B6D4)
                TripStatus.COMPLETED -> Color(0xFF22C55E)
                TripStatus.CANCELED -> Color(0xFFEF4444)
            }
        )
    }
}

private fun mapVehicleDashboardItems(
    items: List<VehicleDashboardSummaryItem>
): List<DashboardChartItem> {
    val total = items.sumOf { it.value }.toFloat()

    return items.map { item ->
        val percentage = if (total == 0f) {
            0f
        } else {
            item.value.toFloat() / total * 100f
        }

        DashboardChartItem(
            label = when (item.vehicleStatus) {
                VehicleStatus.AVAILABLE -> "Available"
                VehicleStatus.NEED_MAINTENANCE -> "Maintenance"
                VehicleStatus.INACTIVE -> "Inactive"
            },
            value = percentage,
            color = when (item.vehicleStatus) {
                VehicleStatus.AVAILABLE -> Color(0xFF22C55E)
                VehicleStatus.NEED_MAINTENANCE -> Color(0xFFF97316)
                VehicleStatus.INACTIVE -> Color(0xFFEF4444)
            }
        )
    }
}