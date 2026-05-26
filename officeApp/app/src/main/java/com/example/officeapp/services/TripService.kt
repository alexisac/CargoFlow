package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import com.example.officeapp.models.trip.Address
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.CompletedTripsResponse
import com.example.officeapp.models.trip.Currency
import com.example.officeapp.models.trip.CurrentTrip
import com.example.officeapp.models.trip.Trip
import com.example.officeapp.models.trip.TripPageResponse
import com.example.officeapp.models.trip.TripSearchRequest
import com.example.officeapp.models.trip.TripStatus
import com.example.officeapp.repositories.TripRepository
import com.example.officeapp.utils.ApiResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TripService @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend fun addNewTrip(
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
    ): ApiResult<GenericApplicationResponse> {
        val pickupAddressResult = buildAddress(
            country = pickupCountry,
            administrativeArea = pickupAdministrativeArea,
            city = pickupCity,
            streetName = pickupStreetName,
            streetNumber = pickupStreetNumber,
            postalCode = pickupPostalCode,
            additionalDetails = pickupAdditionalDetails
        )

        val pickupAddress = when (pickupAddressResult) {
            is AddressBuildResult.Success -> pickupAddressResult.address
            is AddressBuildResult.Error -> return ApiResult.Error(ValidationMessages.PICKUP_ADDRESS_PREFIX + pickupAddressResult.message)
        }

        val deliveryAddressResult = buildAddress(
            country = deliveryCountry,
            administrativeArea = deliveryAdministrativeArea,
            city = deliveryCity,
            streetName = deliveryStreetName,
            streetNumber = deliveryStreetNumber,
            postalCode = deliveryPostalCode,
            additionalDetails = deliveryAdditionalDetails
        )

        val deliveryAddress = when (deliveryAddressResult) {
            is AddressBuildResult.Success -> deliveryAddressResult.address
            is AddressBuildResult.Error -> return ApiResult.Error(ValidationMessages.DELIVERY_ADDRESS_PREFIX + deliveryAddressResult.message)
        }

        val trimmedPickupDateTime = pickupDateTime.trim()
        val trimmedPickupTimeZone = pickupTimeZone.trim()
        val trimmedDeliveryDateTime = deliveryDateTime.trim()
        val trimmedDeliveryTimeZone = deliveryTimeZone.trim()
        val trimmedCargoDescription = cargoDescription
            ?.trim()
            ?.takeIf { it.isNotBlank() }
        val trimmedAdditionalInfo = additionalInfo
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        if (trimmedPickupDateTime.isBlank())
            return ApiResult.Error(ValidationMessages.PICKUP_DATE_TIME_REQUIRED)

        if (trimmedDeliveryDateTime.isBlank())
            return ApiResult.Error(ValidationMessages.DELIVERY_DATE_TIME_REQUIRED)

        if (trimmedPickupTimeZone.length !in 3..50)
            return ApiResult.Error(ValidationMessages.PICKUP_TIME_ZONE_LENGTH)

        if (trimmedDeliveryTimeZone.length !in 3..50)
            return ApiResult.Error(ValidationMessages.DELIVERY_TIME_ZONE_LENGTH)

        val pickupLocalDateTime = try {
            LocalDateTime.parse(trimmedPickupDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (_: Exception) {
            return ApiResult.Error(ValidationMessages.PICKUP_DATE_TIME_FORMAT)
        }

        val deliveryLocalDateTime = try {
            LocalDateTime.parse(trimmedDeliveryDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (_: Exception) {
            return ApiResult.Error(ValidationMessages.DELIVERY_DATE_TIME_FORMAT)
        }

        if (!deliveryLocalDateTime.isAfter(pickupLocalDateTime))
            return ApiResult.Error(ValidationMessages.DELIVERY_DATE_TIME_AFTER_PICKUP)

        if (trimmedCargoDescription != null && trimmedCargoDescription.length !in 3..250)
            return ApiResult.Error(ValidationMessages.CARGO_DESCRIPTION_LENGTH)


        val trimmedCargoWeight = cargoWeight.trim().takeIf { it.isNotBlank() }
        val cargoWeightInt = if (trimmedCargoWeight == null)
            null
        else
            trimmedCargoWeight.toIntOrNull() ?: return ApiResult.Error(ValidationMessages.CARGO_WEIGHT_REQUIRED)

        if (cargoWeightInt != null && cargoWeightInt !in 1..24000)
            return ApiResult.Error(ValidationMessages.CARGO_WEIGHT_RANGE)

        val trimmedCargoVolume = cargoVolume.trim().takeIf { it.isNotBlank() }
        val cargoVolumeInt = if (trimmedCargoVolume == null)
            null
        else
            trimmedCargoVolume.toIntOrNull() ?: return ApiResult.Error(ValidationMessages.CARGO_VOLUME_REQUIRED)

        if (cargoVolumeInt != null && cargoVolumeInt !in 1..90)
            return ApiResult.Error(ValidationMessages.CARGO_VOLUME_RANGE)

        val priceDouble = price.trim().toDoubleOrNull()
            ?: return ApiResult.Error(ValidationMessages.PRICE_REQUIRED)

        if (priceDouble < 0)
            return ApiResult.Error(ValidationMessages.PRICE_RANGE)

        if (trimmedAdditionalInfo != null && trimmedAdditionalInfo.length > 250)
            return ApiResult.Error(ValidationMessages.ADDITIONAL_INFO_MAX_LENGTH)


        val request = AddNewTripRequest(
            pickupAddress = pickupAddress,
            deliveryAddress = deliveryAddress,
            pickupDateTime = trimmedPickupDateTime,
            pickupTimeZone = trimmedPickupTimeZone,
            deliveryDateTime = trimmedDeliveryDateTime,
            deliveryTimeZone = trimmedDeliveryTimeZone,
            cargoDescription = trimmedCargoDescription,
            cargoWeight = cargoWeightInt,
            cargoVolume = cargoVolumeInt,
            cargoType = cargoType,
            price = priceDouble,
            currency = currency,
            additionalInfo = trimmedAdditionalInfo
        )

        return tripRepository.addNewTrip(request)
    }

    suspend fun searchTrips(
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
        pageSize: Int
    ): ApiResult<TripPageResponse> {
        if (pageNumber < 0)
            return ApiResult.Error(ValidationMessages.PAGE_NUMBER_RANGE)

        if (pageSize < 0)
            return ApiResult.Error(ValidationMessages.PAGE_SIZE_RANGE)

        val request = TripSearchRequest(
            tripStatusList = tripStatusList,
            pickupCountries = pickupCountries.cleanStringList(),
            pickupCities = pickupCities.cleanStringList(),
            deliveryCountries = deliveryCountries.cleanStringList(),
            deliveryCities = deliveryCities.cleanStringList(),
            pickupDateTimeFrom = pickupDateTimeFrom.cleanNullableString(),
            pickupDateTimeTo = pickupDateTimeTo.cleanNullableString(),
            deliveryDateTimeFrom = deliveryDateTimeFrom.cleanNullableString(),
            deliveryDateTimeTo = deliveryDateTimeTo.cleanNullableString(),
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        return tripRepository.searchTrips(request)
    }

    suspend fun getTrip(
        tripId: Long
    ): ApiResult<Trip> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        return tripRepository.getTrip(tripId)
    }

    suspend fun getCurrentTrip(): ApiResult<CurrentTrip> {
        return tripRepository.getCurrentTrip()
    }

    suspend fun getCompletedTrips(days: Int): ApiResult<CompletedTripsResponse> {
        if (days !in listOf(30, 60, 90))
            return ApiResult.Error(ValidationMessages.COMPLETED_TRIPS_PERIOD_INVALID)

        return tripRepository.getCompletedTrips(days)
    }

    private fun buildAddress(
        country: String,
        administrativeArea: String,
        city: String,
        streetName: String,
        streetNumber: String,
        postalCode: String,
        additionalDetails: String?
    ): AddressBuildResult {
        val trimmedCountry = country.trim()
        val trimmedAdministrativeArea = administrativeArea.trim()
        val trimmedCity = city.trim()
        val trimmedStreetName = streetName.trim()
        val trimmedStreetNumber = streetNumber.trim()
        val trimmedPostalCode = postalCode.trim()
        val trimmedAdditionalDetails = additionalDetails
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        if (trimmedCountry.length !in 2..50)
            return AddressBuildResult.Error(ValidationMessages.COUNTRY_LENGTH)

        if (trimmedAdministrativeArea.length !in 2..50)
            return AddressBuildResult.Error(ValidationMessages.ADMINISTRATIVE_AREA_LENGTH)

        if (trimmedCity.length !in 2..50)
            return AddressBuildResult.Error(ValidationMessages.CITY_LENGTH)

        if (trimmedStreetName.length !in 2..100)
            return AddressBuildResult.Error(ValidationMessages.STREET_NAME_LENGTH)

        if (trimmedStreetNumber.length !in 1..15)
            return AddressBuildResult.Error(ValidationMessages.STREET_NUMBER_LENGTH)

        if (trimmedPostalCode.length !in 2..15)
            return AddressBuildResult.Error(ValidationMessages.POSTAL_CODE_LENGTH)

        if (trimmedAdditionalDetails != null && trimmedAdditionalDetails.length > 250)
            return AddressBuildResult.Error(ValidationMessages.ADDITIONAL_DETAILS_MAX_LENGTH)

        return AddressBuildResult.Success(
            Address(
                country = trimmedCountry,
                administrativeArea = trimmedAdministrativeArea,
                city = trimmedCity,
                streetName = trimmedStreetName,
                streetNumber = trimmedStreetNumber,
                postalCode = trimmedPostalCode,
                additionalDetails = trimmedAdditionalDetails
            )
        )
    }

    private fun List<String>.cleanStringList(): List<String> {
        return this
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }

    private fun String?.cleanNullableString(): String? {
        return this
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    private sealed class AddressBuildResult {
        data class Success(val address: Address): AddressBuildResult()
        data class Error(val message: String): AddressBuildResult()
    }
}