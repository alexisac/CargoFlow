package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.location.GetLatestDriverLocationsResponse
import com.example.officeapp.models.location.UpdateDriverLocationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface LocationInterfaceAPI {
    @Headers("Content-Type: application/json")
    @PUT("locations/me")
    suspend fun updateMyLocation(
        @Body request: UpdateDriverLocationRequest
    ): Response<GenericApplicationResponse>

    @Headers("Content-Type: application/json")
    @GET("locations/drivers/latest")
    suspend fun getLatestDriverLocations(): Response<GetLatestDriverLocationsResponse>
}