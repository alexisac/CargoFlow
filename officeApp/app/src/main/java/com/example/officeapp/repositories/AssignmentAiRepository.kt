package com.example.officeapp.repositories

import com.example.officeapp.interfacesAPI.AssignmentAiInterfaceAPI
import com.example.officeapp.models.assignmentAi.OptimalAssignmentResponseDto
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.parseApiError
import javax.inject.Inject


class AssignmentAiRepository @Inject constructor(
    private val assignmentAiInterfaceAPI: AssignmentAiInterfaceAPI
) {

    suspend fun autoOptimizeTripAssignment(
        tripId: Long
    ): ApiResult<OptimalAssignmentResponseDto> {
        return try {
            val response = assignmentAiInterfaceAPI.autoOptimizeTripAssignment(tripId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null) {
                    ApiResult.Error("Empty response from server.")
                } else {
                    ApiResult.Success(body)
                }
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex.message ?: "Could not generate AI assignment recommendation.")
        }
    }
}