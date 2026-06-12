package com.example.assignmentai.service;

import com.example.assignmentai.common.exceptions.BadRequestException;
import com.example.assignmentai.controller.assignmentai.models.AssignmentFeedbackRequestDto;
import com.example.assignmentai.controller.common.models.GenericApplicationResponseDto;
import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.assignment.EnrichedAssignmentCandidate;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExample;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExampleSource;
import com.example.assignmentai.model.mapper.AssignmentAiMapper;
import com.example.assignmentai.model.mapper.AssignmentTrainingExampleMapper;
import com.example.assignmentai.model.training.repository.AssignmentTrainingExampleRepository;
import com.example.assignmentai.service.candidate.CandidatePreselectionService;
import com.example.assignmentai.service.feature.AssignmentFeatureExtractor;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentFeedbackService {
    private final AssignmentAiMapper assignmentAiMapper;
    private final CandidatePreselectionService candidatePreselectionService;
    private final AssignmentFeatureExtractor assignmentFeatureExtractor;
    private final AssignmentTrainingExampleMapper assignmentTrainingExampleMapper;
    private final AssignmentTrainingExampleRepository assignmentTrainingExampleRepository;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public GenericApplicationResponseDto saveAssignmentFeedback(
            AssignmentFeedbackRequestDto assignmentFeedbackRequestDto
    ) {
        validateFeedbackRequest(assignmentFeedbackRequestDto);

        List<AssignmentCandidate> candidates = assignmentAiMapper.mapAssignmentCandidateDtosToAssignmentCandidates(assignmentFeedbackRequestDto.getEvaluatedCandidates());

        List<EnrichedAssignmentCandidate> enrichedCandidates = candidatePreselectionService.preselectAndEnrichCandidates(candidates);

        List<AssignmentTrainingExample> trainingExamples = enrichedCandidates.stream()
                .map(enrichedCandidate -> {
                    AssignmentCandidate candidate = enrichedCandidate.candidate();

                    AssignmentCandidateFeatures features = assignmentFeatureExtractor.extractFeatures(enrichedCandidate);

                    boolean wasSelected = isSelectedCandidate(candidate, assignmentFeedbackRequestDto);

                    return assignmentTrainingExampleMapper.mapToAssignmentTrainingExample(
                            candidate,
                            features,
                            wasSelected,
                            AssignmentTrainingExampleSource.DISPATCHER
                    );
                })
                .toList();

        assignmentTrainingExampleRepository.saveAll(trainingExamples);

        return GenericApplicationResponseFactory.success(
                "200 - ASSIGNMENT_FEEDBACK_SAVED",
                "Assignment feedback was saved successfully"
        );
    }

    private void validateFeedbackRequest(AssignmentFeedbackRequestDto requestDto) {
        if (requestDto == null) {
            throw new BadRequestException("Feedback request is required");
        }

        if (requestDto.getSelectedDriverId() == null) {
            throw new BadRequestException("Selected driver id is required");
        }

        if (requestDto.getSelectedPrimaryVehicleId() == null) {
            throw new BadRequestException("Selected primary vehicle id is required");
        }

        if (requestDto.getEvaluatedCandidates() == null || requestDto.getEvaluatedCandidates().isEmpty()) {
            throw new BadRequestException("Evaluated candidates are required");
        }
    }

    private boolean isSelectedCandidate(AssignmentCandidate candidate, AssignmentFeedbackRequestDto requestDto) {
        Long selectedTrailerId = map(requestDto.getSelectedTrailerId());
        return candidate.driverId().equals(requestDto.getSelectedDriverId())
                && candidate.primaryVehicleId().equals(requestDto.getSelectedPrimaryVehicleId())
                && areTrailerIdsEqual(candidate.trailerId(), selectedTrailerId);
    }

    private boolean areTrailerIdsEqual(Long candidateTrailerId, Long selectedTrailerId) {
        if (candidateTrailerId == null && selectedTrailerId == null) {
            return true;
        }

        if (candidateTrailerId == null || selectedTrailerId == null) {
            return false;
        }

        return candidateTrailerId.equals(selectedTrailerId);
    }

    private <T> T map(JsonNullable<T> value) {
        return value == null || !value.isPresent() ? null : value.get();
    }
}