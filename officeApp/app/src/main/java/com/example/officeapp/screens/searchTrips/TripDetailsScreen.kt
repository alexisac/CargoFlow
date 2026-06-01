package com.example.officeapp.screens.searchTrips

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
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.screens.reusableComponents.formatDateTime
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.SuccessGreen
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.ui.theme.WarningOrange
import com.example.officeapp.viewModels.TripViewModel

@Composable
fun TripDetailsScreen(
    viewModel: TripViewModel,
    tripId: Long,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight
    val pickupColor = primaryColor
    val deliveryColor = SuccessGreen
    val cargoColor = Color(0xFF7C4DFF)
    val priceColor = WarningOrange

    LaunchedEffect(tripId) {
        viewModel.getTrip(tripId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessage()
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
                .padding(horizontal = 14.dp)
                .padding(top = 48.dp, bottom = 32.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.trip_details_title),
                subtitle = null,
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = borderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessage()
                    onBack()
                },
                onRefresh = {
                    viewModel.clearMessage()
                    viewModel.getTrip(tripId)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.currentTrip != null -> {
                    val trip = uiState.currentTrip!!

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TripDetailSection(
                            title = stringResource(R.string.label_general),
                            icon = Icons.Outlined.ReceiptLong,
                            accentColor = primaryColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            DetailRow(
                                label = stringResource(R.string.label_id),
                                value = trip.id.toString(),
                                icon = Icons.Outlined.Numbers,
                                iconColor = primaryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_status),
                                value = trip.tripStatus.name,
                                icon = Icons.Outlined.Info,
                                iconColor = tripStatusColor(trip.tripStatus),
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                valueColor = tripStatusColor(trip.tripStatus)
                            )

                            DetailRow(
                                label = stringResource(R.string.label_created_by),
                                value = trip.createdBy,
                                icon = Icons.Outlined.Person,
                                iconColor = primaryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        TripDetailSection(
                            title = stringResource(R.string.label_pickup),
                            icon = Icons.Outlined.LocationOn,
                            accentColor = pickupColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            AddressRows(
                                country = trip.pickupAddress.country,
                                administrativeArea = trip.pickupAddress.administrativeArea,
                                city = trip.pickupAddress.city,
                                streetName = trip.pickupAddress.streetName,
                                streetNumber = trip.pickupAddress.streetNumber,
                                postalCode = trip.pickupAddress.postalCode,
                                additionalDetails = trip.pickupAddress.additionalDetails,
                                iconColor = pickupColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            SectionDivider(isDarkTheme)

                            DetailRow(
                                label = stringResource(R.string.label_pickup_date_time_multiline),
                                value = formatDateTime(trip.pickupDateTime),
                                icon = Icons.Outlined.CalendarMonth,
                                iconColor = pickupColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_pickup_time_zone_multiline),
                                value = trip.pickupTimeZone,
                                icon = Icons.Outlined.AccessTime,
                                iconColor = pickupColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        TripDetailSection(
                            title = stringResource(R.string.label_delivery),
                            icon = Icons.Outlined.LocationOn,
                            accentColor = deliveryColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            AddressRows(
                                country = trip.deliveryAddress.country,
                                administrativeArea = trip.deliveryAddress.administrativeArea,
                                city = trip.deliveryAddress.city,
                                streetName = trip.deliveryAddress.streetName,
                                streetNumber = trip.deliveryAddress.streetNumber,
                                postalCode = trip.deliveryAddress.postalCode,
                                additionalDetails = trip.deliveryAddress.additionalDetails,
                                iconColor = deliveryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            SectionDivider(isDarkTheme)

                            DetailRow(
                                label = stringResource(R.string.label_delivery_date_time_multiline),
                                value = formatDateTime(trip.deliveryDateTime),
                                icon = Icons.Outlined.CalendarMonth,
                                iconColor = deliveryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_delivery_time_zone_multiline),
                                value = trip.deliveryTimeZone,
                                icon = Icons.Outlined.AccessTime,
                                iconColor = deliveryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        TripDetailSection(
                            title = stringResource(R.string.label_cargo),
                            icon = Icons.Outlined.Inventory2,
                            accentColor = cargoColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            DetailRow(
                                label = stringResource(R.string.label_cargo_type),
                                value = trip.cargoType.name,
                                icon = Icons.Outlined.Inventory2,
                                iconColor = cargoColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_cargo_description_multiline),
                                value = trip.cargoDescription ?: "-",
                                icon = Icons.Outlined.Description,
                                iconColor = cargoColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_cargo_weight),
                                value = trip.cargoWeight?.toString() ?: "-",
                                icon = Icons.Outlined.Straighten,
                                iconColor = cargoColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_cargo_volume),
                                value = trip.cargoVolume?.toString() ?: "-",
                                icon = Icons.Outlined.Inventory2,
                                iconColor = cargoColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        TripDetailSection(
                            title = stringResource(R.string.label_price),
                            icon = Icons.Outlined.Payment,
                            accentColor = priceColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            DetailRow(
                                label = stringResource(R.string.label_price),
                                value = trip.price.toString(),
                                icon = Icons.Outlined.Payment,
                                iconColor = priceColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )

                            DetailRow(
                                label = stringResource(R.string.label_currency),
                                value = trip.currency.name,
                                icon = Icons.Outlined.Info,
                                iconColor = priceColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        TripDetailSection(
                            title = stringResource(R.string.label_additional_info),
                            icon = Icons.Outlined.Info,
                            accentColor = primaryColor,
                            containerColor = cardColor,
                            isDarkTheme = isDarkTheme
                        ) {
                            DetailRow(
                                label = stringResource(R.string.label_additional_info),
                                value = trip.additionalInfo ?: "-",
                                icon = Icons.Outlined.Description,
                                iconColor = primaryColor,
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }
                    }
                }
            }
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