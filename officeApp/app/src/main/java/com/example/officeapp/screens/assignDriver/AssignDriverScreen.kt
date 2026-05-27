package com.example.officeapp.screens.assignDriver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.tripAssignment.AvailableVehicle
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.viewModels.TripAssignmentViewModel

@Composable
fun AssignDriverScreen(
    tripId: Long,
    viewModel: TripAssignmentViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var selectedDriverId by remember { mutableStateOf<Long?>(null) }
    var selectedPrimaryVehicle by remember { mutableStateOf<AvailableVehicle?>(null) }
    var selectedTrailerVehicleId by remember { mutableStateOf<Long?>(null) }

    val trailerEnabled = selectedPrimaryVehicle?.vehicleType == VehicleType.TRACTOR_UNIT

    LaunchedEffect(tripId) {
        viewModel.getAvailableDriversForTrip(
            tripId = tripId,
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
        viewModel.getAvailablePrimaryVehiclesForTrip(
            tripId = tripId,
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.button_assign_driver_for_trip) + " $tripId"
        )

        OutlinedButton(
            onClick = {
                viewModel.clearMessage()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.button_back))
        }

        OldFormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier.padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessage() }
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(stringResource(R.string.section_drivers)) }
            )

            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(stringResource(R.string.section_primary_vehicle)) }
            )

            Tab(
                selected = selectedTabIndex == 2,
                enabled = trailerEnabled,
                onClick = {
                    if (trailerEnabled) {
                        selectedTabIndex = 2

                        if (uiState.availableTrailers.isEmpty()) {
                            viewModel.getAvailableTrailersForTrip(
                                tripId = tripId,
                                pageNumber = 0,
                                pageSize = 20,
                                append = false
                            )
                        }
                    }
                },
                text = { Text(stringResource(R.string.section_trailers)) }
            )
        }

        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            when (selectedTabIndex) {
                0 -> {
                    AssignmentSection(
                        title = stringResource(R.string.section_drivers),
                        items = uiState.availableDrivers,
                        emptyText = stringResource(R.string.label_no_available_drivers_found),
                        canLoadMore = !uiState.driversLastPage,
                        isLoading = uiState.isLoading,
                        onLoadMore = { viewModel.loadNextDriversPage(tripId) }
                    ) { driver ->
                        AvailableDriverCard(
                            driver = driver,
                            selected = selectedDriverId == driver.id,
                            onClick = { selectedDriverId = driver.id }
                        )
                    }
                }

                1 -> {
                    AssignmentSection(
                        title = stringResource(R.string.section_primary_vehicle),
                        items = uiState.availablePrimaryVehicles,
                        emptyText = stringResource(R.string.label_no_available_primary_vehicles_found),
                        canLoadMore = !uiState.primaryVehiclesLastPage,
                        isLoading = uiState.isLoading,
                        onLoadMore = { viewModel.loadNextPrimaryVehiclesPage(tripId) }
                    ) { vehicle ->
                        AvailableVehicleCard(
                            vehicle = vehicle,
                            selected = selectedPrimaryVehicle?.id == vehicle.id,
                            enabled = true,
                            onClick = {
                                selectedPrimaryVehicle = vehicle

                                if (vehicle.vehicleType == VehicleType.TRACTOR_UNIT) {
                                    selectedTrailerVehicleId = null

                                    if (uiState.availableTrailers.isEmpty()) {
                                        viewModel.getAvailableTrailersForTrip(
                                            tripId = tripId,
                                            pageNumber = 0,
                                            pageSize = 20,
                                            append = false
                                        )
                                    }
                                } else {
                                    selectedTrailerVehicleId = null
                                    viewModel.clearTrailers()

                                    if (selectedTabIndex == 2) {
                                        selectedTabIndex = 1
                                    }
                                }
                            }
                        )
                    }
                }

                2 -> {
                    AssignmentSection(
                        title = stringResource(R.string.section_trailers),
                        items = uiState.availableTrailers,
                        emptyText = stringResource(R.string.label_no_available_trailers_found),
                        canLoadMore = !uiState.trailersLastPage,
                        enabled = trailerEnabled,
                        isLoading = uiState.isLoading,
                        onLoadMore = {
                            viewModel.loadNextTrailersPage(tripId)
                        }
                    ) { trailer ->
                        AvailableVehicleCard(
                            vehicle = trailer,
                            selected = selectedTrailerVehicleId == trailer.id,
                            enabled = trailerEnabled,
                            onClick = {
                                selectedTrailerVehicleId = trailer.id
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                val driverId = selectedDriverId
                val primaryVehicle = selectedPrimaryVehicle

                if (driverId != null && primaryVehicle != null) {
                    viewModel.assignTrip(
                        tripId = tripId,
                        driverId = driverId,
                        primaryVehicleId = primaryVehicle.id,
                        trailerVehicleId = if (primaryVehicle.vehicleType == VehicleType.TRACTOR_UNIT) {
                            selectedTrailerVehicleId
                        } else {
                            null
                        }
                    )
                    selectedDriverId = null
                    selectedPrimaryVehicle = null
                    selectedTrailerVehicleId = null
                    onBack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = selectedDriverId != null &&
                    selectedPrimaryVehicle != null &&
                    (!trailerEnabled || selectedTrailerVehicleId != null)
        ) {
            Text(stringResource(R.string.button_assign))
        }
    }
}