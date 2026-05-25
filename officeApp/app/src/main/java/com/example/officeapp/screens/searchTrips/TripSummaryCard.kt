package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.models.trip.TripSummary

@Composable
fun TripSummaryCard(
    trip: TripSummary,
    onClick: () -> Unit,
    onAssignDriver: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = trip.tripStatus.name,
            )

            Row(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.section_pickup),
                    )

                    Text(
                        text = "${trip.pickupCity}, ${trip.pickupCountry}",
                    )

                    Text(
                        text = "${trip.pickupDateTime} | ${trip.pickupTimeZone}",
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.section_delivery),
                    )

                    Text(
                        text = "${trip.deliveryCity}, ${trip.deliveryCountry}",
                    )

                    Text(
                        text = "${trip.deliveryDateTime} | ${trip.deliveryTimeZone}",
                    )
                }
            }

            if (trip.tripStatus == TripStatus.PLANNED) {
                Button(
                    onClick = onAssignDriver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.button_assign_driver_for_trip))
                }
            }
        }
    }
}