package com.example.officeapp.screens.driverLocations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R

@Composable
fun DriverLocationsStatusCard(
    driversCount: Int,
    isConnected: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    primaryColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$driversCount",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.label_drivers_on_map),
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (isConnected) {
                        stringResource(R.string.label_live)
                    } else {
                        stringResource(R.string.label_offline)
                    },
                    color = if (isConnected) primaryColor else secondaryTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = if (isConnected) {
                        stringResource(R.string.label_websocket_connected)
                    } else {
                        stringResource(R.string.label_waiting_for_live_updates)
                    },
                    color = secondaryTextColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}