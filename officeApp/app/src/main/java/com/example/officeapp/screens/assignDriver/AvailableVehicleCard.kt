package com.example.officeapp.screens.assignDriver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.tripAssignment.AvailableVehicle

@Composable
fun AvailableVehicleCard(
    vehicle: AvailableVehicle,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = vehicle.licencePlate
            )

            Text(
                text = vehicle.vehicleType.name,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (!enabled) {
                Text(
                    text = stringResource(R.string.label_disabled),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (selected) {
                Text(
                    text = stringResource(R.string.label_selected),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}