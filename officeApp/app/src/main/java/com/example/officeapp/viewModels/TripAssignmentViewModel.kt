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

    fun getAvailableVehiclesForTrip(
        tripId: Long
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                availablePrimaryVehicles = emptyList(),
                availableTrailers = emptyList()
            )

            when (
                val result = tripAssignmentService.getAvailableVehiclesForTrip(tripId)
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        availablePrimaryVehicles = result.data.primaryVehicles,
                        availableTrailers = result.data.trailers
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        availablePrimaryVehicles = emptyList(),
                        availableTrailers = emptyList()
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

    fun assignTrip(
        tripId: Long,
        driverId: Long,
        primaryVehicleId: Long,
        trailerVehicleId: Long?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = tripAssignmentService.assignTrip(
                    tripId = tripId,
                    driverId = driverId,
                    primaryVehicleId = primaryVehicleId,
                    trailerVehicleId = trailerVehicleId
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = result.data.message ?: "Assigned was created with success.",
                        errorMessage = null,
                        availableDrivers = emptyList(),
                        availablePrimaryVehicles = emptyList(),
                        availableTrailers = emptyList()
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        successMessage = null
                    )
                }

                is ApiResult.Loading -> {
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
}