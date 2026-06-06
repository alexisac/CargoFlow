package com.example.officeapp.models.location

data class LocationUiState(
    val isLoading: Boolean = false,
    val driverLocations: List<DriverLocation> = emptyList(),
    val isWebSocketConnected: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)