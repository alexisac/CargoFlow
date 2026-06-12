package com.example.assignmentai.service;

import com.example.assignmentai.controller.assignmentai.models.TrainAssignmentModelResponseDto;
import com.example.assignmentai.service.model.AssignmentRandomForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentModelTrainingService {
    private final AssignmentRandomForestService assignmentRandomForestService;

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public TrainAssignmentModelResponseDto trainAssignmentModel() {
        int trainingExamplesCount = assignmentRandomForestService.trainModel();

        return new TrainAssignmentModelResponseDto()
                .trained(true)
                .trainingExamplesCount(trainingExamplesCount)
                .modelType("RANDOM_FOREST_CLASSIFIER")
                .message("Random Forest model was trained successfully");
    }
}