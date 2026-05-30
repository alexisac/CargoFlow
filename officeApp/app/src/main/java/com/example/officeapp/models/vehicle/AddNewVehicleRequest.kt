package com.example.officeapp.models.vehicle

data class AddNewVehicleRequest (
    val licencePlate: String,
    val vin: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val vehicleType: VehicleType,
    val maxWeight: Int?,
    val maxVolume: Int?,
    val additionalInfo: String?
)