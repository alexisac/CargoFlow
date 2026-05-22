package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.viewModels.TripViewModel

@Composable
fun TripDetailsScreen(
    viewModel: TripViewModel,
    tripId: Long,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tripId) {
        viewModel.getTrip(tripId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.button_back)
                )
            }

            Text(text = stringResource(R.string.trip_details_title))
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.currentTrip != null -> {
                uiState.currentTrip?.let { currentTrip ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(R.string.label_general))
                        DetailRow(stringResource(R.string.label_id), currentTrip.id.toString())
                        DetailRow(stringResource(R.string.label_status), currentTrip.tripStatus.toString())
                        DetailRow(stringResource(R.string.label_created_by), currentTrip.createdBy)

                        Divider()

                        Text(text = stringResource(R.string.label_pickup))
                        DetailRow(stringResource(R.string.label_country), currentTrip.pickupAddress.country)
                        DetailRow(stringResource(R.string.label_administrative_area), currentTrip.pickupAddress.administrativeArea)
                        DetailRow(stringResource(R.string.label_city), currentTrip.pickupAddress.city)
                        DetailRow(stringResource(R.string.label_street_name), currentTrip.pickupAddress.streetName)
                        DetailRow(stringResource(R.string.label_street_number), currentTrip.pickupAddress.streetNumber)
                        DetailRow(stringResource(R.string.label_postal_code), currentTrip.pickupAddress.postalCode)
                        DetailRow(stringResource(R.string.label_additional_details), currentTrip.pickupAddress.additionalDetails ?: "-")
                        DetailRow(stringResource(R.string.label_pickup_date_time), currentTrip.pickupDateTime)
                        DetailRow(stringResource(R.string.label_pickup_time_zone), currentTrip.pickupTimeZone)

                        Divider()

                        Text(text = stringResource(R.string.label_delivery))
                        DetailRow(stringResource(R.string.label_country), currentTrip.deliveryAddress.country)
                        DetailRow(stringResource(R.string.label_administrative_area), currentTrip.deliveryAddress.administrativeArea)
                        DetailRow(stringResource(R.string.label_city), currentTrip.deliveryAddress.city)
                        DetailRow(stringResource(R.string.label_street_name), currentTrip.deliveryAddress.streetName)
                        DetailRow(stringResource(R.string.label_street_number), currentTrip.deliveryAddress.streetNumber)
                        DetailRow(stringResource(R.string.label_postal_code), currentTrip.deliveryAddress.postalCode)
                        DetailRow(stringResource(R.string.label_additional_details), currentTrip.deliveryAddress.additionalDetails ?: "-")
                        DetailRow(stringResource(R.string.label_delivery_date_time), currentTrip.deliveryDateTime)
                        DetailRow(stringResource(R.string.label_delivery_time_zone), currentTrip.deliveryTimeZone)

                        Divider()

                        Text(text = stringResource(R.string.label_cargo))
                        DetailRow(stringResource(R.string.label_cargo_type), currentTrip.cargoType.toString())
                        DetailRow(stringResource(R.string.label_cargo_description), currentTrip.cargoDescription ?: "-")
                        DetailRow(stringResource(R.string.label_cargo_weight), currentTrip.cargoWeight?.toString() ?: "-")
                        DetailRow(stringResource(R.string.label_cargo_volume), currentTrip.cargoVolume?.toString() ?: "-")

                        Divider()

                        Text(text = stringResource(R.string.label_price))
                        DetailRow(stringResource(R.string.label_price), currentTrip.price.toString())
                        DetailRow(stringResource(R.string.label_currency), currentTrip.currency.toString())

                        Divider()

                        Text(text = stringResource(R.string.label_additional_info))
                        DetailRow(stringResource(R.string.label_additional_info), currentTrip.additionalInfo ?: "-")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label)
        Text(text = value)
    }
}