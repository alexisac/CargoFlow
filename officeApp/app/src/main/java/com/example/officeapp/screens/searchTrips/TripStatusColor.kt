package com.example.officeapp.screens.searchTrips

import androidx.compose.ui.graphics.Color
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.ErrorRed
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.SuccessGreen
import com.example.officeapp.ui.theme.WarningOrange

fun tripStatusColor(status: TripStatus): Color {
    return when (status) {
        TripStatus.PLANNED -> PrimaryBlueLight
        TripStatus.ASSIGNED -> AccentPink
        TripStatus.IN_PROGRESS -> WarningOrange
        TripStatus.COMPLETED -> SuccessGreen
        TripStatus.CANCELED -> ErrorRed
    }
}