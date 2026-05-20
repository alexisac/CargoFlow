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
        vehicleStatus: VehicleStatus,
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
                    vehicleStatus = vehicleStatus,
                    additionalInfo = additionalInfo
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = result.data.message ?: "Vehicle was created with success."
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

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}