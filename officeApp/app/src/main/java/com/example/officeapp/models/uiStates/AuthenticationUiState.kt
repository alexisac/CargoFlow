package com.example.officeapp.models.uiStates

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isCheckingSession: Boolean = true,
    val userRole: String? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)