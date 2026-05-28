package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import com.example.officeapp.models.vehicle.ChangeVehicleStatusRequest
import com.example.officeapp.models.vehicle.GetAllVehiclesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleInterfaceAPI {
    @Headers("Content-Type: application/json")
    @POST("/vehicle/create")
    suspend fun addNewVehicle(
        @Body request: AddNewVehicleRequest
    ): Response<GenericApplicationResponse>

    @Headers("Content-Type: application/json")
    @GET("vehicle/getAll")
    suspend fun getAllVehicles(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<GetAllVehiclesResponse>

    @Headers("Content-Type: application/json")
    @PATCH("vehicle/{vehicleId}/changeStatus")
    suspend fun changeVehicleStatus(
        @Path("vehicleId") vehicleId: Long,
        @Body request: ChangeVehicleStatusRequest
    ): Response<GenericApplicationResponse>
}