package com.example.officeapp.features.addTrip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.officeapp.features.addTrip.TripViewModel
import com.example.officeapp.features.reusableComponents.DatePickerField
import com.example.officeapp.features.reusableComponents.FormMessages
import com.example.officeapp.features.reusableComponents.LoadingButton
import com.example.officeapp.features.reusableComponents.TimePickerField
import com.example.officeapp.features.reusableComponents.TimeZoneDropdownField
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.Currency
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTripScreen(
    viewModel: TripViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var pickupCountry by remember { mutableStateOf("") }
    var pickupAdministrativeArea by remember { mutableStateOf("") }
    var pickupCity by remember { mutableStateOf("") }
    var pickupStreetName by remember { mutableStateOf("") }
    var pickupStreetNumber by remember { mutableStateOf("") }
    var pickupPostalCode by remember { mutableStateOf("") }
    var pickupAdditionalDetails by remember { mutableStateOf("") }

    var deliveryCountry by remember { mutableStateOf("") }
    var deliveryAdministrativeArea by remember { mutableStateOf("") }
    var deliveryCity by remember { mutableStateOf("") }
    var deliveryStreetName by remember { mutableStateOf("") }
    var deliveryStreetNumber by remember { mutableStateOf("") }
    var deliveryPostalCode by remember { mutableStateOf("") }
    var deliveryAdditionalDetails by remember { mutableStateOf("") }

    var pickupDate by remember { mutableStateOf("") }
    var pickupTime by remember { mutableStateOf("") }
    var pickupTimeZone by remember { mutableStateOf(ZoneId.systemDefault().id) }
    var deliveryDate by remember { mutableStateOf("") }
    var deliveryTime by remember { mutableStateOf("") }
    var deliveryTimeZone by remember { mutableStateOf(ZoneId.systemDefault().id) }

    var cargoDescription by remember { mutableStateOf("") }
    var cargoWeight by remember { mutableStateOf("") }
    var cargoVolume by remember { mutableStateOf("") }
    var cargoType by remember { mutableStateOf(CargoType.GENERAL) }
    var price by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf(Currency.RON) }
    var additionalInfo by remember { mutableStateOf("") }

    var pickupAddressExpanded by remember { mutableStateOf(false) }
    var deliveryAddressExpanded by remember { mutableStateOf(false) }
    var timeExpanded by remember { mutableStateOf(false) }
    var cargoAndPaymentExpanede by remember { mutableStateOf(false) }

    var cargoTypeExpanded by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessage()
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Add new trip",
            modifier = Modifier
                .padding(bottom = 24.dp)
        )

        TripSection(
            title = "Pickup address",
            expanded = pickupAddressExpanded,
            onClick = { pickupAddressExpanded = !pickupAddressExpanded }
        ) {
            OutlinedTextField(
                value = pickupCountry,
                onValueChange = { pickupCountry = it },
                label = { Text("Country") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupAdministrativeArea,
                onValueChange = { pickupAdministrativeArea = it },
                label = { Text("Administrative Area") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupCity,
                onValueChange = { pickupCity = it },
                label = { Text("City") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupStreetName,
                onValueChange = { pickupStreetName = it },
                label = { Text("Street name") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupStreetNumber,
                onValueChange = { pickupStreetNumber = it },
                label = { Text("Street number") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupPostalCode,
                onValueChange = { pickupPostalCode = it },
                label = { Text("Postal code") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = pickupAdditionalDetails,
                onValueChange = { pickupAdditionalDetails = it },
                label = { Text("Additional details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                minLines = 3,
                maxLines = 5,
                singleLine = false
            )
        }

        TripSection(
            title = "Delivery address",
            expanded = deliveryAddressExpanded,
            onClick = { deliveryAddressExpanded = !deliveryAddressExpanded }
        ) {
            OutlinedTextField(
                value = deliveryCountry,
                onValueChange = { deliveryCountry = it },
                label = { Text("Country") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryAdministrativeArea,
                onValueChange = { deliveryAdministrativeArea = it },
                label = { Text("Administrative Area") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryCity,
                onValueChange = { deliveryCity = it },
                label = { Text("City") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryStreetName,
                onValueChange = { deliveryStreetName = it },
                label = { Text("Street name") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryStreetNumber,
                onValueChange = { deliveryStreetNumber = it },
                label = { Text("Street number") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryPostalCode,
                onValueChange = { deliveryPostalCode = it },
                label = { Text("Postal code") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deliveryAdditionalDetails,
                onValueChange = { deliveryAdditionalDetails = it },
                label = { Text("Additional details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                minLines = 3,
                maxLines = 5,
                singleLine = false
            )
        }

        TripSection(
            title = "Time",
            expanded = timeExpanded,
            onClick = { timeExpanded = !timeExpanded }
        ) {
            DatePickerField(
                label = "Pickup date",
                value = pickupDate,
                onDateSelected = { pickupDate = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )

            TimePickerField(
                label = "Pickup time",
                value = pickupTime,
                onTimeSelected = { pickupTime = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )

            TimeZoneDropdownField(
                label = "Pickup time zone",
                selectedTimeZone = pickupTimeZone,
                onTimeZoneSelected = { pickupTimeZone = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )

            DatePickerField(
                label = "Delivery date",
                value = deliveryDate,
                onDateSelected = { deliveryDate = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )

            TimePickerField(
                label = "Delivery time",
                value = deliveryTime,
                onTimeSelected = { deliveryTime = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )

            TimeZoneDropdownField(
                label = "Delivery time zone",
                selectedTimeZone = deliveryTimeZone,
                onTimeZoneSelected = { deliveryTimeZone = it },
                modifier = Modifier.padding(top = 12.dp),
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                }
            )
        }

        TripSection(
            title = "Cargo and Payment",
            expanded = cargoAndPaymentExpanede,
            onClick = { cargoAndPaymentExpanede = !cargoAndPaymentExpanede }
        ) {
            OutlinedTextField(
                value = cargoDescription,
                onValueChange = { cargoDescription = it },
                label = { Text("Cargo description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                minLines = 3,
                maxLines = 5,
                singleLine = false
            )

            OutlinedTextField(
                value = cargoWeight,
                onValueChange = { cargoWeight = it },
                label = { Text("Cargo weight") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = cargoVolume,
                onValueChange = { cargoVolume = it },
                label = { Text("Cargo volume") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = cargoTypeExpanded,
                onExpandedChange = { cargoTypeExpanded = !cargoTypeExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                OutlinedTextField(
                    value = cargoType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cargo type") },
                    supportingText = {
                        Text(
                            text = "*",
                            color = Color.Red
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(cargoTypeExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = cargoTypeExpanded,
                    onDismissRequest = { cargoTypeExpanded = false }
                ) {
                    CargoType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                cargoType = type
                                cargoTypeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                supportingText = {
                    Text(
                        text = "*",
                        color = Color.Red
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = !currencyExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                OutlinedTextField(
                    value = currency.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    supportingText = {
                        Text(
                            text = "*",
                            color = Color.Red
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(currencyExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    Currency.entries.forEach { selectedCurrency ->
                        DropdownMenuItem(
                            text = { Text(selectedCurrency.name) },
                            onClick = {
                                currency = selectedCurrency
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = additionalInfo,
                onValueChange = { additionalInfo = it },
                label = { Text("Additional info") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                minLines = 3,
                maxLines = 5,
                singleLine = false
            )
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier
                .padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessage() }
        )

        LoadingButton(
            text = "Create trip",
            isLoading = uiState.isLoading,
            onClick = {
                val pickupDateTime = if (pickupDate.isNotBlank() && pickupTime.isNotBlank()) {
                    "${pickupDate}T${pickupTime}:00"
                } else {
                    ""
                }

                val deliveryDateTime = if (deliveryDate.isNotBlank() && deliveryTime.isNotBlank()) {
                    "${deliveryDate}T${deliveryTime}:00"
                } else {
                    ""
                }

                viewModel.addNewTrip(
                    pickupCountry = pickupCountry,
                    pickupAdministrativeArea = pickupAdministrativeArea,
                    pickupCity = pickupCity,
                    pickupStreetName = pickupStreetName,
                    pickupStreetNumber = pickupStreetNumber,
                    pickupPostalCode = pickupPostalCode,
                    pickupAdditionalDetails = pickupAdditionalDetails,

                    deliveryCountry = deliveryCountry,
                    deliveryAdministrativeArea = deliveryAdministrativeArea,
                    deliveryCity = deliveryCity,
                    deliveryStreetName = deliveryStreetName,
                    deliveryStreetNumber = deliveryStreetNumber,
                    deliveryPostalCode = deliveryPostalCode,
                    deliveryAdditionalDetails = deliveryAdditionalDetails,

                    pickupDateTime = pickupDateTime,
                    pickupTimeZone = pickupTimeZone,
                    deliveryDateTime = deliveryDateTime,
                    deliveryTimeZone = deliveryTimeZone,

                    cargoDescription = cargoDescription,
                    cargoWeight = cargoWeight,
                    cargoVolume = cargoVolume,
                    cargoType = cargoType,
                    price = price,
                    currency = currency,
                    additionalInfo = additionalInfo
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            enabled = !uiState.isLoading
        )

        OutlinedButton(
            onClick = {
                viewModel.clearMessage()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Back")
        }
    }
}