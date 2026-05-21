package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.viewModels.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripSearchScreen (
    viewModel: TripViewModel
    //onTripClick: (Long) -> Unit
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

    LaunchedEffect(Unit) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showFilters = true }
        ) {
            Text(stringResource(R.string.button_filters))
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
            onRemoveStatus = { selectedStatusList.remove(it) },
            onRemovePickupCountry = { pickupCountries.remove(it) },
            onRemovePickupCity = { pickupCities.remove(it) },
            onRemoveDeliveryCountry = { deliveryCountries.remove(it) },
            onRemoveDeliveryCity = { deliveryCities.remove(it) },
            onClearPickupDateTimeFrom = { pickupDateTimeFrom = "" },
            onClearPickupDateTimeTo = { pickupDateTimeTo = "" },
            onClearDeliveryDateTimeFrom = { deliveryDateTimeFrom = "" },
            onClearDeliveryDateTimeTo = { deliveryDateTimeTo = "" },
            modifier = Modifier.padding(top = 12.dp)
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        OldFormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier
                .padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessage() }
        )

        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.trips) { trip ->
                TripSummaryCard(
                    trip = trip,
                    onClick = {
                        // TODO: Open trip details later
                    }
                )
            }
        }
    }

    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            TripFiltersSheet(
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
                onApplyFilters = {
                    showFilters = false

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
            )
        }
    }

}