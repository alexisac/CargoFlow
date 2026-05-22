package com.example.officeapp.models.user

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isCheckingSession: Boolean = true,
    val userRole: String? = null,
    val userFirstName: String? = null,
    var userLastName: String? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)