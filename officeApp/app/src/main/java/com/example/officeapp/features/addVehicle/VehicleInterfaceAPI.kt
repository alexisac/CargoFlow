package com.example.officeapp.features.addVehicle

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.vehicle.AddNewVehicleRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VehicleInterfaceAPI {
    @Headers("Content-Type: application/json")
    @POST("/vehicle/create")
    suspend fun addNewVehicle(
        @Body request: AddNewVehicleRequest
    ): Response<GenericApplicationResponse>
}