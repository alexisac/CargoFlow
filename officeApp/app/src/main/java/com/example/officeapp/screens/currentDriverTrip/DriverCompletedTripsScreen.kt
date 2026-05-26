package com.example.officeapp.screens.currentDriverTrip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.viewModels.TripViewModel

@Composable
fun DriverCompletedTripsScreen(
    viewModel: TripViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedDays by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        selectedDays = 30
        viewModel.clearMessage()
        viewModel.getCompletedTrips(30)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = stringResource(R.string.completed_trips_history_title))

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompletedTripsDaysButton(
                text = stringResource(R.string.label_30_days),
                selected = selectedDays == 30,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedDays = 30
                    viewModel.getCompletedTrips(30)
                }
            )

            CompletedTripsDaysButton(
                text = stringResource(R.string.label_60_days),
                selected = selectedDays == 60,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedDays = 60
                    viewModel.getCompletedTrips(60)
                }
            )

            CompletedTripsDaysButton(
                text = stringResource(R.string.label_90_days),
                selected = selectedDays == 90,
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedDays = 90
                    viewModel.getCompletedTrips(90)
                }
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
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            if (uiState.completedTrips.isEmpty() && uiState.errorMessage == null) {
                Text(
                    text = stringResource(R.string.label_no_completed_trips_found),
                    modifier = Modifier.padding(top = 24.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.completedTrips) { trip ->
                        CompletedDriverTripCard(trip = trip)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompletedTripsDaysButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text)
        }
    }
}