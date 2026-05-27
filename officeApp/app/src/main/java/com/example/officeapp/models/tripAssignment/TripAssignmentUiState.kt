package com.example.officeapp.models.tripAssignment

data class TripAssignmentUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val availableDrivers: List<AvailableDriver> = emptyList(),
    val driversPageNumber: Int = 0,
    val driversPageSize: Int = 20,
    val driversLastPage: Boolean = false,

    val availablePrimaryVehicles: List<AvailableVehicle> = emptyList(),
    val primaryVehiclesPageNumber: Int = 0,
    val primaryVehiclesPageSize: Int = 20,
    val primaryVehiclesLastPage: Boolean = false,

    val availableTrailers: List<AvailableVehicle> = emptyList(),
    val trailersPageNumber: Int = 0,
    val trailersPageSize: Int = 20,
    val trailersLastPage: Boolean = false
)
