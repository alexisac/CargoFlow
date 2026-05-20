package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TripInterfaceAPI {
    @Headers("Content-Type: application/json")
    @POST("/trip/create")
    suspend fun addNewTrip(
        @Body request: AddNewTripRequest
    ): Response<GenericApplicationResponse>
}