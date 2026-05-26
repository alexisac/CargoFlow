package com.example.officeapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.Currency
import com.example.officeapp.models.trip.TripStatus
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

    fun searchTrips(
        tripStatusList: List<TripStatus>,
        pickupCountries: List<String>,
        pickupCities: List<String>,
        deliveryCountries: List<String>,
        deliveryCities: List<String>,
        pickupDateTimeFrom: String?,
        pickupDateTimeTo: String?,
        deliveryDateTimeFrom: String?,
        deliveryDateTimeTo: String?,
        pageNumber: Int,
        pageSize: Int,
        append: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = tripService.searchTrips(
                    tripStatusList = tripStatusList,
                    pickupCountries = pickupCountries,
                    pickupCities = pickupCities,
                    deliveryCountries = deliveryCountries,
                    deliveryCities = deliveryCities,
                    pickupDateTimeFrom = pickupDateTimeFrom,
                    pickupDateTimeTo = pickupDateTimeTo,
                    deliveryDateTimeFrom = deliveryDateTimeFrom,
                    deliveryDateTimeTo = deliveryDateTimeTo,
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
            ) {
                is ApiResult.Success -> {
                    val currentTrips = if (append) {
                        _uiState.value.trips
                    } else {
                        emptyList()
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        trips = currentTrips + result.data.trips,
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

    fun loadNextTripsPage(
        tripStatusList: List<TripStatus>,
        pickupCountries: List<String>,
        pickupCities: List<String>,
        deliveryCountries: List<String>,
        deliveryCities: List<String>,
        pickupDateTimeFrom: String?,
        pickupDateTimeTo: String?,
        deliveryDateTimeFrom: String?,
        deliveryDateTimeTo: String?
    ) {
        val state = _uiState.value

        if (state.isLoading || state.lastPage) {
            return
        }

        searchTrips(
            tripStatusList = tripStatusList,
            pickupCountries = pickupCountries,
            pickupCities = pickupCities,
            deliveryCountries = deliveryCountries,
            deliveryCities = deliveryCities,
            pickupDateTimeFrom = pickupDateTimeFrom,
            pickupDateTimeTo = pickupDateTimeTo,
            deliveryDateTimeFrom = deliveryDateTimeFrom,
            deliveryDateTimeTo = deliveryDateTimeTo,
            pageNumber = state.pageNumber + 1,
            pageSize = state.pageSize,
            append = true
        )
    }

    fun getTrip(
        tripId: Long
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                currentTrip = null
            )

            when (val result = tripService.getTrip(tripId)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        currentTrip = result.data
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

    fun getCurrentTrip() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                currentDriverTrip = null
            )

            when (val result = tripService.getCurrentTrip()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        currentDriverTrip = result.data
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        currentDriverTrip = null
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

    fun getCompletedTrips(days: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                completedTrips = emptyList()
            )

            when (val result = tripService.getCompletedTrips(days)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        completedTrips = result.data.trips
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        completedTrips = emptyList()
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
            errorMessage = null,
            completedTrips = emptyList()
        )
    }
}