package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.Currency
import com.example.officeapp.models.trip.TripUiState
import com.example.officeapp.services.TripService
import com.example.officeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripService: TripService
): ViewModel() {
    private val _uiState = MutableStateFlow(TripUiState())
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    fun addNewTrip(
        pickupCountry: String,
        pickupAdministrativeArea: String,
        pickupCity: String,
        pickupStreetName: String,
        pickupStreetNumber: String,
        pickupPostalCode: String,
        pickupAdditionalDetails: String?,

        deliveryCountry: String,
        deliveryAdministrativeArea: String,
        deliveryCity: String,
        deliveryStreetName: String,
        deliveryStreetNumber: String,
        deliveryPostalCode: String,
        deliveryAdditionalDetails: String?,

        pickupDateTime: String,
        pickupTimeZone: String,
        deliveryDateTime: String,
        deliveryTimeZone: String,

        cargoDescription: String?,
        cargoWeight: String,
        cargoVolume: String,
        cargoType: CargoType,
        price: String,
        currency: Currency,
        additionalInfo: String?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = tripService.addNewTrip(
                    pickupCountry = pickupCountry,
                    pickupAdministrativeArea = pickupAdministrativeArea,
                    pickupCity = pickupCity,
                    pickupStreetName = pickupStreetName,
                    pickupStreetNumber = pickupStreetNumber,
                    pickupPostalCode = pickupPostalCode,
                    pickupAdditionalDetails = pickupAdditionalDetails,
                    deliveryCountry = deliveryCountry,
                    deliveryAdministrativeArea = deliveryAdministrativeArea,
                    deliveryCity = deliveryCity,
                    deliveryStreetName = deliveryStreetName,
                    deliveryStreetNumber = deliveryStreetNumber,
                    deliveryPostalCode = deliveryPostalCode,
                    deliveryAdditionalDetails = deliveryAdditionalDetails,
                    pickupDateTime = pickupDateTime,
                    pickupTimeZone = pickupTimeZone,
                    deliveryDateTime = deliveryDateTime,
                    deliveryTimeZone = deliveryTimeZone,
                    cargoDescription = cargoDescription,
                    cargoWeight = cargoWeight,
                    cargoVolume = cargoVolume,
                    cargoType = cargoType,
                    price = price,
                    currency = currency,
                    additionalInfo = additionalInfo
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = result.data.message ?: "Trip was created with success."
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