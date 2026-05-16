package com.example.officeapp.features.addTrip

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.parseApiError
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val tripInterfaceAPI: TripInterfaceAPI
){
    suspend fun addNewTrip(addNewTripRequest: AddNewTripRequest): ApiResult<GenericApplicationResponse> {
        return try {
            val response = tripInterfaceAPI.addNewTrip(addNewTripRequest)

            if(response.isSuccessful) {
                val body = response.body()

                if(body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(message = ex.message ?: "Unknown error at AddNewTrip.")
        }
    }
}