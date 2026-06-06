package com.example.officeapp.screens.driverLocations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun DriverLocationsMapScreen(
    viewModel: LocationViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var cameraInitialized by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    val defaultLocation = LatLng(44.4268, 26.1025)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    LaunchedEffect(Unit) {
        viewModel.getLatestDriverLocations()
        viewModel.startLiveDriverLocationUpdates()
    }

    LaunchedEffect(uiState.driverLocations) {
        if (!cameraInitialized) {
            val firstLocation = uiState.driverLocations.firstOrNull()

            if (firstLocation != null) {
                cameraInitialized = true

                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            firstLocation.latitude,
                            firstLocation.longitude
                        ),
                        11f
                    )
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLiveDriverLocationUpdates()
            viewModel.clearDriverLocations()
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
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.live_driver_locations_title),
                subtitle = stringResource(R.string.live_driver_locations_subtitle),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = borderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessage()
                    onBack()
                },
                onRefresh = {
                    viewModel.refreshDriverLocations()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            DriverLocationsStatusCard(
                driversCount = uiState.driverLocations.size,
                isConnected = uiState.isWebSocketConnected,
                cardColor = cardColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                primaryColor = primaryColor
            )

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    uiState.driverLocations.forEach { driverLocation ->
                        key(
                            driverLocation.driverId,
                            driverLocation.latitude,
                            driverLocation.longitude,
                            driverLocation.updatedAt
                        ) {
                            Marker(
                                state = rememberUpdatedMarkerState(
                                    position = LatLng(
                                        driverLocation.latitude,
                                        driverLocation.longitude
                                    )
                                ),
                                title = "Driver #${driverLocation.driverId}",
                                snippet = driverLocation.updatedAt
                            )
                        }
                    }
                }

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = primaryColor
                        )
                    }
                }

                if (
                    !uiState.isLoading &&
                    uiState.driverLocations.isEmpty() &&
                    uiState.errorMessage == null
                ) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardColor
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.label_no_driver_locations_found),
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isDarkTheme = isDarkTheme,
            onMessageShown = {
                viewModel.clearMessage()
            }
        )
    }
}
