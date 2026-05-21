package com.example.officeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.models.user.UserRole

@Composable
fun HomeScreen(
    viewModel: AuthenticationViewModel,
    onLogout: () -> Unit,
    onGoToAddUser: () -> Unit,
    onGoToAddVehicle: () -> Unit,
    onGoToAddTrip: () -> Unit,
    onGoToSearchTrips: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (text = "Dashboard - ${uiState.userRole}")

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier
                .padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessages() }
        )

        if(uiState.userRole == UserRole.ADMIN.name) {
            Button(
                onClick = onGoToAddUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text("Add new user")
            }
        }

        if (uiState.userRole in listOf(
                UserRole.DISPATCHER.name,
                UserRole.MANAGER.name,
                UserRole.ADMIN.name
        )) {
            Button(
                onClick = onGoToAddVehicle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Add new vehicle")
            }
        }

        if (uiState.userRole in listOf(
                UserRole.DISPATCHER.name,
                UserRole.MANAGER.name,
                UserRole.ADMIN.name
            )) {
            Button(
                onClick = onGoToAddTrip,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Add new trip")
            }
        }

        if (uiState.userRole in listOf(
                UserRole.DISPATCHER.name,
                UserRole.MANAGER.name,
                UserRole.ADMIN.name
            )) {
            Button(
                onClick = onGoToSearchTrips,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Search trips")
            }
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Logout")
        }
    }
}