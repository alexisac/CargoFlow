package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import com.example.officeapp.models.trip.TripPageResponse
import com.example.officeapp.models.trip.TripSearchRequest
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

    @Headers("Content-Type: application/json")
    @POST("trip/search")
    suspend fun searchTrips(
        @Body request: TripSearchRequest
    ): Response<TripPageResponse>
}