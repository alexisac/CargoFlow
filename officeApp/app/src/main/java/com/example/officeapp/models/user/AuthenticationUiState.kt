package com.example.officeapp.models.user

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isCheckingSession: Boolean = true,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val userRole: String? = null,
    val userFirstName: String? = null,
    var userLastName: String? = null,

    val users: List<UserSummary> = emptyList(),
    val pageNumber: Int = 0,
    val pageSize: Int = 20,
    val lastPage: Boolean = false
)