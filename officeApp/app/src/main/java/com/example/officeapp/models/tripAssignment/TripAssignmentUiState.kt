package com.example.officeapp.models.tripAssignment

data class TripAssignmentUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val availableDrivers: List<AvailableDriver> = emptyList()
)
