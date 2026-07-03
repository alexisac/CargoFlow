package com.example.officeapp.models.assignmentAi

data class OptimalAssignmentResponseDto(
    val tripId: Long,
    val driverId: Long,
    val primaryVehicleId: Long,
    val trailerId: Long?,
    val confidence: Double,
    val modelType: String
)