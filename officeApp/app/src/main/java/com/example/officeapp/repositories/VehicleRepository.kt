package com.example.officeapp.repositories

import com.example.officeapp.interfacesAPI.VehicleInterfaceAPI
import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import com.example.officeapp.models.vehicle.ChangeVehicleStatusRequest
import com.example.officeapp.models.vehicle.GetAllVehiclesResponse
import com.example.officeapp.models.vehicle.VehicleDashboardSummaryResponse
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

    suspend fun getAllVehicles(
        pageNumber: Int,
        pageSize: Int
    ): ApiResult<GetAllVehiclesResponse> {
        return try {
            val response = vehicleInterfaceAPI.getAllVehicles(
                pageNumber = pageNumber,
                pageSize = pageSize
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex.message ?: "Unexpected error occurred.")
        }
    }

    suspend fun changeVehicleStatus(
        vehicleId: Long,
        changeVehicleStatusRequest: ChangeVehicleStatusRequest
    ): ApiResult<GenericApplicationResponse> {
        return try {
            val response = vehicleInterfaceAPI.changeVehicleStatus(
                vehicleId = vehicleId,
                request = changeVehicleStatusRequest
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex.message ?: "Unexpected error occurred.")
        }
    }

    suspend fun getVehicleDashboardSummary(): ApiResult<VehicleDashboardSummaryResponse> {
        return try {
            val response = vehicleInterfaceAPI.getVehicleDashboardSummary()

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex.message ?: "Could not load vehicle dashboard summary.")
        }
    }
}