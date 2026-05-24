package com.example.officeapp.models.tripAssignment

data class AvailableVehiclesResponse(
    val primaryVehicles: List<AvailableVehicle>,
    val trailers: List<AvailableVehicle>
)