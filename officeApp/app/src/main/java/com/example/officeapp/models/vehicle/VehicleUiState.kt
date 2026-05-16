package com.example.officeapp.models.vehicle

data class VehicleUiState (
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)