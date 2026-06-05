package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.location.UpdateDriverLocationRequest
import com.example.officeapp.repositories.LocationRepository
import com.example.officeapp.utils.ApiResult
import jakarta.inject.Inject
import java.time.Instant

class LocationService @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun updateMyLocation(
        latitude: Double,
        longitude: Double
    ): ApiResult<GenericApplicationResponse> {
        val request = UpdateDriverLocationRequest(
            latitude = latitude,
            longitude = longitude,
            updatedAt = Instant.now().toString()
        )
        return locationRepository.updateMyLocation(request)
    }
}