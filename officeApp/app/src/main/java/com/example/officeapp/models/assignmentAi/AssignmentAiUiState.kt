package com.example.officeapp.models.assignmentAi

data class AssignmentAiUiState(
    val isLoading: Boolean = false,
    val recommendation: OptimalAssignmentResponseDto? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)