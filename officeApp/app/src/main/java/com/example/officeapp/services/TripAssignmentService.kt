package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.tripAssignment.AssignTripRequest
import com.example.officeapp.models.tripAssignment.AvailableDriversResponse
import com.example.officeapp.models.tripAssignment.AvailableVehiclesResponse
import com.example.officeapp.repositories.TripAssignmentRepository
import com.example.officeapp.utils.ApiResult
import jakarta.inject.Inject

class TripAssignmentService @Inject constructor(
    private val tripAssignmentRepository: TripAssignmentRepository
) {
    suspend fun getAvailableDriversForTrip(
        tripId: Long,
        pageNumber: Int,
        pageSize: Int
    ): ApiResult<AvailableDriversResponse> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)
        if (pageNumber < 0)
            return ApiResult.Error(ValidationMessages.PAGE_NUMBER_RANGE)
        if (pageSize < 0)
            return ApiResult.Error(ValidationMessages.PAGE_SIZE_RANGE)

        return tripAssignmentRepository.getAvailableDriversForTrip(
            tripId = tripId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    suspend fun getAvailablePrimaryVehiclesForTrip(
        tripId: Long,
        pageNumber: Int,
        pageSize: Int
    )
    : ApiResult<AvailableVehiclesResponse> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)
        if (pageNumber < 0)
            return ApiResult.Error(ValidationMessages.PAGE_NUMBER_RANGE)
        if (pageSize < 0)
            return ApiResult.Error(ValidationMessages.PAGE_SIZE_RANGE)

        return tripAssignmentRepository.getAvailablePrimaryVehiclesForTrip(
            tripId = tripId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    suspend fun getAvailableTrailersForTrip(
        tripId: Long,
        pageNumber: Int,
        pageSize: Int
    )
            : ApiResult<AvailableVehiclesResponse> {
        if (tripId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)
        if (pageNumber < 0)
            return ApiResult.Error(ValidationMessages.PAGE_NUMBER_RANGE)
        if (pageSize < 0)
            return ApiResult.Error(ValidationMessages.PAGE_SIZE_RANGE)

        return tripAssignmentRepository.getAvailableTrailersForTrip(
            tripId = tripId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    suspend fun assignTrip(
        tripId: Long,
        driverId: Long,
        primaryVehicleId: Long,
        trailerVehicleId: Long?
    ): ApiResult<GenericApplicationResponse> {
        if (tripId < 0 || driverId < 0 || primaryVehicleId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        if (trailerVehicleId != null && trailerVehicleId < 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        val request = AssignTripRequest(
            tripId = tripId,
            driverId = driverId,
            primaryVehicleId = primaryVehicleId,
            trailerVehicleId = trailerVehicleId
        )

        return tripAssignmentRepository.assignTrip(request)
    }
}