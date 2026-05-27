package com.example.officeapp.models.tripAssignment

data class AvailableVehiclesResponse(
    val vehicles: List<AvailableVehicle>,
    val pageNumber: Int,
    val pageSize: Int,
    val lastPage: Boolean
)