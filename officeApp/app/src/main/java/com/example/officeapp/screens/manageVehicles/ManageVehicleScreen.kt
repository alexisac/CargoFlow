package com.example.officeapp.screens.manageVehicles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.ui.theme.AccentBlue
import com.example.officeapp.ui.theme.AccentCyan
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.AccentViolet
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.VehicleViewModel

@Composable
fun ManageVehiclesScreen(
    viewModel: VehicleViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val subtleBorderColor = if (isDarkTheme) BorderDark else BorderLight

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.manage_vehicles_title),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = subtleBorderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearVehicles()
                    viewModel.clearMessage()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
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
            }

            if (uiState.vehicles.isEmpty() && uiState.errorMessage == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.label_no_vehicles_found),
                        color = textColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 26.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
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

                    val accentColor = when (index % 4) {
                        0 -> AccentBlue
                        1 -> AccentCyan
                        2 -> AccentViolet
                        else -> AccentPink
                    }

                    VehicleManagementCard(
                        vehicle = vehicle,
                        accentColor = accentColor,
                        isDarkTheme = isDarkTheme,
                        containerColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isDarkTheme = isDarkTheme,
            onMessageShown = { viewModel.clearMessage() }
        )
    }
}