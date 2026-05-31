package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight

@Composable
fun TripFiltersSheet(
    isDarkTheme: Boolean,
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

    onResetFilters: () -> Unit,
    onApplyFilters: () -> Unit
) {
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val fieldContainerColor = if (isDarkTheme) DarkCard else LightSurface
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    var pickupCountryInput by remember { mutableStateOf("") }
    var pickupCityInput by remember { mutableStateOf("") }
    var deliveryCountryInput by remember { mutableStateOf("") }
    var deliveryCityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 720.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.button_filters),
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = onResetFilters,
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor
                )
            ) {
                Text(stringResource(R.string.button_reset))

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = null
                )
            }
        }

        Text(
            text = stringResource(R.string.label_trip_status),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TripStatus.entries.forEach { status ->
                val selected = selectedStatusList.contains(status)
                val color = tripStatusColor(status)

                FilterChip(
                    selected = selected,
                    onClick = {
                        if (selected) {
                            selectedStatusList.remove(status)
                        } else {
                            selectedStatusList.add(status)
                        }
                    },
                    label = {
                        Text(status.name)
                    },
                    trailingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = color
                            )
                        }
                    } else {
                        null
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        selectedContainerColor = color.copy(alpha = if (isDarkTheme) 0.16f else 0.10f),
                        labelColor = color,
                        selectedLabelColor = color
                    ),
                    border = BorderStroke(1.dp, color),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(9.dp)
                )
            }
        }

        Text(
            text = stringResource(R.string.label_locations),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        AddTextFilterField(
            label = stringResource(R.string.label_pickup_country),
            value = pickupCountryInput,
            onValueChange = { pickupCountryInput = it },
            onClear = { pickupCountryInput = "" },
            textColor = textColor,
            secondaryTextColor = primaryColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        AddTextFilterField(
            label = stringResource(R.string.label_pickup_city),
            value = pickupCityInput,
            onValueChange = { pickupCityInput = it },
            onClear = { pickupCityInput = "" },
            textColor = textColor,
            secondaryTextColor = primaryColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        AddTextFilterField(
            label = stringResource(R.string.label_delivery_country),
            value = deliveryCountryInput,
            onValueChange = { deliveryCountryInput = it },
            onClear = { deliveryCountryInput = "" },
            textColor = textColor,
            secondaryTextColor = primaryColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        AddTextFilterField(
            label = stringResource(R.string.label_delivery_city),
            value = deliveryCityInput,
            onValueChange = { deliveryCityInput = it },
            onClear = { deliveryCityInput = "" },
            textColor = textColor,
            secondaryTextColor = primaryColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        Text(
            text = stringResource(R.string.section_pickup_interval),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        DateTimeIntervalRow(
            fromDateLabel = stringResource(R.string.label_pickup_from_date),
            fromTimeLabel = stringResource(R.string.label_pickup_from_time),
            toDateLabel = stringResource(R.string.label_pickup_to_date),
            toTimeLabel = stringResource(R.string.label_pickup_to_time),
            fromDateTime = pickupDateTimeFrom,
            toDateTime = pickupDateTimeTo,
            onFromChange = onPickupDateTimeFromChange,
            onToChange = onPickupDateTimeToChange,
            iconColor = primaryColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        Text(
            text = stringResource(R.string.section_delivery_interval),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        DateTimeIntervalRow(
            fromDateLabel = stringResource(R.string.label_delivery_from_date),
            fromTimeLabel = stringResource(R.string.label_delivery_from_time),
            toDateLabel = stringResource(R.string.label_delivery_to_date),
            toTimeLabel = stringResource(R.string.label_delivery_to_time),
            fromDateTime = deliveryDateTimeFrom,
            toDateTime = deliveryDateTimeTo,
            onFromChange = onDeliveryDateTimeFromChange,
            onToChange = onDeliveryDateTimeToChange,
            iconColor = primaryColor,
            textColor = textColor,
            secondaryTextColor = secondaryTextColor,
            containerColor = fieldContainerColor,
            borderColor = primaryColor
        )

        LoadingButton(
            text = stringResource(R.string.button_apply_filters),
            isLoading = false,
            onClick = {
                pickupCountryInput.trim()
                    .takeIf { it.isNotBlank() && !pickupCountries.contains(it) }
                    ?.let { pickupCountries.add(it) }

                pickupCityInput.trim()
                    .takeIf { it.isNotBlank() && !pickupCities.contains(it) }
                    ?.let { pickupCities.add(it) }

                deliveryCountryInput.trim()
                    .takeIf { it.isNotBlank() && !deliveryCountries.contains(it) }
                    ?.let { deliveryCountries.add(it) }

                deliveryCityInput.trim()
                    .takeIf { it.isNotBlank() && !deliveryCities.contains(it) }
                    ?.let { deliveryCities.add(it) }

                onApplyFilters()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        )
    }
}
