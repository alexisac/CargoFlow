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
        tripId: Long,
        pageNumber: Int,
        pageSize: Int,
        append: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

            when (
                val result = tripAssignmentService.getAvailableDriversForTrip(
                    tripId = tripId,
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
            ) {
                is ApiResult.Success -> {
                    val currentDrivers = if (append) {
                        _uiState.value.availableDrivers
                    } else {
                        emptyList()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        availableDrivers = currentDrivers + result.data.drivers,
                        driversPageNumber = result.data.pageNumber,
                        driversPageSize = result.data.pageSize,
                        driversLastPage = result.data.lastPage
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

    fun loadNextDriversPage(tripId: Long) {
        val state = _uiState.value

        if (state.isLoading || state.driversLastPage) {
            return
        }

        getAvailableDriversForTrip(
            tripId = tripId,
            pageNumber = state.driversPageNumber + 1,
            pageSize = state.driversPageSize,
            append = true
        )
    }

    fun getAvailablePrimaryVehiclesForTrip(
        tripId: Long,
        pageNumber: Int,
        pageSize: Int,
        append: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

            when (
                val result = tripAssignmentService.getAvailablePrimaryVehiclesForTrip(
                    tripId = tripId,
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
            ) {
                is ApiResult.Success -> {
                    val currentVehicle = if (append) {
                        _uiState.value.availablePrimaryVehicles
                    } else {
                        emptyList()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        availablePrimaryVehicles = currentVehicle + result.data.vehicles,
                        primaryVehiclesPageNumber = result.data.pageNumber,
                        primaryVehiclesPageSize = result.data.pageSize,
                        primaryVehiclesLastPage = result.data.lastPage,
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        availablePrimaryVehicles = emptyList(),
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

    fun loadNextPrimaryVehiclesPage(tripId: Long) {
        val state = _uiState.value

        if (state.isLoading || state.primaryVehiclesLastPage) {
            return
        }

        getAvailablePrimaryVehiclesForTrip(
            tripId = tripId,
            pageNumber = state.primaryVehiclesPageNumber + 1,
            pageSize = state.primaryVehiclesPageSize,
            append = true
        )
    }

    fun getAvailableTrailersForTrip(
        tripId: Long,
        pageNumber: Int,
        pageSize: Int,
        append: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

            when (
                val result = tripAssignmentService.getAvailableTrailersForTrip(
                    tripId = tripId,
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
            ) {
                is ApiResult.Success -> {
                    val currentVehicle = if (append) {
                        _uiState.value.availableTrailers
                    } else {
                        emptyList()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        availableTrailers = currentVehicle + result.data.vehicles,
                        trailersPageNumber = result.data.pageNumber,
                        trailersPageSize = result.data.pageSize,
                        trailersLastPage = result.data.lastPage,
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        availableTrailers = emptyList(),
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

    fun loadNextTrailersPage(tripId: Long) {
        val state = _uiState.value

        if (state.isLoading || state.trailersLastPage) {
            return
        }

        getAvailableTrailersForTrip(
            tripId = tripId,
            pageNumber = state.trailersPageNumber + 1,
            pageSize = state.trailersPageSize,
            append = true
        )
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

    fun clearTrailers(){
        _uiState.value = _uiState.value.copy(
            availableTrailers = emptyList(),
            trailersPageNumber = 0,
            trailersLastPage = false
        )
    }
}