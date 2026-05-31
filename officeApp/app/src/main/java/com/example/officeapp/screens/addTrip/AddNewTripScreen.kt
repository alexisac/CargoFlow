package com.example.officeapp.screens.addTrip

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Euro
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Signpost
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.Currency
import com.example.officeapp.screens.reusableComponents.DatePickerField
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.screens.reusableComponents.OfficeFormDropdownField
import com.example.officeapp.screens.reusableComponents.OfficeFormTextField
import com.example.officeapp.screens.reusableComponents.TimePickerField
import com.example.officeapp.screens.reusableComponents.TimeZoneDropdownField
import com.example.officeapp.ui.theme.AccentBlue
import com.example.officeapp.ui.theme.AccentCyan
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.AccentViolet
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.TripViewModel
import java.time.ZoneId

@Composable
fun AddNewTripScreen(
    viewModel: TripViewModel,
    isDarkTheme: Boolean,
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
    var cargoAndPaymentExpanded by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val sectionContainerColor = if (isDarkTheme) DarkCard else LightSurface
    val fieldContainerColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val subtleBorderColor = if (isDarkTheme) BorderDark else BorderLight

    val pickupAddressSectionColor = AccentBlue
    val deliveryAddressSectionColor = AccentCyan
    val timeSectionColor = AccentViolet
    val cargoAndPaymentSectionColor = AccentPink

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

    fun submitTrip() {
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
                .padding(horizontal = 20.dp)
                .padding(top = 36.dp, bottom = 28.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.add_new_trip_title),
                subtitle = stringResource(R.string.add_new_trip_subtitle),
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

            TripSection(
                title = stringResource(R.string.section_pickup_address),
                subtitle = stringResource(R.string.trip_pickup_address_subtitle),
                icon = Icons.Outlined.LocationOn,
                accentColor = pickupAddressSectionColor,
                containerColor = sectionContainerColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                expanded = pickupAddressExpanded,
                onClick = {
                    pickupAddressExpanded = !pickupAddressExpanded
                }
            ) {
                AddressFields(
                    country = pickupCountry,
                    onCountryChange = { pickupCountry = it },
                    administrativeArea = pickupAdministrativeArea,
                    onAdministrativeAreaChange = { pickupAdministrativeArea = it },
                    city = pickupCity,
                    onCityChange = { pickupCity = it },
                    streetName = pickupStreetName,
                    onStreetNameChange = { pickupStreetName = it },
                    streetNumber = pickupStreetNumber,
                    onStreetNumberChange = { pickupStreetNumber = it },
                    postalCode = pickupPostalCode,
                    onPostalCodeChange = { pickupPostalCode = it },
                    additionalDetails = pickupAdditionalDetails,
                    onAdditionalDetailsChange = { pickupAdditionalDetails = it },
                    accentColor = pickupAddressSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    fieldContainerColor = fieldContainerColor
                )
            }

            TripSection(
                title = stringResource(R.string.section_delivery_address),
                subtitle = stringResource(R.string.trip_delivery_address_subtitle),
                icon = Icons.Outlined.Map,
                accentColor = deliveryAddressSectionColor,
                containerColor = sectionContainerColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                expanded = deliveryAddressExpanded,
                onClick = {
                    deliveryAddressExpanded = !deliveryAddressExpanded
                }
            ) {
                AddressFields(
                    country = deliveryCountry,
                    onCountryChange = { deliveryCountry = it },
                    administrativeArea = deliveryAdministrativeArea,
                    onAdministrativeAreaChange = { deliveryAdministrativeArea = it },
                    city = deliveryCity,
                    onCityChange = { deliveryCity = it },
                    streetName = deliveryStreetName,
                    onStreetNameChange = { deliveryStreetName = it },
                    streetNumber = deliveryStreetNumber,
                    onStreetNumberChange = { deliveryStreetNumber = it },
                    postalCode = deliveryPostalCode,
                    onPostalCodeChange = { deliveryPostalCode = it },
                    additionalDetails = deliveryAdditionalDetails,
                    onAdditionalDetailsChange = { deliveryAdditionalDetails = it },
                    accentColor = deliveryAddressSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    fieldContainerColor = fieldContainerColor
                )
            }

            TripSection(
                title = stringResource(R.string.section_time),
                subtitle = stringResource(R.string.trip_time_subtitle),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = timeSectionColor,
                containerColor = sectionContainerColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                expanded = timeExpanded,
                onClick = {
                    timeExpanded = !timeExpanded
                }
            ) {
                DatePickerField(
                    value = pickupDate,
                    onDateSelected = { pickupDate = it },
                    label = stringResource(R.string.label_pickup_date),
                    placeholder = "YYYY-MM-DD",
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )

                TimePickerField(
                    value = pickupTime,
                    onTimeSelected = { pickupTime = it },
                    label = stringResource(R.string.label_pickup_time),
                    placeholder = "HH:mm",
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )

                TimeZoneDropdownField(
                    selectedTimeZone = pickupTimeZone,
                    onTimeZoneSelected = { pickupTimeZone = it },
                    label = stringResource(R.string.label_pickup_time_zone),
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )

                DatePickerField(
                    value = deliveryDate,
                    onDateSelected = { deliveryDate = it },
                    label = stringResource(R.string.label_delivery_date),
                    placeholder = "YYYY-MM-DD",
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )

                TimePickerField(
                    value = deliveryTime,
                    onTimeSelected = { deliveryTime = it },
                    label = stringResource(R.string.label_delivery_time),
                    placeholder = "HH:mm",
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )

                TimeZoneDropdownField(
                    selectedTimeZone = deliveryTimeZone,
                    onTimeZoneSelected = { deliveryTimeZone = it },
                    label = stringResource(R.string.label_delivery_time_zone),
                    required = true,
                    iconColor = timeSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = timeSectionColor
                )
            }

            TripSection(
                title = stringResource(R.string.section_cargo_and_payment),
                subtitle = stringResource(R.string.trip_cargo_payment_subtitle),
                icon = Icons.Outlined.Inventory2,
                accentColor = cargoAndPaymentSectionColor,
                containerColor = sectionContainerColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                expanded = cargoAndPaymentExpanded,
                onClick = {
                    cargoAndPaymentExpanded = !cargoAndPaymentExpanded
                }
            ) {
                OfficeFormTextField(
                    value = cargoDescription,
                    onValueChange = { cargoDescription = it },
                    label = stringResource(R.string.label_cargo_description),
                    icon = Icons.Outlined.Description,
                    required = true,
                    singleLine = false,
                    minLines = 1,
                    maxLines = 4,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormTextField(
                    value = cargoWeight,
                    onValueChange = { cargoWeight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = stringResource(R.string.label_cargo_weight) + " " + stringResource(R.string.label_kilograms),
                    placeholder = "e.g. 1200",
                    icon = Icons.Outlined.Scale,
                    keyboardType = KeyboardType.Number,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormTextField(
                    value = cargoVolume,
                    onValueChange = { cargoVolume = it.filter { char -> char.isDigit() || char == '.' } },
                    label = stringResource(R.string.label_cargo_volume) + " " + stringResource(R.string.label_cubic_meters),
                    placeholder = "e.g. 12.5",
                    icon = Icons.Outlined.Warehouse,
                    keyboardType = KeyboardType.Number,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormDropdownField(
                    selectedValue = cargoType,
                    values = CargoType.entries,
                    label = stringResource(R.string.label_cargo_type),
                    icon = Icons.Outlined.Inventory2,
                    itemText = { it.name },
                    onValueSelected = { cargoType = it },
                    required = true,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() || char == '.' } },
                    label = stringResource(R.string.label_price),
                    placeholder = "e.g. 3500.00",
                    icon = Icons.Outlined.Payments,
                    required = true,
                    keyboardType = KeyboardType.Number,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormDropdownField(
                    selectedValue = currency,
                    values = Currency.entries,
                    label = stringResource(R.string.label_currency),
                    icon = Icons.Outlined.Euro,
                    itemText = { it.name },
                    onValueSelected = { currency = it },
                    required = true,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )

                OfficeFormTextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = stringResource(R.string.label_additional_details),
                    icon = Icons.Outlined.Description,
                    singleLine = false,
                    minLines = 1,
                    maxLines = 4,
                    iconColor = cargoAndPaymentSectionColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = cargoAndPaymentSectionColor
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            LoadingButton(
                text = stringResource(R.string.button_create_trip),
                isLoading = uiState.isLoading,
                onClick = {
                    submitTrip()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
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

@Composable
private fun AddressFields(
    country: String,
    onCountryChange: (String) -> Unit,
    administrativeArea: String,
    onAdministrativeAreaChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    streetName: String,
    onStreetNameChange: (String) -> Unit,
    streetNumber: String,
    onStreetNumberChange: (String) -> Unit,
    postalCode: String,
    onPostalCodeChange: (String) -> Unit,
    additionalDetails: String,
    onAdditionalDetailsChange: (String) -> Unit,
    accentColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    fieldContainerColor: Color
) {
    OfficeFormTextField(
        value = country,
        onValueChange = onCountryChange,
        label = stringResource(R.string.label_country),
        placeholder = stringResource(R.string.label_select_country),
        icon = Icons.Outlined.Place,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = administrativeArea,
        onValueChange = onAdministrativeAreaChange,
        label = stringResource(R.string.label_administrative_area),
        placeholder = stringResource(R.string.label_select_administrative_area),
        icon = Icons.Outlined.Map,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = city,
        onValueChange = onCityChange,
        label = stringResource(R.string.label_city),
        placeholder = stringResource(R.string.label_select_city),
        icon = Icons.Outlined.LocationOn,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = streetName,
        onValueChange = onStreetNameChange,
        label = stringResource(R.string.label_street_name),
        placeholder = stringResource(R.string.label_enter_street_name),
        icon = Icons.Outlined.Signpost,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = streetNumber,
        onValueChange = onStreetNumberChange,
        label = stringResource(R.string.label_street_number),
        placeholder = stringResource(R.string.label_enter_street_number),
        icon = Icons.Outlined.Signpost,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = postalCode,
        onValueChange = onPostalCodeChange,
        label = stringResource(R.string.label_postal_code),
        placeholder = stringResource(R.string.label_enter_postal_code),
        icon = Icons.Outlined.MyLocation,
        required = true,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )

    OfficeFormTextField(
        value = additionalDetails,
        onValueChange = onAdditionalDetailsChange,
        label = stringResource(R.string.label_additional_details),
        icon = Icons.Outlined.Description,
        singleLine = false,
        minLines = 1,
        maxLines = 4,
        iconColor = accentColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = fieldContainerColor,
        borderColor = accentColor
    )
}