package com.example.officeapp.features.addVehicle.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.officeapp.features.addVehicle.VehicleViewModel
import com.example.officeapp.features.reusableComponents.FormMessages
import com.example.officeapp.features.reusableComponents.LoadingButton
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewVehicleScreen (
    viewModel: VehicleViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessage()
            onBack()
        }
    }

    var licencePlate by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var manufactureYear by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf(VehicleType.TRUCK) }
    var maxWeight by remember { mutableStateOf("") }
    var maxVolume by remember { mutableStateOf("") }
    var vehicleStatus by remember { mutableStateOf(VehicleStatus.AVAILABLE) }
    var additionalInfo by remember { mutableStateOf("") }

    var expendedVehicleType by remember { mutableStateOf(false) }
    var expendedVehicleStatus by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text (
            text = "Add new vehicle",
            modifier = Modifier
                .padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = licencePlate,
            onValueChange = { licencePlate = it.uppercase() },
            label = { Text("Licence plate") },
            placeholder = { Text("B-111-AAA") },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = vin,
            onValueChange = { vin = it },
            label = { Text("VIN") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = manufactureYear,
            onValueChange = { manufactureYear = it },
            label = { Text("Manufacture year") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        ExposedDropdownMenuBox(
            expanded = expendedVehicleType,
            onExpandedChange = { expendedVehicleType = !expendedVehicleType },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            OutlinedTextField(
                value = vehicleType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Vehicle type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expendedVehicleType) },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expendedVehicleType,
                onDismissRequest = { expendedVehicleType = false }
            ) {
                VehicleType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            vehicleType = type
                            expendedVehicleType = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = maxWeight,
            onValueChange = { maxWeight = it },
            label = { Text("Maximum weight") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = maxVolume,
            onValueChange = { maxVolume = it },
            label = { Text("Maximum volume") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        ExposedDropdownMenuBox(
            expanded = expendedVehicleStatus,
            onExpandedChange = { expendedVehicleStatus = !expendedVehicleStatus },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            OutlinedTextField(
                value = vehicleStatus.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Vehicle status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expendedVehicleStatus) },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expendedVehicleStatus,
                onDismissRequest = { expendedVehicleStatus = false }
            ) {
                VehicleStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.name) },
                        onClick = {
                            vehicleStatus = status
                            expendedVehicleStatus = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Additional info") },
            placeholder = { Text("Optional details about the vehicle") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            minLines = 3,
            maxLines = 5,
            singleLine = false
        )

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier
                .padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessage() }
        )

        LoadingButton(
            text = "Create vehicle",
            isLoading = uiState.isLoading,
            onClick = {
                viewModel.addNewVehicle(
                    licencePlate = licencePlate,
                    vin = vin,
                    brand = brand,
                    model = model,
                    manufactureYear = manufactureYear,
                    vehicleType = vehicleType,
                    maxWeight = maxWeight,
                    maxVolume = maxVolume,
                    vehicleStatus = vehicleStatus,
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