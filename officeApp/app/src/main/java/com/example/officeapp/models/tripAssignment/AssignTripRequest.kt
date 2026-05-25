package com.example.officeapp.models.tripAssignment

data class AssignTripRequest(
    val tripId: Long,
    val driverId: Long,
    val primaryVehicleId: Long,
    val trailerVehicleId: Long?
)
