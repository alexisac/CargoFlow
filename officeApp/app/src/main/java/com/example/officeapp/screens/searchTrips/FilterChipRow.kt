package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.ui.theme.AccentBlue
import com.example.officeapp.ui.theme.AccentCyan
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.AccentViolet

@Composable
fun FilterChipRow(
    selectedStatusList: List<TripStatus>,
    pickupCountries: List<String>,
    pickupCities: List<String>,
    deliveryCountries: List<String>,
    deliveryCities: List<String>,
    pickupDateTimeFrom: String,
    pickupDateTimeTo: String,
    deliveryDateTimeFrom: String,
    deliveryDateTimeTo: String,
    textColor: Color,
    containerColor: Color,
    borderColor: Color,
    onRemoveStatus: (TripStatus) -> Unit,
    onRemovePickupCountry: (String) -> Unit,
    onRemovePickupCity: (String) -> Unit,
    onRemoveDeliveryCountry: (String) -> Unit,
    onRemoveDeliveryCity: (String) -> Unit,
    onClearPickupDateTimeFrom: () -> Unit,
    onClearPickupDateTimeTo: () -> Unit,
    onClearDeliveryDateTimeFrom: () -> Unit,
    onClearDeliveryDateTimeTo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        selectedStatusList.forEach { status ->
            FilterChipItem(
                text = status.name,
                accentColor = tripStatusColor(status),
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = { onRemoveStatus(status) }
            )
        }

        pickupCountries.forEach { country ->
            FilterChipItem(
                text = stringResource(R.string.label_pickup_country) + ": " + country,
                accentColor = AccentBlue,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = { onRemovePickupCountry(country) }
            )
        }

        pickupCities.forEach { city ->
            FilterChipItem(
                text = stringResource(R.string.label_pickup_city) + ": " + city,
                accentColor = AccentBlue,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = { onRemovePickupCity(city) }
            )
        }

        deliveryCountries.forEach { country ->
            FilterChipItem(
                text = stringResource(R.string.label_delivery_country) + ": " + country,
                accentColor = AccentCyan,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = { onRemoveDeliveryCountry(country) }
            )
        }

        deliveryCities.forEach { city ->
            FilterChipItem(
                text = stringResource(R.string.label_delivery_city) + ": " + city,
                accentColor = AccentCyan,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = { onRemoveDeliveryCity(city) }
            )
        }

        if (pickupDateTimeFrom.isNotBlank()) {
            FilterChipItem(
                text = stringResource(R.string.label_pickup_from) + ": " + pickupDateTimeFrom,
                accentColor = AccentViolet,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = onClearPickupDateTimeFrom
            )
        }

        if (pickupDateTimeTo.isNotBlank()) {
            FilterChipItem(
                text = stringResource(R.string.label_pickup_to) + ": " + pickupDateTimeTo,
                accentColor = AccentViolet,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = onClearPickupDateTimeTo
            )
        }

        if (deliveryDateTimeFrom.isNotBlank()) {
            FilterChipItem(
                text = stringResource(R.string.label_delivery_from) + ": " + deliveryDateTimeFrom,
                accentColor = AccentPink,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = onClearDeliveryDateTimeFrom
            )
        }

        if (deliveryDateTimeTo.isNotBlank()) {
            FilterChipItem(
                text = stringResource(R.string.label_delivery_to) + ": " + deliveryDateTimeTo,
                accentColor = AccentPink,
                textColor = textColor,
                containerColor = containerColor,
                borderColor = borderColor,
                onRemove = onClearDeliveryDateTimeTo
            )
        }
    }
}