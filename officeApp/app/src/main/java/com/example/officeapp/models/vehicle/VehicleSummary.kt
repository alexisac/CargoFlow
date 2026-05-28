package com.example.officeapp.models.vehicle

data class VehicleSummary(
    val id: Long,
    val licencePlate: String,
    val vin: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val vehicleType: VehicleType,
    val maxWeight: Int?,
    val maxVolume: Int?,
    val vehicleStatus: VehicleStatus,
    val additionalInfo: String?
)