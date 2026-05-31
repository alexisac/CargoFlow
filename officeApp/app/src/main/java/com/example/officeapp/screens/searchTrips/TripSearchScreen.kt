package com.example.officeapp.screens.searchTrips

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripSearchScreen(
    viewModel: TripViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit,
    onTripClick: (Long) -> Unit,
    onAssignDriver: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showFilters by remember { mutableStateOf(false) }

    val selectedStatusList = remember { mutableStateListOf<TripStatus>() }
    val pickupCountries = remember { mutableStateListOf<String>() }
    val pickupCities = remember { mutableStateListOf<String>() }
    val deliveryCountries = remember { mutableStateListOf<String>() }
    val deliveryCities = remember { mutableStateListOf<String>() }

    var pickupDateTimeFrom by remember { mutableStateOf("") }
    var pickupDateTimeTo by remember { mutableStateOf("") }
    var deliveryDateTimeFrom by remember { mutableStateOf("") }
    var deliveryDateTimeTo by remember { mutableStateOf("") }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    val listState = rememberLazyListState()

    fun searchFirstPage() {
        viewModel.searchTrips(
            tripStatusList = selectedStatusList.toList(),
            pickupCountries = pickupCountries.toList(),
            pickupCities = pickupCities.toList(),
            deliveryCountries = deliveryCountries.toList(),
            deliveryCities = deliveryCities.toList(),
            pickupDateTimeFrom = pickupDateTimeFrom,
            pickupDateTimeTo = pickupDateTimeTo,
            deliveryDateTimeFrom = deliveryDateTimeFrom,
            deliveryDateTimeTo = deliveryDateTimeTo,
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
    }

    fun clearFilters() {
        selectedStatusList.clear()
        pickupCountries.clear()
        pickupCities.clear()
        deliveryCountries.clear()
        deliveryCities.clear()
        pickupDateTimeFrom = ""
        pickupDateTimeTo = ""
        deliveryDateTimeFrom = ""
        deliveryDateTimeTo = ""
    }

    val shouldLoadNextPage by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = listState.layoutInfo.totalItemsCount

            totalItemsCount > 0 &&
                    lastVisibleItemIndex >= totalItemsCount - 5 &&
                    !uiState.isLoading &&
                    !uiState.lastPage
        }
    }

    LaunchedEffect(Unit) {
        searchFirstPage()
    }

    LaunchedEffect(shouldLoadNextPage) {
        if (shouldLoadNextPage) {
            viewModel.loadNextTripsPage(
                tripStatusList = selectedStatusList.toList(),
                pickupCountries = pickupCountries.toList(),
                pickupCities = pickupCities.toList(),
                deliveryCountries = deliveryCountries.toList(),
                deliveryCities = deliveryCities.toList(),
                pickupDateTimeFrom = pickupDateTimeFrom,
                pickupDateTimeTo = pickupDateTimeTo,
                deliveryDateTimeFrom = deliveryDateTimeFrom,
                deliveryDateTimeTo = deliveryDateTimeTo
            )
        }
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
            FormScreenHeader(
                title = stringResource(R.string.trips_title),
                subtitle = stringResource(R.string.all_trips_subtitle),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = borderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessage()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showFilters = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryColor
                    )
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Text(stringResource(R.string.button_filters))
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            FilterChipRow(
                selectedStatusList = selectedStatusList,
                pickupCountries = pickupCountries,
                pickupCities = pickupCities,
                deliveryCountries = deliveryCountries,
                deliveryCities = deliveryCities,
                pickupDateTimeFrom = pickupDateTimeFrom,
                pickupDateTimeTo = pickupDateTimeTo,
                deliveryDateTimeFrom = deliveryDateTimeFrom,
                deliveryDateTimeTo = deliveryDateTimeTo,
                textColor = textColor,
                containerColor = cardColor,
                borderColor = borderColor,
                onRemoveStatus = {
                    selectedStatusList.remove(it)
                    searchFirstPage()
                },
                onRemovePickupCountry = {
                    pickupCountries.remove(it)
                    searchFirstPage()
                },
                onRemovePickupCity = {
                    pickupCities.remove(it)
                    searchFirstPage()
                },
                onRemoveDeliveryCountry = {
                    deliveryCountries.remove(it)
                    searchFirstPage()
                },
                onRemoveDeliveryCity = {
                    deliveryCities.remove(it)
                    searchFirstPage()
                },
                onClearPickupDateTimeFrom = {
                    pickupDateTimeFrom = ""
                    searchFirstPage()
                },
                onClearPickupDateTimeTo = {
                    pickupDateTimeTo = ""
                    searchFirstPage()
                },
                onClearDeliveryDateTimeFrom = {
                    deliveryDateTimeFrom = ""
                    searchFirstPage()
                },
                onClearDeliveryDateTimeTo = {
                    deliveryDateTimeTo = ""
                    searchFirstPage()
                },
                modifier = Modifier.padding(top = 14.dp)
            )

            if (uiState.isLoading && uiState.trips.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.trips) { trip ->
                    TripSummaryCard(
                        trip = trip,
                        isDarkTheme = isDarkTheme,
                        containerColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        onClick = { onTripClick(trip.id) },
                        onAssignDriver = { onAssignDriver(trip.id) },
                        onCancelTrip = { viewModel.cancelTrip(trip.id) }
                    )
                }

                if (uiState.isLoading && uiState.trips.isNotEmpty()) {
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

    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = surfaceColor
        ) {
            TripFiltersSheet(
                isDarkTheme = isDarkTheme,
                selectedStatusList = selectedStatusList,
                pickupCountries = pickupCountries,
                pickupCities = pickupCities,
                deliveryCountries = deliveryCountries,
                deliveryCities = deliveryCities,
                pickupDateTimeFrom = pickupDateTimeFrom,
                pickupDateTimeTo = pickupDateTimeTo,
                deliveryDateTimeFrom = deliveryDateTimeFrom,
                deliveryDateTimeTo = deliveryDateTimeTo,
                onPickupDateTimeFromChange = { pickupDateTimeFrom = it },
                onPickupDateTimeToChange = { pickupDateTimeTo = it },
                onDeliveryDateTimeFromChange = { deliveryDateTimeFrom = it },
                onDeliveryDateTimeToChange = { deliveryDateTimeTo = it },
                onResetFilters = {
                    clearFilters()
                },
                onApplyFilters = {
                    showFilters = false
                    searchFirstPage()
                }
            )
        }
    }
}