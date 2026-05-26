package com.example.officeapp.screens.currentDriverTrip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.CurrentTrip

@Composable
fun CurrentDriverTripCard(
    trip: CurrentTrip
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(R.string.current_trip_title))
            Text(text = stringResource(R.string.label_status) + ": " + trip.tripStatus.name)

            Text(
                text = stringResource(R.string.label_pickup),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(text = "${trip.pickupAddress.city}, ${trip.pickupAddress.country}")
            Text(text = "${trip.pickupDateTime} | ${trip.pickupTimeZone}")

            Text(
                text = stringResource(R.string.label_delivery),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(text = "${trip.deliveryAddress.city}, ${trip.deliveryAddress.country}")
            Text(text = "${trip.deliveryDateTime} | ${trip.deliveryTimeZone}")

            Text(
                text = stringResource(R.string.label_cargo),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(text = stringResource(R.string.label_type) + ": " + trip.cargoType.name)

            trip.cargoDescription?.let {
                Text(text = stringResource(R.string.label_description) + ": " +it)
            }

            trip.cargoWeight?.let {
                Text(text = stringResource(R.string.label_weight) + ": " + it + " " + stringResource(R.string.label_kilograms))
            }

            trip.cargoVolume?.let {
                Text(text = stringResource(R.string.label_volume) + ": " + it + " " + stringResource(R.string.label_cubic_meters))
            }

            trip.additionalInfo?.let {
                Text(
                    text = stringResource(R.string.label_additional_info) + ": " + it,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}