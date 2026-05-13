package com.example.officeapp.models.uiStates

data class VehicleUiState (
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)