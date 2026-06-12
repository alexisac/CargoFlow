package com.example.assignmentai.service.feature;

import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.assignment.EnrichedAssignmentCandidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentFeatureExtractor {
    private final VehicleCapacityFeatureService vehicleCapacityFeatureService;

    public AssignmentCandidateFeatures extractFeatures(EnrichedAssignmentCandidate enrichedAssignmentCandidate) {
        AssignmentCandidate candidate = enrichedAssignmentCandidate.candidate();

        return new AssignmentCandidateFeatures(
                enrichedAssignmentCandidate.roadDistanceToPickupKm(),
                nullToZero(candidate.cargoWeight()),
                nullToZero(candidate.cargoVolume()),
                vehicleCapacityFeatureService.calculateWeightUsageRatio(candidate),
                vehicleCapacityFeatureService.calculateVolumeUsageRatio(candidate),
                vehicleCapacityFeatureService.hasWeightCapacityFeature(candidate),
                vehicleCapacityFeatureService.hasVolumeCapacityFeature(candidate),
                boolToDouble(candidate.keepsPreviousPrimaryVehicle()),
                boolToDouble(candidate.keepsPreviousTrailer()),
                boolToDouble(candidate.trailerRequired()),
                boolToDouble(candidate.driverAvailable()),
                boolToDouble(candidate.primaryVehicleAvailable()),
                boolToDouble(candidate.trailerAvailable()),
                candidate.minutesUntilPickup() == null ? 0.0 : candidate.minutesUntilPickup(),
                candidate.driverCompletedTripsLast30Days() == null ? 0.0 : candidate.driverCompletedTripsLast30Days()
        );
    }

    private double boolToDouble(Boolean value) {
        return Boolean.TRUE.equals(value) ? 1.0 : 0.0;
    }

    private double nullToZero(Double value) {
        return value == null ? 0.0 : value;
    }
}