package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.officeapp.models.trip.TripStatus

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
                onRemove = { onRemoveStatus(status) }
            )
        }

        pickupCountries.forEach { country ->
            FilterChipItem(
                text = "Pickup country: $country",
                onRemove = { onRemovePickupCountry(country) }
            )
        }

        pickupCities.forEach { city ->
            FilterChipItem(
                text = "Pickup city: $city",
                onRemove = { onRemovePickupCity(city) }
            )
        }

        deliveryCountries.forEach { country ->
            FilterChipItem(
                text = "Delivery country: $country",
                onRemove = { onRemoveDeliveryCountry(country) }
            )
        }

        deliveryCities.forEach { city ->
            FilterChipItem(
                text = "Delivery city: $city",
                onRemove = { onRemoveDeliveryCity(city) }
            )
        }

        if (pickupDateTimeFrom.isNotBlank()) {
            FilterChipItem(
                text = "Pickup from: $pickupDateTimeFrom",
                onRemove = onClearPickupDateTimeFrom
            )
        }

        if (pickupDateTimeTo.isNotBlank()) {
            FilterChipItem(
                text = "Pickup to: $pickupDateTimeTo",
                onRemove = onClearPickupDateTimeTo
            )
        }

        if (deliveryDateTimeFrom.isNotBlank()) {
            FilterChipItem(
                text = "Delivery from: $deliveryDateTimeFrom",
                onRemove = onClearDeliveryDateTimeFrom
            )
        }

        if (deliveryDateTimeTo.isNotBlank()) {
            FilterChipItem(
                text = "Delivery to: $deliveryDateTimeTo",
                onRemove = onClearDeliveryDateTimeTo
            )
        }
    }
}