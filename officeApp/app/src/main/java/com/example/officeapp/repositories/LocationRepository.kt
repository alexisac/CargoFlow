package com.example.officeapp.repositories

import com.example.officeapp.interfacesAPI.LocationInterfaceAPI
import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.location.UpdateDriverLocationRequest
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.parseApiError
import jakarta.inject.Inject

class LocationRepository @Inject constructor(
    private val locationInterfaceAPI: LocationInterfaceAPI
) {
    suspend fun updateMyLocation(
        updateDriverLocationRequest : UpdateDriverLocationRequest
    ): ApiResult<GenericApplicationResponse> {
        return try {
            val response = locationInterfaceAPI.updateMyLocation(updateDriverLocationRequest)

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
            ApiResult.Error(ex.message ?: "Unexpected error while updating driver location.")
        }
    }
}