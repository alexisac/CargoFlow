package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.models.vehicle.VehicleUiState
import com.example.officeapp.services.VehicleService
import com.example.officeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleService: VehicleService
): ViewModel() {
    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState: StateFlow<VehicleUiState> = _uiState.asStateFlow()

    fun addNewVehicle(
        licencePlate: String,
        vin: String,
        brand: String,
        model: String,
        manufactureYear: String,
        vehicleType: VehicleType,
        maxWeight: String,
        maxVolume: String,
        additionalInfo: String?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = vehicleService.addNewVehicle(
                    licencePlate = licencePlate,
                    vin = vin,
                    brand = brand,
                    model = model,
                    manufactureYear = manufactureYear,
                    vehicleType = vehicleType,
                    maxWeight = maxWeight,
                    maxVolume = maxVolume,
                    additionalInfo = additionalInfo
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = result.data.message
                            ?: "Vehicle was created with success."
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

    fun getAllVehicles(
        pageNumber: Int = 0,
        pageSize: Int = 20,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = vehicleService.getAllVehicles(
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
            ) {
                is ApiResult.Success -> {
                    val currentVehicles = if (append) {
                        _uiState.value.vehicles
                    } else {
                        emptyList()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vehicles = currentVehicles + result.data.vehicles,
                        pageNumber = result.data.pageNumber,
                        pageSize = result.data.pageSize,
                        lastPage = result.data.lastPage,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
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

    fun loadNextVehiclesPage() {
        val state = _uiState.value

        if (state.isLoading || state.lastPage) {
            return
        }

        getAllVehicles(
            pageNumber = state.pageNumber + 1,
            pageSize = state.pageSize,
            append = true
        )
    }

    fun changeVehicleStatus(
        vehicleId: Long,
        vehicleStatus: VehicleStatus
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = vehicleService.changeVehicleStatus(
                    vehicleId = vehicleId,
                    vehicleStatus = vehicleStatus
                )
            ) {
                is ApiResult.Success -> {
                    val updatedVehicles = _uiState.value.vehicles.map { vehicle ->
                        if (vehicle.id == vehicleId) {
                            vehicle.copy(vehicleStatus = vehicleStatus)
                        } else {
                            vehicle
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vehicles = updatedVehicles,
                        successMessage = result.data.message
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

    fun getVehicleDashboardSummary() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = vehicleService.getVehicleDashboardSummary()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        vehicleDashboardSummaryItems = result.data.items,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
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

    fun clearVehicles() {
        _uiState.value = _uiState.value.copy(
            vehicles = emptyList(),
            pageNumber = 0,
            pageSize = 20,
            lastPage = false
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}