package com.example.officeapp.screens.manageVehicles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleSummary

@Composable
fun VehicleManagementCard(
    vehicle: VehicleSummary,
    accentColor: Color,
    isDarkTheme: Boolean,
    containerColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    onChangeStatusClick: (VehicleStatus) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor.copy(alpha = if (isDarkTheme) 0.9f else 0.8f)
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkTheme) 0.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = if (isDarkTheme) 0.18f else 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalShipping,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = vehicle.licencePlate,
                        color = textColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${vehicle.brand} ${vehicle.model}",
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            VehicleInfoRow(
                icon = Icons.Outlined.DirectionsCar,
                label = stringResource(R.string.label_vin),
                value = vehicle.vin,
                secondaryTextColor = secondaryTextColor
            )

            VehicleInfoRow(
                icon = Icons.Outlined.Numbers,
                label = stringResource(R.string.label_type),
                value = vehicle.vehicleType.name,
                secondaryTextColor = secondaryTextColor
            )

            VehicleStatusInfoRow(
                status = vehicle.vehicleStatus,
                secondaryTextColor = secondaryTextColor
            )

            VehicleInfoRow(
                icon = Icons.Outlined.CalendarMonth,
                label = stringResource(R.string.label_manufacture_year),
                value = vehicle.manufactureYear.toString(),
                secondaryTextColor = secondaryTextColor
            )

            vehicle.maxWeight?.let {
                VehicleInfoRow(
                    icon = Icons.Outlined.Scale,
                    label = stringResource(R.string.label_maximum_weight),
                    value = it.toString(),
                    secondaryTextColor = secondaryTextColor
                )
            }

            vehicle.maxVolume?.let {
                VehicleInfoRow(
                    icon = Icons.Outlined.Inventory2,
                    label = stringResource(R.string.label_maximum_volume),
                    value = it.toString(),
                    secondaryTextColor = secondaryTextColor
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(secondaryTextColor.copy(alpha = 0.18f))
            )

            Spacer(modifier = Modifier.height(18.dp))

            VehicleStatusDropdown(
                selectedStatus = vehicle.vehicleStatus,
                accentColor = accentColor,
                isDarkTheme = isDarkTheme,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                onStatusSelected = { status ->
                    if (status != vehicle.vehicleStatus) {
                        onChangeStatusClick(status)
                    }
                }
            )
        }
    }
}

@Composable
private fun VehicleStatusInfoRow(
    status: VehicleStatus,
    secondaryTextColor: Color
) {
    val statusColor = vehicleStatusColor(status)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Speed,
            contentDescription = null,
            tint = secondaryTextColor,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = stringResource(R.string.label_status),
            color = secondaryTextColor,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.weight(1.6f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(RoundedCornerShape(50))
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = status.name,
                color = secondaryTextColor,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}