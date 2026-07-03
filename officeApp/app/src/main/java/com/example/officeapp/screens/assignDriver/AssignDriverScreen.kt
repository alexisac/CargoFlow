package com.example.officeapp.screens.assignDriver

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.tripAssignment.AvailableVehicle
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.ui.theme.AccentCyan
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.AccentViolet
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
import com.example.officeapp.viewModels.AssignmentAiViewModel
import com.example.officeapp.viewModels.TripAssignmentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.roundToInt

@Composable
fun AssignDriverScreen(
    tripId: Long,
    viewModel: TripAssignmentViewModel,
    assignmentAiViewModel: AssignmentAiViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val tripAssignmentState = viewModel.uiState.collectAsState()
    val uiState = tripAssignmentState.value

    val assignmentAiState = assignmentAiViewModel.uiState.collectAsState()
    val assignmentAiUiState = assignmentAiState.value

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var selectedDriverId by remember { mutableStateOf<Long?>(null) }
    var selectedPrimaryVehicle by remember { mutableStateOf<AvailableVehicle?>(null) }
    var selectedTrailerVehicleId by remember { mutableStateOf<Long?>(null) }
    var aiSelectionErrorMessage by remember { mutableStateOf<String?>(null) }
    var aiSelectionSuccessMessage by remember { mutableStateOf<String?>(null) }

    val trailerEnabled = selectedPrimaryVehicle?.vehicleType == VehicleType.TRACTOR_UNIT

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight
    val personColor = AccentCyan
    val primaryVehicleColor = AccentViolet
    val trailerColor = AccentPink

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

    LaunchedEffect(assignmentAiUiState.recommendation) {
        val recommendation = assignmentAiUiState.recommendation ?: return@LaunchedEffect

        aiSelectionErrorMessage = null
        aiSelectionSuccessMessage = null

        suspend fun waitForStateChange(
            previousSize: Int,
            currentSize: () -> Int,
            isLastPage: () -> Boolean
        ): Boolean {
            return withTimeoutOrNull(8000L) {
                while (true) {
                    val state = tripAssignmentState.value

                    if (!state.isLoading &&
                        (currentSize() != previousSize || isLastPage())
                    ) {
                        return@withTimeoutOrNull true
                    }

                    delay(100L)
                }
            } == true
        }

        suspend fun findDriver(driverId: Long): Boolean {
            while (true) {
                val state = tripAssignmentState.value

                if (state.availableDrivers.any { it.id == driverId }) {
                    return true
                }

                if (state.driversLastPage) {
                    return false
                }

                val previousSize = state.availableDrivers.size

                viewModel.loadNextDriversPage(tripId)

                val updated = waitForStateChange(
                    previousSize = previousSize,
                    currentSize = { tripAssignmentState.value.availableDrivers.size },
                    isLastPage = { tripAssignmentState.value.driversLastPage }
                )

                if (!updated) {
                    return false
                }
            }
        }

        suspend fun findPrimaryVehicle(primaryVehicleId: Long): AvailableVehicle? {
            while (true) {
                val state = tripAssignmentState.value

                val vehicle = state.availablePrimaryVehicles
                    .firstOrNull { it.id == primaryVehicleId }

                if (vehicle != null) {
                    return vehicle
                }

                if (state.primaryVehiclesLastPage) {
                    return null
                }

                val previousSize = state.availablePrimaryVehicles.size

                viewModel.loadNextPrimaryVehiclesPage(tripId)

                val updated = waitForStateChange(
                    previousSize = previousSize,
                    currentSize = { tripAssignmentState.value.availablePrimaryVehicles.size },
                    isLastPage = { tripAssignmentState.value.primaryVehiclesLastPage }
                )

                if (!updated) {
                    return null
                }
            }
        }

        suspend fun findTrailer(trailerId: Long): Boolean {
            if (tripAssignmentState.value.availableTrailers.isEmpty()) {
                viewModel.getAvailableTrailersForTrip(
                    tripId = tripId,
                    pageNumber = 0,
                    pageSize = 20,
                    append = false
                )

                waitForStateChange(
                    previousSize = 0,
                    currentSize = { tripAssignmentState.value.availableTrailers.size },
                    isLastPage = { tripAssignmentState.value.trailersLastPage }
                )
            }

            while (true) {
                val state = tripAssignmentState.value

                if (state.availableTrailers.any { it.id == trailerId }) {
                    return true
                }

                if (state.trailersLastPage) {
                    return false
                }

                val previousSize = state.availableTrailers.size

                viewModel.loadNextTrailersPage(tripId)

                val updated = waitForStateChange(
                    previousSize = previousSize,
                    currentSize = { tripAssignmentState.value.availableTrailers.size },
                    isLastPage = { tripAssignmentState.value.trailersLastPage }
                )

                if (!updated) {
                    return false
                }
            }
        }

        val driverFound = findDriver(recommendation.driverId)

        if (!driverFound) {
            aiSelectionErrorMessage =
                "AI recommended driver ${recommendation.driverId}, but it was not found in the available drivers list."

            assignmentAiViewModel.clearRecommendation()
            return@LaunchedEffect
        }

        val recommendedPrimaryVehicle = findPrimaryVehicle(recommendation.primaryVehicleId)

        if (recommendedPrimaryVehicle == null) {
            aiSelectionErrorMessage =
                "AI recommended primary vehicle ${recommendation.primaryVehicleId}, but it was not found in the available vehicles list."

            assignmentAiViewModel.clearRecommendation()
            return@LaunchedEffect
        }

        selectedDriverId = recommendation.driverId
        selectedPrimaryVehicle = recommendedPrimaryVehicle

        if (recommendedPrimaryVehicle.vehicleType == VehicleType.TRACTOR_UNIT) {
            val recommendedTrailerId = recommendation.trailerId

            if (recommendedTrailerId == null) {
                aiSelectionErrorMessage =
                    "AI recommended a tractor unit, but no trailer was returned."

                assignmentAiViewModel.clearRecommendation()
                return@LaunchedEffect
            }

            val trailerFound = findTrailer(recommendedTrailerId)

            if (!trailerFound) {
                aiSelectionErrorMessage =
                    "AI recommended trailer $recommendedTrailerId, but it was not found in the available trailers list."

                assignmentAiViewModel.clearRecommendation()
                return@LaunchedEffect
            }

            selectedTrailerVehicleId = recommendedTrailerId
        } else {
            selectedTrailerVehicleId = null
            viewModel.clearTrailers()

            if (selectedTabIndex == 2) {
                selectedTabIndex = 1
            }
        }

        val confidencePercent = (recommendation.confidence * 100).roundToInt()

        aiSelectionSuccessMessage =
            "AI recommendation applied: driver ${recommendation.driverId}, primary vehicle ${recommendation.primaryVehicleId}" +
                    "${recommendation.trailerId?.let { ", trailer $it" } ?: ""}, confidence $confidencePercent%. Review and press Assign."

        selectedTabIndex = 0

        assignmentAiViewModel.clearRecommendation()
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
            AssignHeader(
                title = stringResource(R.string.button_assign_driver_for_trip) + " $tripId",
                textColor = textColor,
                borderColor = borderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessage()
                    onBack()
                }
            )

            Spacer(modifier = Modifier.height(26.dp))

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = primaryColor,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = primaryColor,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                AssignmentTab(
                    selected = selectedTabIndex == 0,
                    enabled = true,
                    text = stringResource(R.string.section_drivers),
                    icon = Icons.Outlined.Person,
                    selectedColor = personColor,
                    unselectedColor = secondaryTextColor,
                    onClick = { selectedTabIndex = 0 }
                )

                AssignmentTab(
                    selected = selectedTabIndex == 1,
                    enabled = true,
                    text = stringResource(R.string.section_primary_vehicle),
                    icon = Icons.Outlined.LocalShipping,
                    selectedColor = primaryVehicleColor,
                    unselectedColor = secondaryTextColor,
                    onClick = { selectedTabIndex = 1 }
                )

                AssignmentTab(
                    selected = selectedTabIndex == 2,
                    enabled = trailerEnabled,
                    text = stringResource(R.string.section_trailers),
                    icon = Icons.Outlined.Inventory2,
                    selectedColor = trailerColor,
                    unselectedColor = secondaryTextColor,
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
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.isLoading && selectedTabIndex == 0 && uiState.availableDrivers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    when (selectedTabIndex) {
                        0 -> {
                            AssignmentSection(
                                title = stringResource(R.string.section_drivers),
                                items = uiState.availableDrivers,
                                emptyText = stringResource(R.string.label_no_available_drivers_found),
                                canLoadMore = !uiState.driversLastPage,
                                isLoading = uiState.isLoading,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                onLoadMore = { viewModel.loadNextDriversPage(tripId) }
                            ) { driver ->
                                AvailableDriverCard(
                                    driver = driver,
                                    selected = selectedDriverId == driver.id,
                                    isDarkTheme = isDarkTheme,
                                    containerColor = cardColor,
                                    textColor = textColor,
                                    secondaryTextColor = secondaryTextColor,
                                    borderColor = borderColor,
                                    accentColor = personColor,
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
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                onLoadMore = { viewModel.loadNextPrimaryVehiclesPage(tripId) }
                            ) { vehicle ->
                                AvailableVehicleCard(
                                    vehicle = vehicle,
                                    selected = selectedPrimaryVehicle?.id == vehicle.id,
                                    enabled = true,
                                    isDarkTheme = isDarkTheme,
                                    containerColor = cardColor,
                                    textColor = textColor,
                                    secondaryTextColor = secondaryTextColor,
                                    borderColor = borderColor,
                                    accentColor = primaryVehicleColor,
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
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                onLoadMore = { viewModel.loadNextTrailersPage(tripId) }
                            ) { trailer ->
                                AvailableVehicleCard(
                                    vehicle = trailer,
                                    selected = selectedTrailerVehicleId == trailer.id,
                                    enabled = trailerEnabled,
                                    isDarkTheme = isDarkTheme,
                                    containerColor = cardColor,
                                    textColor = textColor,
                                    secondaryTextColor = secondaryTextColor,
                                    borderColor = borderColor,
                                    accentColor = trailerColor,
                                    onClick = { selectedTrailerVehicleId = trailer.id }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadingButton(
                    text = stringResource(R.string.button_assign),
                    isLoading = false,
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
                        .weight(1f)
                        .height(58.dp),
                    enabled = selectedDriverId != null &&
                            selectedPrimaryVehicle != null &&
                            (!trailerEnabled || selectedTrailerVehicleId != null)
                )

                Spacer(modifier = Modifier.width(12.dp))

                LoadingButton(
                    text = stringResource(R.string.button_ai_recommendation),
                    isLoading = assignmentAiUiState.isLoading,
                    onClick = {
                        assignmentAiViewModel.autoOptimizeTripAssignment(tripId)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp),
                    enabled = !assignmentAiUiState.isLoading
                )
            }
        }

        FormMessages(
            errorMessage = aiSelectionErrorMessage
                ?: assignmentAiUiState.errorMessage
                ?: uiState.errorMessage,
            successMessage = aiSelectionSuccessMessage
                ?: uiState.successMessage,
            isDarkTheme = isDarkTheme,
            onMessageShown = {
                viewModel.clearMessage()
                assignmentAiViewModel.clearMessage()
                aiSelectionErrorMessage = null
                aiSelectionSuccessMessage = null
            }
        )
    }
}