package com.example.assignmentai.controller;

import com.example.assignmentai.common.LogMessage;
import com.example.assignmentai.controller.assignmentai.api.AssignmentAiApi;
import com.example.assignmentai.controller.assignmentai.models.AssignmentFeedbackRequestDto;
import com.example.assignmentai.controller.assignmentai.models.OptimalAssignmentResponseDto;
import com.example.assignmentai.controller.assignmentai.models.OptimizeAssignmentRequestDto;
import com.example.assignmentai.controller.assignmentai.models.TrainAssignmentModelResponseDto;
import com.example.assignmentai.controller.common.models.GenericApplicationResponseDto;
import com.example.assignmentai.service.AssignmentFeedbackService;
import com.example.assignmentai.service.AssignmentModelTrainingService;
import com.example.assignmentai.service.AssignmentOptimizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssignmentAiController implements AssignmentAiApi {
    private final AssignmentOptimizationService assignmentOptimizationService;
    private final AssignmentFeedbackService assignmentFeedbackService;
    private final AssignmentModelTrainingService assignmentModelTrainingService;

    @Override
    public OptimalAssignmentResponseDto optimizeTripAssignment(Long tripId, @RequestBody OptimizeAssignmentRequestDto optimizeAssignmentRequestDto) {
        Integer candidatesCount = optimizeAssignmentRequestDto.getCandidates() == null
                ? 0 : optimizeAssignmentRequestDto.getCandidates().size();

        log.info(String.format(LogMessage.OPTIMIZE_TRIP_ASSIGNMENT, tripId, candidatesCount));

        return assignmentOptimizationService.optimizeTripAssignment(tripId, optimizeAssignmentRequestDto);
    }

    @Override
    public GenericApplicationResponseDto saveAssignmentFeedback(Long tripId, @RequestBody AssignmentFeedbackRequestDto assignmentFeedbackRequestDto) {
        Integer candidatesCount = assignmentFeedbackRequestDto.getEvaluatedCandidates() == null
                ? 0 : assignmentFeedbackRequestDto.getEvaluatedCandidates().size();

        log.info(String.format(LogMessage.SAVE_ASSIGNMENT_FEEDBACK,
                tripId,
                assignmentFeedbackRequestDto.getSelectedDriverId(),
                assignmentFeedbackRequestDto.getSelectedPrimaryVehicleId(),
                assignmentFeedbackRequestDto.getSelectedTrailerId(),
                candidatesCount
        ));

        return assignmentFeedbackService.saveAssignmentFeedback(assignmentFeedbackRequestDto);
    }

    @Override
    public TrainAssignmentModelResponseDto trainAssignmentModel() {
        log.info(LogMessage.TRAIN_ASSIGNMENT_MODEL);
        return assignmentModelTrainingService.trainAssignmentModel();
    }

    @Override
    public OptimalAssignmentResponseDto autoOptimizeTripAssignment(Long tripId) {
        log.info(String.format(LogMessage.AUTO_OPTIMIZE_TRIP_ASSIGNMENT, tripId));
        return assignmentOptimizationService.autoOptimizeTripAssignment(tripId);
    }
}