package com.example.officeapp.models.trip

data class TripUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val trips: List<TripSummary> = emptyList(),
    val pageNumber: Int = 0,
    val pageSize: Int = 20,
    val lastPage: Boolean = false,
    val currentTrip: Trip? = null
)
