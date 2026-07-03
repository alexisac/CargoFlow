package com.example.officeapp.services

import com.example.officeapp.models.assignmentAi.OptimalAssignmentResponseDto
import com.example.officeapp.repositories.AssignmentAiRepository
import com.example.officeapp.utils.ApiResult
import javax.inject.Inject

class AssignmentAiService @Inject constructor(
    private val assignmentAiRepository: AssignmentAiRepository
) {

    suspend fun autoOptimizeTripAssignment(
        tripId: Long
    ): ApiResult<OptimalAssignmentResponseDto> {
        if (tripId <= 0)
            return ApiResult.Error(ValidationMessages.ID_RANGE)

        return assignmentAiRepository.autoOptimizeTripAssignment(tripId)
    }
}