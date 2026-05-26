package com.example.officeapp.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R

@Composable
fun DriverHomeContent(
    isLoading: Boolean,
    errorMessage: String?,
    hasCurrentTrip: Boolean,
    currentTripContent: @Composable () -> Unit,
    onGoToDriverCompletedTrips: () -> Unit,
    onLogout: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.padding(top = 32.dp)
        )
    } else {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else if (hasCurrentTrip) {
            currentTripContent()
        } else {
            Text(
                text = stringResource(R.string.label_no_current_trip_found),
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }

    Button(
        onClick = {
            menuExpanded = !menuExpanded
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(stringResource(R.string.button_menu))
    }

    if (menuExpanded) {
        Button(
            onClick = onGoToDriverCompletedTrips,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.button_completed_trips_history))
        }
    }

    OutlinedButton(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(stringResource(R.string.button_logout))
    }
}