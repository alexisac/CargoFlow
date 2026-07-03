package com.example.officeapp.models.vehicle

data class VehicleUiState (
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val vehicles: List<VehicleSummary> = emptyList(),
    val pageNumber: Int = 0,
    val pageSize: Int = 20,
    val lastPage: Boolean = false,

    val vehicleDashboardSummaryItems: List<VehicleDashboardSummaryItem> = emptyList()
)