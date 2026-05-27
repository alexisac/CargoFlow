package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.tripAssignment.AssignTripRequest
import com.example.officeapp.models.tripAssignment.AvailableDriversResponse
import com.example.officeapp.models.tripAssignment.AvailableVehiclesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TripAssignmentInterfaceAPI {
    @Headers("Content-Type: application/json")
    @GET("tripAssignment/{tripId}/availableDrivers")
    suspend fun getAvailableDriversForTrip(
        @Path("tripId") tripId: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<AvailableDriversResponse>

    @Headers("Content-Type: application/json")
    @GET("tripAssignment/{tripId}/availablePrimaryVehicles")
    suspend fun getAvailablePrimaryVehiclesForTrip(
        @Path("tripId") tripId: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<AvailableVehiclesResponse>

    @Headers("Content-Type: application/json")
    @GET("tripAssignment/{tripId}/availableTrailers")
    suspend fun getAvailableTrailersForTrip(
        @Path("tripId") tripId: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<AvailableVehiclesResponse>

    @Headers("Content-Type: application/json")
    @POST("tripAssignment/assign")
    suspend fun assignTrip(
        @Body request: AssignTripRequest
    ): Response<GenericApplicationResponse>
}