package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.trip.AddNewTripRequest
import com.example.officeapp.models.trip.CompletedTripsResponse
import com.example.officeapp.models.trip.CurrentTrip
import com.example.officeapp.models.trip.Trip
import com.example.officeapp.models.trip.TripPageResponse
import com.example.officeapp.models.trip.TripSearchRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Headers("Content-Type: application/json")
    @GET("trip/{id}")
    suspend fun getTrip(
        @Path("id") id: Long
    ): Response<Trip>

    @Headers("Content-Type: application/json")
    @GET("trip/currentTrip")
    suspend fun getCurrentTrip(): Response<CurrentTrip>

    @Headers("Content-Type: application/json")
    @GET("trip/completedTrips")
    suspend fun getCompletedTrips(
        @Query("days") days: Int
    ): Response<CompletedTripsResponse>
}