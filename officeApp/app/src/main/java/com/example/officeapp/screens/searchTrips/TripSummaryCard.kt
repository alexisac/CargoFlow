package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.models.trip.TripSummary

@Composable
fun TripSummaryCard(
    trip: TripSummary,
    isDarkTheme: Boolean,
    containerColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onClick: () -> Unit,
    onAssignDriver: () -> Unit,
    onCancelTrip: () -> Unit
) {
    val accentColor = tripStatusColor(trip.tripStatus)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor.copy(alpha = if (isDarkTheme) 0.95f else 0.85f)
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkTheme) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusPill(
                    text = trip.tripStatus.name,
                    color = accentColor,
                    isDarkTheme = isDarkTheme
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "TRIP-${trip.id.toString().padStart(3, '0')}",
                    color = secondaryTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TripLocationBlock(
                    title = stringResource(R.string.label_pickup),
                    location = "${trip.pickupCity}, ${trip.pickupCountry}",
                    dateTime = trip.pickupDateTime,
                    timeZone = trip.pickupTimeZone,
                    accentColor = accentColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(secondaryTextColor.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = secondaryTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                TripLocationBlock(
                    title = stringResource(R.string.label_delivery),
                    location = "${trip.deliveryCity}, ${trip.deliveryCountry}",
                    dateTime = trip.deliveryDateTime,
                    timeZone = trip.deliveryTimeZone,
                    accentColor = accentColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    modifier = Modifier.weight(1f)
                )
            }

            if (trip.tripStatus == TripStatus.PLANNED) {
                TextButton(
                    onClick = onAssignDriver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(text = stringResource(R.string.button_assign_driver_for_trip))
                }
            }

            if (
                trip.tripStatus == TripStatus.PLANNED ||
                trip.tripStatus == TripStatus.ASSIGNED
            ) {
                TextButton(
                    onClick = onCancelTrip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.button_cancel_trip),
                        color = Color(0xFFFF4D4F)
                    )
                }
            }
        }
    }
}