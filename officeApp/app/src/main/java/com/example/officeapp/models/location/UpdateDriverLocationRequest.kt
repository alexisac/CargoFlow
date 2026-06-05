package com.example.officeapp.models.location

data class UpdateDriverLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val updatedAt: String
)
