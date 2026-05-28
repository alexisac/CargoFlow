package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import com.example.officeapp.models.vehicle.ChangeVehicleStatusRequest
import com.example.officeapp.models.vehicle.GetAllVehiclesResponse
import com.example.officeapp.models.vehicle.VehicleCapacityRequirement
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.models.vehicle.capacityRequirement
import com.example.officeapp.repositories.VehicleRepository
import com.example.officeapp.utils.ApiResult
import java.time.Year
import javax.inject.Inject

class VehicleService @Inject constructor(
    private val vehicleRepository: VehicleRepository
) {
    suspend fun addNewVehicle(
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
    ): ApiResult<GenericApplicationResponse> {
        val normalizedLicencePlate = licencePlate.trim().uppercase()
        val normalizedVin = vin.trim().uppercase()
        val trimmedBrand = brand.trim()
        val trimmedModel = model.trim()
        val trimmedAdditionalInfo = additionalInfo
            ?.trim()
            ?.takeIf { it.isNotBlank() }

        val licencePlateRegex = Regex(ValidationMessages.LICENCE_PLATE_REGEX)

        if (!licencePlateRegex.matches(normalizedLicencePlate))
            return ApiResult.Error(ValidationMessages.LICENCE_PLATE_FORMAT)

        if (normalizedVin.length != 17)
            return ApiResult.Error(ValidationMessages.VIN_LENGTH)

        if (trimmedBrand.length !in 2..50)
            return ApiResult.Error(ValidationMessages.BRAND_LENGTH)

        if (trimmedModel.length !in 3..50)
            return ApiResult.Error(ValidationMessages.MODEL_LENGTH)

        val currentYear = Year.now().value

        val manufactureYearInt = manufactureYear.trim().toIntOrNull()
            ?: return ApiResult.Error(ValidationMessages.MANUFACTURE_YEAR_REQUIRED)

        if (manufactureYearInt !in 1900..currentYear)
            return ApiResult.Error(ValidationMessages.MANUFACTURE_YEAR_RANGE + currentYear)

        val capacityRequirement = vehicleType.capacityRequirement()

        val maxWeightInt: Int? = when(capacityRequirement) {
            VehicleCapacityRequirement.WEIGHT_AND_VOLUME -> {
                val value = maxWeight.trim().toIntOrNull()
                    ?: return ApiResult.Error(ValidationMessages.MAX_WEIGHT_REQUIRED)

                if (value !in 1 .. 24000)
                    return ApiResult.Error(ValidationMessages.MAX_WEIGHT_RANGE)

                value
            }
            VehicleCapacityRequirement.ONLY_VOLUME,
            VehicleCapacityRequirement.NONE -> {
                null
            }
        }

        val maxVolumeInt: Int? = when (capacityRequirement) {
            VehicleCapacityRequirement.WEIGHT_AND_VOLUME,
            VehicleCapacityRequirement.ONLY_VOLUME -> {
                val value = maxVolume.trim().toIntOrNull()
                    ?: return ApiResult.Error(ValidationMessages.MAX_VOLUME_REQUIRED)

                if (value !in 1 .. 90)
                    return ApiResult.Error(ValidationMessages.MAX_VOLUME_RANGE)

                value
            }
            VehicleCapacityRequirement.NONE -> {
                null
            }
        }

        if (trimmedAdditionalInfo != null && trimmedAdditionalInfo.length > 250)
            return ApiResult.Error(ValidationMessages.ADDITIONAL_INFO_MAX_LENGTH)

        val request = AddNewVehicleRequest(
            licencePlate = normalizedLicencePlate,
            vin = normalizedVin,
            brand = trimmedBrand,
            model = trimmedModel,
            manufactureYear = manufactureYearInt,
            vehicleType = vehicleType,
            maxWeight = maxWeightInt,
            maxVolume = maxVolumeInt,
            vehicleStatus = vehicleStatus,
            additionalInfo = trimmedAdditionalInfo
        )

        return vehicleRepository.addNewVehicle(request)
    }

    suspend fun getAllVehicles(
        pageNumber: Int,
        pageSize: Int
    ): ApiResult<GetAllVehiclesResponse> {
        if (pageNumber < 0)
            return ApiResult.Error(ValidationMessages.PAGE_NUMBER_RANGE)

        if (pageSize < 0)
            return ApiResult.Error(ValidationMessages.PAGE_SIZE_RANGE)

        return vehicleRepository.getAllVehicles(
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    suspend fun changeVehicleStatus(
        vehicleId: Long,
        vehicleStatus: VehicleStatus
    ): ApiResult<GenericApplicationResponse> {
        if (vehicleId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        val request = ChangeVehicleStatusRequest(vehicleStatus)

        return vehicleRepository.changeVehicleStatus(
            vehicleId = vehicleId,
            changeVehicleStatusRequest = request
        )
    }
}