package com.example.officeapp.screens.addVehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.vehicle.VehicleCapacityRequirement
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.models.vehicle.capacityRequirement
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.screens.reusableComponents.OfficeFormDropdownField
import com.example.officeapp.screens.reusableComponents.OfficeFormTextField
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.VehicleViewModel

@Composable
fun AddNewVehicleScreen(
    viewModel: VehicleViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var licencePlate by remember { mutableStateOf("") }
    var vin by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var manufactureYear by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf<VehicleType?>(null) }
    var vehicleStatus by remember { mutableStateOf<VehicleStatus?>(null) }
    var maxWeight by remember { mutableStateOf("") }
    var maxVolume by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    val selectedType = vehicleType ?: VehicleType.TRACTOR_UNIT
    val capacityRequirement = selectedType.capacityRequirement()

    val maxWeightEnabled = capacityRequirement == VehicleCapacityRequirement.WEIGHT_AND_VOLUME
    val maxVolumeEnabled = capacityRequirement == VehicleCapacityRequirement.WEIGHT_AND_VOLUME ||
                           capacityRequirement == VehicleCapacityRequirement.ONLY_VOLUME

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val fieldContainerColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) PrimaryBlueDark.copy(alpha = 0.85f) else PrimaryBlueLight.copy(alpha = 0.75f)
    val subtleBorderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessage()
            onBack()
        }
    }

    LaunchedEffect(vehicleType) {
        if (!maxWeightEnabled) {
            maxWeight = ""
        }

        if (!maxVolumeEnabled) {
            maxVolume = ""
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.add_new_vehicle_title),
                subtitle = stringResource(R.string.add_new_vehicle_subtitle),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = subtleBorderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessage()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OfficeFormTextField(
                    value = licencePlate,
                    onValueChange = { licencePlate = it.uppercase() },
                    label = stringResource(R.string.label_licence_plate),
                    placeholder = stringResource(R.string.description_licence_plate),
                    icon = Icons.Outlined.Badge,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = vin,
                    onValueChange = { vin = it.uppercase() },
                    label = stringResource(R.string.label_vin),
                    icon = Icons.Outlined.Numbers,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = stringResource(R.string.label_brand),
                    icon = Icons.Outlined.Security,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = stringResource(R.string.label_model),
                    icon = Icons.Outlined.DirectionsCar,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = manufactureYear,
                    onValueChange = { manufactureYear = it.filter(Char::isDigit).take(4) },
                    label = stringResource(R.string.label_manufacture_year),
                    icon = Icons.Outlined.CalendarMonth,
                    keyboardType = KeyboardType.Number,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormDropdownField(
                    selectedValue = vehicleType,
                    values = VehicleType.entries,
                    label = stringResource(R.string.label_vehicle_type),
                    icon = Icons.Outlined.LocalShipping,
                    itemText = { it.name },
                    onValueSelected = { vehicleType = it },
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormDropdownField(
                    selectedValue = vehicleStatus,
                    values = VehicleStatus.entries,
                    label = stringResource(R.string.label_vehicle_status),
                    icon = Icons.Outlined.Autorenew,
                    itemText = { it.name },
                    onValueSelected = { vehicleStatus = it },
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = maxWeight,
                    onValueChange = { maxWeight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = stringResource(R.string.label_maximum_weight) + " " + stringResource(R.string.label_kilograms),
                    icon = Icons.Outlined.Scale,
                    enabled = maxWeightEnabled,
                    keyboardType = KeyboardType.Number,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = maxVolume,
                    onValueChange = { maxVolume = it.filter { char -> char.isDigit() || char == '.' } },
                    label = stringResource(R.string.label_maximum_volume) + " " + stringResource(R.string.label_cubic_meters),
                    icon = Icons.Outlined.Inventory2,
                    enabled = maxVolumeEnabled,
                    keyboardType = KeyboardType.Number,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )

                OfficeFormTextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = stringResource(R.string.label_additional_info),
                    placeholder = stringResource(R.string.description_additional_info),
                    icon = Icons.Outlined.Description,
                    singleLine = false,
                    minLines = 1,
                    maxLines = 4,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = borderColor
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            LoadingButton(
                text = stringResource(R.string.button_create_vehicle),
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.addNewVehicle(
                        licencePlate = licencePlate,
                        vin = vin,
                        brand = brand,
                        model = model,
                        manufactureYear = manufactureYear,
                        vehicleType = vehicleType ?: VehicleType.TRACTOR_UNIT,
                        maxWeight = maxWeight,
                        maxVolume = maxVolume,
                        vehicleStatus = vehicleStatus ?: VehicleStatus.AVAILABLE,
                        additionalInfo = additionalInfo
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !uiState.isLoading
            )
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isDarkTheme = isDarkTheme,
            onMessageShown = {
                viewModel.clearMessage()
            }
        )
    }
}