package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.tripAssignment.AvailableDriversResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TripAssignmentInterfaceAPI {
    @Headers("Content-Type: application/json")
    @GET("tripAssignment/{tripId}/available-drivers")
    suspend fun getAvailableDriversForTrip(
        @Path("tripId") tripId: Long
    ): Response<AvailableDriversResponse>
}