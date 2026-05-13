package com.example.officeapp.features.addVehicle

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.parseApiError
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleInterfaceAPI: VehicleInterfaceAPI
) {
    suspend fun addNewVehicle(addNewVehicleRequest: AddNewVehicleRequest): ApiResult<GenericApplicationResponse> {
        return try {
            val response = vehicleInterfaceAPI.addNewVehicle(addNewVehicleRequest)

            if (response.isSuccessful) {
                val body = response.body()

                if(body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(message = ex.message ?: "Unknown error at AddNewVehicle.")
        }
    }
}