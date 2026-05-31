package com.example.officeapp.screens.manageVehicles

import androidx.compose.ui.graphics.Color
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.ui.theme.SuccessGreen
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.ui.theme.WarningOrange

fun vehicleStatusColor(
    status: VehicleStatus
): Color {
    return when (status) {
        VehicleStatus.AVAILABLE -> SuccessGreen
        VehicleStatus.NEED_MAINTENANCE -> WarningOrange
        VehicleStatus.INACTIVE -> TextSecondaryLight
    }
}