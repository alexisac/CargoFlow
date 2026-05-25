package com.example.officeapp.screens.assignDriver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    var selectedDriverId by remember { mutableStateOf<Long?>(null) }
    var selectedPrimaryVehicle by remember { mutableStateOf<AvailableVehicle?>(null) }
    var selectedTrailerVehicleId by remember { mutableStateOf<Long?>(null) }

    val trailerEnabled = selectedPrimaryVehicle?.vehicleType == VehicleType.TRACTOR_UNIT

    LaunchedEffect(tripId) {
        viewModel.getAvailableDriversForTrip(tripId)
        viewModel.getAvailableVehiclesForTrip(tripId)
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
            onClick = onBack,
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
            LazyColumn(
                modifier = Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(text = stringResource(R.string.section_drivers))
                }

                if (uiState.availableDrivers.isEmpty() && uiState.errorMessage == null) {
                    item {
                        Text(
                            text = stringResource(R.string.label_no_available_drivers_found),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } else {
                    items(uiState.availableDrivers) { driver ->
                        AvailableDriverCard(
                            driver = driver,
                            selected = selectedDriverId == driver.id,
                            onClick = {
                                selectedDriverId = driver.id
                            }
                        )
                    }
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Text(text = stringResource(R.string.section_primary_vehicle))
                }

                if (uiState.availablePrimaryVehicles.isEmpty() && uiState.errorMessage == null) {
                    item {
                        Text(
                            text = stringResource(R.string.label_no_available_primary_vehicle_found),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } else {
                    items(uiState.availablePrimaryVehicles) { vehicle ->
                        AvailableVehicleCard(
                            vehicle = vehicle,
                            selected = selectedPrimaryVehicle?.id == vehicle.id,
                            enabled = true,
                            onClick = {
                                selectedPrimaryVehicle = vehicle

                                if (vehicle.vehicleType != VehicleType.TRACTOR_UNIT) {
                                    selectedTrailerVehicleId = null
                                }
                            }
                        )
                    }
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Text(text = stringResource(R.string.section_trailers))
                }

                if (!trailerEnabled) {
                    item {
                        Text(
                            text = stringResource(R.string.label_select_tractor_unit_for_trailer),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } else {
                    if (uiState.availableTrailers.isEmpty() && uiState.errorMessage == null) {
                        item {
                            Text(
                                text = stringResource(R.string.label_no_available_trailer_found),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    } else {
                        items(uiState.availableTrailers) { trailer ->
                            AvailableVehicleCard(
                                vehicle = trailer,
                                selected = selectedTrailerVehicleId == trailer.id,
                                enabled = true,
                                onClick = {
                                    selectedTrailerVehicleId = trailer.id
                                }
                            )
                        }
                    }
                }
                item {
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
        }
    }
}