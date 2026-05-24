package com.example.officeapp.models.tripAssignment

import com.example.officeapp.models.vehicle.VehicleType

data class AvailableVehicle(
    val id: Long,
    val licencePlate: String,
    val vehicleType: VehicleType
)