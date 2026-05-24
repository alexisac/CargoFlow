package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.tripAssignment.TripAssignmentUiState
import com.example.officeapp.services.TripAssignmentService
import com.example.officeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TripAssignmentViewModel @Inject constructor(
    private val tripAssignmentService: TripAssignmentService
) : ViewModel() {
    private val _uiState = MutableStateFlow(TripAssignmentUiState())
    val uiState: StateFlow<TripAssignmentUiState> = _uiState.asStateFlow()

    fun getAvailableDriversForTrip(
        tripId: Long
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                availableDrivers = emptyList()
            )

            when (
                val result = tripAssignmentService.getAvailableDriversForTrip(tripId)
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        availableDrivers = result.data.drivers
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        availableDrivers = emptyList()
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

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }

    fun clearAvailableDrivers() {
        _uiState.value = _uiState.value.copy(
            availableDrivers = emptyList()
        )
    }
}