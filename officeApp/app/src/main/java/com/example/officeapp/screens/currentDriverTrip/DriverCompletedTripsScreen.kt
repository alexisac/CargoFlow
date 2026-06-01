package com.example.officeapp.screens.currentDriverTrip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkSurface
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.TripViewModel

@Composable
fun DriverCompletedTripsScreen(
    viewModel: TripViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedDays by remember { mutableIntStateOf(30) }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    LaunchedEffect(Unit) {
        selectedDays = 30
        viewModel.clearMessage()
        viewModel.getCompletedTrips(30)
    }

    DisposableEffect(Unit) {
        onDispose {
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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearMessage()
                        onBack()
                    },
                    modifier = Modifier.align(Alignment.CenterStart),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = surfaceColor,
                        contentColor = textColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.button_back)
                    )
                }

                Text(
                    text = stringResource(R.string.completed_trips_history_title),
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CompletedTripsDaysButton(
                    text = stringResource(R.string.label_30_days),
                    selected = selectedDays == 30,
                    primaryColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    borderColor = borderColor,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedDays = 30
                        viewModel.getCompletedTrips(30)
                    }
                )

                CompletedTripsDaysButton(
                    text = stringResource(R.string.label_60_days),
                    selected = selectedDays == 60,
                    primaryColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    borderColor = borderColor,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedDays = 60
                        viewModel.getCompletedTrips(60)
                    }
                )

                CompletedTripsDaysButton(
                    text = stringResource(R.string.label_90_days),
                    selected = selectedDays == 90,
                    primaryColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    borderColor = borderColor,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedDays = 90
                        viewModel.getCompletedTrips(90)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = primaryColor
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = secondaryTextColor,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                uiState.completedTrips.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = stringResource(R.string.label_no_completed_trips_found),
                            color = secondaryTextColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(uiState.completedTrips) { trip ->
                            CompletedDriverTripCard(
                                trip = trip,
                                isDarkTheme = isDarkTheme
                            )
                        }

                        item {
                            Text(
                                text = "Showing completed trips from the last $selectedDays days",
                                color = secondaryTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 8.dp),
                            )
                        }
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

@Composable
private fun CompletedTripsDaysButton(
    text: String,
    selected: Boolean,
    primaryColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) primaryColor else borderColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) primaryColor else Color.Transparent,
            contentColor = if (selected) Color.White else secondaryTextColor
        )
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else textColor
        )
    }
}