package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.screens.reusableComponents.DatePickerField
import com.example.officeapp.screens.reusableComponents.TimePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripFiltersSheet(
    selectedStatusList: SnapshotStateList<TripStatus>,
    pickupCountries: SnapshotStateList<String>,
    pickupCities: SnapshotStateList<String>,
    deliveryCountries: SnapshotStateList<String>,
    deliveryCities: SnapshotStateList<String>,

    pickupDateTimeFrom: String,
    pickupDateTimeTo: String,
    deliveryDateTimeFrom: String,
    deliveryDateTimeTo: String,

    onPickupDateTimeFromChange: (String) -> Unit,
    onPickupDateTimeToChange: (String) -> Unit,
    onDeliveryDateTimeFromChange: (String) -> Unit,
    onDeliveryDateTimeToChange: (String) -> Unit,

    onApplyFilters: () -> Unit
) {
    var expandedStatus by remember { mutableStateOf(false) }
    var selectedStatusTemp by remember { mutableStateOf(TripStatus.PLANNED) }

    var pickupCountryInput by remember { mutableStateOf("") }
    var pickupCityInput by remember { mutableStateOf("") }
    var deliveryCountryInput by remember { mutableStateOf("") }
    var deliveryCityInput by remember { mutableStateOf("") }

    var pickupFromDate by remember { mutableStateOf("") }
    var pickupFromTime by remember { mutableStateOf("") }
    var pickupToDate by remember { mutableStateOf("") }
    var pickupToTime by remember { mutableStateOf("") }

    var deliveryFromDate by remember { mutableStateOf("") }
    var deliveryFromTime by remember { mutableStateOf("") }
    var deliveryToDate by remember { mutableStateOf("") }
    var deliveryToTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Filters")

        ExposedDropdownMenuBox(
            expanded = expandedStatus,
            onExpandedChange = { expandedStatus = !expandedStatus },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedStatusTemp.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Trip status") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expandedStatus)
                },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false }
            ) {
                TripStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name) },
                        onClick = {
                            selectedStatusTemp = status
                            expandedStatus = false
                        }
                    )
                }
            }
        }

        OutlinedButton(
            onClick = {
                if (!selectedStatusList.contains(selectedStatusTemp)) {
                    selectedStatusList.add(selectedStatusTemp)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add status filter")
        }

        AddTextFilterField(
            label = "Pickup country",
            value = pickupCountryInput,
            onValueChange = { pickupCountryInput = it },
            onAdd = {
                pickupCountryInput.trim()
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        if (!pickupCountries.contains(it)) {
                            pickupCountries.add(it)
                        }
                        pickupCountryInput = ""
                    }
            }
        )

        AddTextFilterField(
            label = "Pickup city",
            value = pickupCityInput,
            onValueChange = { pickupCityInput = it },
            onAdd = {
                pickupCityInput.trim()
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        if (!pickupCities.contains(it)) {
                            pickupCities.add(it)
                        }
                        pickupCityInput = ""
                    }
            }
        )

        AddTextFilterField(
            label = "Delivery country",
            value = deliveryCountryInput,
            onValueChange = { deliveryCountryInput = it },
            onAdd = {
                deliveryCountryInput.trim()
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        if (!deliveryCountries.contains(it)) {
                            deliveryCountries.add(it)
                        }
                        deliveryCountryInput = ""
                    }
            }
        )

        AddTextFilterField(
            label = "Delivery city",
            value = deliveryCityInput,
            onValueChange = { deliveryCityInput = it },
            onAdd = {
                deliveryCityInput.trim()
                    .takeIf { it.isNotBlank() }
                    ?.let {
                        if (!deliveryCities.contains(it)) {
                            deliveryCities.add(it)
                        }
                        deliveryCityInput = ""
                    }
            }
        )

        Text("Pickup interval")

        DatePickerField(
            label = "Pickup from date",
            value = pickupFromDate,
            onDateSelected = {
                pickupFromDate = it
                onPickupDateTimeFromChange(buildDateTime(pickupFromDate, pickupFromTime))
            }
        )

        TimePickerField(
            label = "Pickup from time",
            value = pickupFromTime,
            onTimeSelected = {
                pickupFromTime = it
                onPickupDateTimeFromChange(buildDateTime(pickupFromDate, pickupFromTime))
            }
        )

        DatePickerField(
            label = "Pickup to date",
            value = pickupToDate,
            onDateSelected = {
                pickupToDate = it
                onPickupDateTimeToChange(buildDateTime(pickupToDate, pickupToTime))
            }
        )

        TimePickerField(
            label = "Pickup to time",
            value = pickupToTime,
            onTimeSelected = {
                pickupToTime = it
                onPickupDateTimeToChange(buildDateTime(pickupToDate, pickupToTime))
            }
        )

        Text("Delivery interval")

        DatePickerField(
            label = "Delivery from date",
            value = deliveryFromDate,
            onDateSelected = {
                deliveryFromDate = it
                onDeliveryDateTimeFromChange(buildDateTime(deliveryFromDate, deliveryFromTime))
            }
        )

        TimePickerField(
            label = "Delivery from time",
            value = deliveryFromTime,
            onTimeSelected = {
                deliveryFromTime = it
                onDeliveryDateTimeFromChange(buildDateTime(deliveryFromDate, deliveryFromTime))
            }
        )

        DatePickerField(
            label = "Delivery to date",
            value = deliveryToDate,
            onDateSelected = {
                deliveryToDate = it
                onDeliveryDateTimeToChange(buildDateTime(deliveryToDate, deliveryToTime))
            }
        )

        TimePickerField(
            label = "Delivery to time",
            value = deliveryToTime,
            onTimeSelected = {
                deliveryToTime = it
                onDeliveryDateTimeToChange(buildDateTime(deliveryToDate, deliveryToTime))
            }
        )

        Button(
            onClick = onApplyFilters,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply filters")
        }
    }
}

@Composable
private fun AddTextFilterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        Button(
            onClick = onAdd,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add")
        }
    }
}

private fun buildDateTime(
    date: String,
    time: String
): String {
    if (date.isBlank() || time.isBlank()) {
        return ""
    }

    return "${date}T${time}:00"
}