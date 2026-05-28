package com.example.officeapp.models.vehicle

data class GetAllVehiclesResponse(
    val vehicles: List<VehicleSummary>,
    val pageNumber: Int,
    val pageSize: Int,
    val lastPage: Boolean
)