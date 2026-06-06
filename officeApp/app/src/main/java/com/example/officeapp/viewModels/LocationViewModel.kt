package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.location.DriverLocation
import com.example.officeapp.models.location.LocationUiState
import com.example.officeapp.services.LocationService
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.webSocket.DriverLocationWebSocketClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationService: LocationService,
    private val driverLocationWebSocketClient: DriverLocationWebSocketClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun getLatestDriverLocations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = locationService.getLatestDriverLocations()
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        driverLocations = result.data.driverLocations,
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

    fun startLiveDriverLocationUpdates() {
        driverLocationWebSocketClient.connect(
            onLocationReceived = { driverLocation ->
                viewModelScope.launch {
                    updateDriverLocation(driverLocation)
                }
            },
            onConnected = {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        isWebSocketConnected = true,
                        errorMessage = null
                    )
                }
            },
            onError = { message ->
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        isWebSocketConnected = false,
                        errorMessage = message
                    )
                }
            }
        )
    }

    fun stopLiveDriverLocationUpdates() {
        driverLocationWebSocketClient.disconnect()

        _uiState.value = _uiState.value.copy(
            isWebSocketConnected = false
        )
    }

    fun refreshDriverLocations() {
        getLatestDriverLocations()
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }

    fun clearDriverLocations() {
        _uiState.value = _uiState.value.copy(
            driverLocations = emptyList(),
            isWebSocketConnected = false,
            errorMessage = null,
            successMessage = null
        )
    }

    private fun updateDriverLocation(newDriverLocation: DriverLocation) {
        val updatedLocations = _uiState.value.driverLocations
            .filterNot { currentLocation ->
                currentLocation.driverId == newDriverLocation.driverId
            } + newDriverLocation

        _uiState.value = _uiState.value.copy(
            driverLocations = updatedLocations
        )
    }

    override fun onCleared() {
        driverLocationWebSocketClient.disconnect()
        super.onCleared()
    }
}