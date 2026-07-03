package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.assignmentAi.AssignmentAiUiState
import com.example.officeapp.services.AssignmentAiService
import com.example.officeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentAiViewModel @Inject constructor(
    private val assignmentAiService: AssignmentAiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssignmentAiUiState())
    val uiState: StateFlow<AssignmentAiUiState> = _uiState.asStateFlow()

    fun autoOptimizeTripAssignment(
        tripId: Long
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = assignmentAiService.autoOptimizeTripAssignment(
                    tripId = tripId
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recommendation = result.data,
                        successMessage = "AI assignment recommendation was generated successfully.",
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        successMessage = null
                    )
                }

                ApiResult.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun clearRecommendation() {
        _uiState.value = _uiState.value.copy(
            recommendation = null
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}