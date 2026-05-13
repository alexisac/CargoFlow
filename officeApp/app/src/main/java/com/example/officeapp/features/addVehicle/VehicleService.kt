package com.example.officeapp.features.addVehicle

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import com.example.officeapp.models.vehicle.VehicleStatus
import com.example.officeapp.models.vehicle.VehicleType
import com.example.officeapp.utils.ApiResult
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

        val licencePlateRegex = Regex("^[A-Z]{1,2}-[0-9]{2,3}-[A-Z]{3}$")

        if (!licencePlateRegex.matches(normalizedLicencePlate))
            return ApiResult.Error("LicencePlate format must be like B-11-AAA or VN-123-AAA.")

        if (normalizedVin.length != 17)
            return ApiResult.Error("VIN must have exactly 17 characters.")

        if (trimmedBrand.length !in 2..50)
            return ApiResult.Error("Brand must be between 2 and 50 characters.")

        if (trimmedModel.length !in 3..50)
            return ApiResult.Error("Model must be between 3 and 50 characters.")

        val currentYear = java.time.Year.now().value

        val manufactureYearInt = manufactureYear.trim().toIntOrNull()
            ?: return ApiResult.Error("Manufacture year must be a number.")

        if (manufactureYearInt !in 1900..currentYear)
            return ApiResult.Error("ManufactureYear must be between 1900 and $currentYear")

        val maxWeightInt = maxWeight.trim().toIntOrNull()
            ?: return ApiResult.Error("Max weight must be a number.")

        if (maxWeightInt !in 1 .. 24000)
            return ApiResult.Error("MaxWeight must be between 1 and 24000")

        val maxVolumeInt = maxVolume.trim().toIntOrNull()
            ?: return ApiResult.Error("Max volume must be a number.")

        if (maxVolumeInt !in 1 .. 90)
            return ApiResult.Error("MaxVolume must be between 1 and 90")

        if (trimmedAdditionalInfo != null && trimmedAdditionalInfo.length > 250)
            return ApiResult.Error("Additional info needs to have maximum 250 characters")

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
}