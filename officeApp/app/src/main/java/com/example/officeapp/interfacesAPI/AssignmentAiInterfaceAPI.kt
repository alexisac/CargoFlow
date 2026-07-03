package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.assignmentAi.OptimalAssignmentResponseDto
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AssignmentAiInterfaceAPI {

    @Headers("Content-Type: application/json")
    @POST("assignment-ai/trips/{tripId}/auto-optimize")
    suspend fun autoOptimizeTripAssignment(
        @Path("tripId") tripId: Long
    ): Response<OptimalAssignmentResponseDto>
}