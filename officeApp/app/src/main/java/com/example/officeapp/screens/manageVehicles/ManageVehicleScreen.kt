package com.example.officeapp.screens.manageVehicles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.viewModels.VehicleViewModel

@Composable
fun ManageVehiclesScreen(
    viewModel: VehicleViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllVehicles(
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearVehicles()
            viewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = stringResource(R.string.manage_vehicles_title))

        OutlinedButton(
            onClick = {
                viewModel.clearVehicles()
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

        if (uiState.isLoading && uiState.vehicles.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

            return
        }

        if (uiState.vehicles.isEmpty() && uiState.errorMessage == null) {
            Text(
                text = stringResource(R.string.label_no_vehicles_found),
                modifier = Modifier.padding(top = 24.dp)
            )

            return
        }

        LazyColumn(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(uiState.vehicles) { index, vehicle ->
                val shouldLoadMore = index >= uiState.vehicles.size - 5

                if (
                    shouldLoadMore &&
                    !uiState.isLoading &&
                    !uiState.lastPage
                ) {
                    LaunchedEffect(uiState.vehicles.size) {
                        viewModel.loadNextVehiclesPage()
                    }
                }

                VehicleManagementCard(
                    vehicle = vehicle,
                    onChangeStatusClick = { newStatus ->
                        viewModel.changeVehicleStatus(
                            vehicleId = vehicle.id,
                            vehicleStatus = newStatus
                        )
                    }
                )
            }

            if (uiState.isLoading && uiState.vehicles.isNotEmpty()) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}