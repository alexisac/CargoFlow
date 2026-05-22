package com.example.officeapp.models.trip

data class Trip(
    val id: Long,
    val tripStatus: TripStatus,
    val pickupAddress: Address,
    val deliveryAddress: Address,
    val pickupDateTime: String,
    val pickupTimeZone: String,
    val deliveryDateTime: String,
    val deliveryTimeZone: String,
    val cargoDescription: String?,
    val cargoWeight: Int?,
    val cargoVolume: Int?,
    val cargoType: CargoType,
    val price: Double,
    val currency: Currency,
    val additionalInfo: String?,
    val createdBy: String
)
