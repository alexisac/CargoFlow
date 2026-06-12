package com.example.assignmentai.service;

import com.example.assignmentai.service.client.CargoCoreAssignmentAiClient;
import com.example.assignmentai.service.client.LocationServiceClient;
import com.example.assignmentai.controller.assignmentai.models.OptimalAssignmentResponseDto;
import com.example.assignmentai.controller.assignmentai.models.OptimizeAssignmentRequestDto;
import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreAssignmentContextResponseDto;
import com.example.assignmentai.model.location.LatestDriverLocationsResponse;
import com.example.assignmentai.model.mapper.AssignmentAiMapper;
import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.assignment.EnrichedAssignmentCandidate;
import com.example.assignmentai.model.assignment.OptimalAssignmentResult;
import com.example.assignmentai.model.assignment.ScoredAssignmentCandidate;
import com.example.assignmentai.service.candidate.CandidateGenerationService;
import com.example.assignmentai.service.candidate.CandidatePreselectionService;
import com.example.assignmentai.service.feature.AssignmentFeatureExtractor;
import com.example.assignmentai.service.feature.VehicleCapacityFeatureService;
import com.example.assignmentai.service.model.AssignmentRandomForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentOptimizationService {
    private final AssignmentFeatureExtractor assignmentFeatureExtractor;
    private final AssignmentRandomForestService assignmentRandomForestService;
    private final VehicleCapacityFeatureService vehicleCapacityFeatureService;
    private final CandidatePreselectionService candidatePreselectionService;
    private final AssignmentAiMapper assignmentAiMapper;
    private final CargoCoreAssignmentAiClient cargoCoreAssignmentAiClient;
    private final LocationServiceClient locationServiceClient;
    private final CandidateGenerationService candidateGenerationService;

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public OptimalAssignmentResponseDto optimizeTripAssignment(Long tripId, OptimizeAssignmentRequestDto optimizeAssignmentRequestDto) {
        List<AssignmentCandidate> candidates = assignmentAiMapper.mapAssignmentCandidateDtosToAssignmentCandidates(optimizeAssignmentRequestDto.getCandidates());
        OptimalAssignmentResult result = findOptimalAssignment(tripId, candidates);
        return assignmentAiMapper.mapOptimalAssignmentResultToOptimalAssignmentResponseDto(result);
    }

    @PreAuthorize("hasAnyRole('DISPATCHER', 'MANAGER', 'ADMIN')")
    public OptimalAssignmentResponseDto autoOptimizeTripAssignment(Long tripId) {
        CargoCoreAssignmentContextResponseDto context = cargoCoreAssignmentAiClient.getTripAssignmentContext(tripId);
        LatestDriverLocationsResponse latestDriverLocationsResponse = locationServiceClient.getLatestDriverLocations();
        List<AssignmentCandidate> candidates = candidateGenerationService.generateCandidates(context, latestDriverLocationsResponse);
        OptimalAssignmentResult result = findOptimalAssignment(tripId, candidates);
        return assignmentAiMapper.mapOptimalAssignmentResultToOptimalAssignmentResponseDto(result);
    }

    public OptimalAssignmentResult findOptimalAssignment(Long tripId, List<AssignmentCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("No assignment candidates available.");
        }

        List<AssignmentCandidate> validCandidates = candidates.stream()
                .filter(this::isHardConstraintValid)
                .toList();

        if (validCandidates.isEmpty()) {
            throw new IllegalArgumentException("No valid assignment candidates available.");
        }

        List<EnrichedAssignmentCandidate> enrichedCandidates = candidatePreselectionService.preselectAndEnrichCandidates(validCandidates);

        if (enrichedCandidates.isEmpty()) {
            throw new IllegalArgumentException("No enriched assignment candidates available.");
        }

        List<ScoredAssignmentCandidate> scoredCandidates = enrichedCandidates.stream()
                .map(enrichedCandidate -> {
                    AssignmentCandidateFeatures features = assignmentFeatureExtractor.extractFeatures(enrichedCandidate);
                    double probability = assignmentRandomForestService.predictProbabilitySelected(features);
                    return new ScoredAssignmentCandidate(enrichedCandidate, probability);
                })
                .sorted(Comparator.comparing(ScoredAssignmentCandidate::probabilitySelected).reversed())
                .toList();

        ScoredAssignmentCandidate bestScoredCandidate = scoredCandidates.getFirst();

        AssignmentCandidate bestCandidate = bestScoredCandidate.enrichedAssignmentCandidate().candidate();

        return new OptimalAssignmentResult(
                tripId,
                bestCandidate.driverId(),
                bestCandidate.primaryVehicleId(),
                bestCandidate.trailerId(),
                bestScoredCandidate.probabilitySelected(),
                "RANDOM_FOREST_CLASSIFIER"
        );
    }

    private boolean isHardConstraintValid(AssignmentCandidate candidate) {
        if (!Boolean.TRUE.equals(candidate.driverAvailable())) {
            return false;
        }

        if (!Boolean.TRUE.equals(candidate.primaryVehicleAvailable())) {
            return false;
        }

        if (Boolean.TRUE.equals(candidate.trailerRequired()) && !Boolean.TRUE.equals(candidate.trailerAvailable())) {
            return false;
        }

        return vehicleCapacityFeatureService.isCapacityValid(candidate);
    }
}