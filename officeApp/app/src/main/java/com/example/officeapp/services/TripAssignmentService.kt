package com.example.officeapp.services

import com.example.officeapp.models.tripAssignment.AvailableDriversResponse
import com.example.officeapp.models.tripAssignment.AvailableVehiclesResponse
import com.example.officeapp.repositories.TripAssignmentRepository
import com.example.officeapp.utils.ApiResult
import jakarta.inject.Inject

class TripAssignmentService @Inject constructor(
    private val tripAssignmentRepository: TripAssignmentRepository
) {
    suspend fun getAvailableDriversForTrip(
        tripId: Long
    ): ApiResult<AvailableDriversResponse> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        return tripAssignmentRepository.getAvailableDriversForTrip(tripId)
    }

    suspend fun getAvailableVehiclesForTrip(
        tripId: Long
    ): ApiResult<AvailableVehiclesResponse> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        return tripAssignmentRepository.getAvailableVehiclesForTrip(tripId)
    }
}