package com.example.officeapp.screens.currentDriverTrip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.models.trip.CompletedTrip
import com.example.officeapp.screens.reusableComponents.formatDate
import com.example.officeapp.screens.reusableComponents.formatHourMinute
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
fun CompletedDriverTripCard(
    trip: CompletedTrip,
    isDarkTheme: Boolean
) {
    val cardColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDarkTheme) 0.dp else 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.trip_completed_title) + trip.tripId,
                color = textColor,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CompletedTripLocationBlock(
                    title = stringResource(R.string.label_pickup),
                    city = trip.pickupAddress.city,
                    country = trip.pickupAddress.country,
                    dateTime = trip.pickupDateTime,
                    timeZone = trip.pickupTimeZone,
                    accentColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )

                CompletedTripLocationBlock(
                    title = stringResource(R.string.label_delivery),
                    city = trip.deliveryAddress.city,
                    country = trip.deliveryAddress.country,
                    dateTime = trip.deliveryDateTime,
                    timeZone = trip.deliveryTimeZone,
                    accentColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 18.dp),
                color = borderColor
            )

            CompletedTripInfoRow(
                icon = Icons.Outlined.Inventory2,
                label = stringResource(R.string.label_type),
                value = trip.cargoType.name,
                iconColor = primaryColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor
            )

            trip.cargoDescription?.let {
                CompletedTripInfoRow(
                    icon = Icons.Outlined.LocalShipping,
                    label = stringResource(R.string.label_description),
                    value = it,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }

            trip.cargoWeight?.let {
                CompletedTripInfoRow(
                    icon = Icons.Outlined.Inventory2,
                    label = stringResource(R.string.label_weight),
                    value = "$it ${stringResource(R.string.label_kilograms)}",
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }

            trip.cargoVolume?.let {
                CompletedTripInfoRow(
                    icon = Icons.Outlined.Inventory2,
                    label = stringResource(R.string.label_volume),
                    value = "$it ${stringResource(R.string.label_cubic_meters)}",
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }

            trip.additionalInfo?.let {
                CompletedTripInfoRow(
                    icon = Icons.Outlined.Payments,
                    label = stringResource(R.string.label_additional_info),
                    value = it,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor
                )
            }
        }
    }
}

@Composable
private fun CompletedTripLocationBlock(
    title: String,
    city: String,
    country: String,
    dateTime: String,
    timeZone: String,
    accentColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 5.dp))

            Text(
                text = title,
                color = secondaryTextColor,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$city, $country",
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = formatDate(dateTime),
            color = secondaryTextColor,
            fontSize = 14.sp
        )

        Text(
            text = formatHourMinute(dateTime),
            color = secondaryTextColor,
            fontSize = 14.sp
        )

        Text(
            text = timeZone,
            color = secondaryTextColor,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun CompletedTripInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

        Text(
            text = label,
            color = secondaryTextColor,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.3f)
        )
    }
}