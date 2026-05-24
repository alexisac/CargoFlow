package com.example.officeapp.repositories

import com.example.officeapp.interfacesAPI.TripAssignmentInterfaceAPI
import com.example.officeapp.models.tripAssignment.AvailableDriversResponse
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.parseApiError
import javax.inject.Inject

class TripAssignmentRepository @Inject constructor(
    private val tripAssignmentInterfaceAPI: TripAssignmentInterfaceAPI,
) {
    suspend fun getAvailableDriversForTrip(tripId: Long): ApiResult<AvailableDriversResponse> {
        return try {
            val response = tripAssignmentInterfaceAPI.getAvailableDriversForTrip(tripId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null) {
                    ApiResult.Error("Empty response from server.")
                } else {
                    ApiResult.Success(body)
                }
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex.message ?: "Unexpected error occurred.")
        }
    }
}