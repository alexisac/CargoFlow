package com.example.officeapp.models.trip

data class TripSummary(
    val id: Long,
    val tripStatus: TripStatus,
    val pickupCountry: String,
    val pickupCity: String,
    val deliveryCountry: String,
    val deliveryCity: String,
    val pickupDateTime: String,
    val pickupTimeZone: String,
    val deliveryDateTime: String,
    val deliveryTimeZone: String
)