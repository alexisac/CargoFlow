package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import com.example.officeapp.models.trip.Address
import com.example.officeapp.models.trip.CargoType
import com.example.officeapp.models.trip.Currency
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
            is AddressBuildResult.Error -> return ApiResult.Error("Pickup address: ${pickupAddressResult.message}")
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
            is AddressBuildResult.Error -> return ApiResult.Error("Delivery address: ${deliveryAddressResult.message}")
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
            return ApiResult.Error("Pickup date and time is required.")

        if (trimmedDeliveryDateTime.isBlank())
            return ApiResult.Error("Delivery date and time is required.")

        if (trimmedPickupTimeZone.length !in 3..50)
            return ApiResult.Error("Pickup time zone must be between 3 and 50 characters.")

        if (trimmedDeliveryTimeZone.length !in 3..50)
            return ApiResult.Error("Delivery time zone must be between 3 and 50 characters.")

        val pickupLocalDateTime = try {
            LocalDateTime.parse(trimmedPickupDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (_: Exception) {
            return ApiResult.Error("Pickup date and time must have format yyyy-MM-ddTHH:mm:ss.")
        }

        val deliveryLocalDateTime = try {
            LocalDateTime.parse(trimmedDeliveryDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (_: Exception) {
            return ApiResult.Error("Delivery date and time must have format yyyy-MM-ddTHH:mm:ss.")
        }

        if (!deliveryLocalDateTime.isAfter(pickupLocalDateTime))
            return ApiResult.Error("Delivery date and time must be after pickup date and time.")

        if (trimmedCargoDescription != null && trimmedCargoDescription.length !in 3..250)
            return ApiResult.Error("Cargo description must be between 3 and 250 characters.")


        val trimmedCargoWeight = cargoWeight.trim().takeIf { it.isNotBlank() }
        val cargoWeightInt = if (trimmedCargoWeight == null)
            null
        else
            trimmedCargoWeight.toIntOrNull() ?: return ApiResult.Error("Cargo weight must be a number.")

        if (cargoWeightInt != null && cargoWeightInt !in 1..24000)
            return ApiResult.Error("Cargo weight must be between 1 and 24000.")

        val trimmedCargoVolume = cargoVolume.trim().takeIf { it.isNotBlank() }
        val cargoVolumeInt = if (trimmedCargoVolume == null)
            null
        else
            trimmedCargoVolume.toIntOrNull() ?: return ApiResult.Error("Cargo volume must be a number.")

        if (cargoVolumeInt != null && cargoVolumeInt !in 1..90)
            return ApiResult.Error("Cargo volume must be between 1 and 90.")

        val priceDouble = price.trim().toDoubleOrNull()
            ?: return ApiResult.Error("Price must be a number.")

        if (priceDouble < 0)
            return ApiResult.Error("Price must be greater than or equal to 0.")

        if (trimmedAdditionalInfo != null && trimmedAdditionalInfo.length > 250)
            return ApiResult.Error("Additional info must have maximum 250 characters.")


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
            return ApiResult.Error("Page number cannot be negative.")

        if (pageSize < 0)
            return ApiResult.Error("Page size cannot be negative.")

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
            return AddressBuildResult.Error("Country must be between 2 and 50 characters.")

        if (trimmedAdministrativeArea.length !in 2..50)
            return AddressBuildResult.Error("Administrative area must be between 2 and 50 characters.")

        if (trimmedCity.length !in 2..50)
            return AddressBuildResult.Error("City must be between 2 and 50 characters.")

        if (trimmedStreetName.length !in 2..100)
            return AddressBuildResult.Error("Street name must be between 2 and 100 characters.")

        if (trimmedStreetNumber.length !in 1..15)
            return AddressBuildResult.Error("Street number must be between 1 and 15 characters.")

        if (trimmedPostalCode.length !in 2..15)
            return AddressBuildResult.Error("Postal code must be between 2 and 15 characters.")

        if (trimmedAdditionalDetails != null && trimmedAdditionalDetails.length > 250)
            return AddressBuildResult.Error("Additional address details must have maximum 250 characters.")

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