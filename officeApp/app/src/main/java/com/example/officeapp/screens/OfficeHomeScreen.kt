package com.example.officeapp.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.user.UserRole

@Composable
fun OfficeHomeMenu(
    userRole: String?,
    onGoToAddUser: () -> Unit,
    onGoToAddVehicle: () -> Unit,
    onGoToAddTrip: () -> Unit,
    onGoToSearchTrips: () -> Unit,
    onGoToManageUsers: () -> Unit,
    onLogout: () -> Unit
) {
    if (userRole == UserRole.ADMIN.name) {
        Button(
            onClick = onGoToAddUser,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(stringResource(R.string.add_new_user_title))
        }

        Button(
            onClick = onGoToManageUsers,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.manage_users_title))
        }
    }

    if (userRole in listOf(
            UserRole.DISPATCHER.name,
            UserRole.MANAGER.name,
            UserRole.ADMIN.name
        )
    ) {
        Button(
            onClick = onGoToAddVehicle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.add_new_vehicle_title))
        }

        Button(
            onClick = onGoToAddTrip,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.add_new_trip_title))
        }

        Button(
            onClick = onGoToSearchTrips,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.search_and_associate_trips_title))
        }
    }

    OutlinedButton(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(stringResource(R.string.button_logout))
    }
}