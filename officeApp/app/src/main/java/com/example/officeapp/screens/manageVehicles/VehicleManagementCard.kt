package com.example.officeapp.screens.manageVehicles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleManagementCard(
    vehicle: VehicleSummary,
    onChangeStatusClick: (VehicleStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = vehicle.licencePlate)

            Text(
                text = "${vehicle.brand} ${vehicle.model}",
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.label_vin) + ": " + vehicle.vin,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.label_type) + ": " + vehicle.vehicleType.name,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.label_status) + ": " + vehicle.vehicleStatus.name,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.label_manufacture_year) + ": " + vehicle.manufactureYear,
                modifier = Modifier.padding(top = 4.dp)
            )

            vehicle.maxWeight?.let {
                Text(
                    text = stringResource(R.string.label_maximum_weight) + ": $it",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            vehicle.maxVolume?.let {
                Text(
                    text = stringResource(R.string.label_maximum_volume) + ": $it",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            vehicle.additionalInfo?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                OutlinedTextField(
                    value = vehicle.vehicleStatus.name,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(stringResource(R.string.label_status))
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    VehicleStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = {
                                Text(status.name)
                            },
                            onClick = {
                                expanded = false

                                if (status != vehicle.vehicleStatus) {
                                    onChangeStatusClick(status)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}