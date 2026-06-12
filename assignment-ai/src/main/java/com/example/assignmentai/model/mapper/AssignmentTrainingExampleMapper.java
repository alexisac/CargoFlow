package com.example.assignmentai.model.mapper;

import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExample;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExampleSource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AssignmentTrainingExampleMapper {

    default AssignmentTrainingExample mapToAssignmentTrainingExample(
            AssignmentCandidate candidate,
            AssignmentCandidateFeatures features,
            Boolean wasSelected,
            AssignmentTrainingExampleSource source
    ) {
        AssignmentTrainingExample trainingExample = new AssignmentTrainingExample();

        trainingExample.setTripId(candidate.tripId());
        trainingExample.setDriverId(candidate.driverId());
        trainingExample.setPrimaryVehicleId(candidate.primaryVehicleId());
        trainingExample.setTrailerId(candidate.trailerId());

        trainingExample.setRoadDistanceToPickupKm(features.roadDistanceToPickupKm());
        trainingExample.setCargoWeight(features.cargoWeight());
        trainingExample.setCargoVolume(features.cargoVolume());
        trainingExample.setWeightUsageRatio(features.weightUsageRatio());
        trainingExample.setVolumeUsageRatio(features.volumeUsageRatio());
        trainingExample.setHasWeightCapacity(features.hasWeightCapacity());
        trainingExample.setHasVolumeCapacity(features.hasVolumeCapacity());
        trainingExample.setKeepsPreviousPrimaryVehicle(features.keepsPreviousPrimaryVehicle());
        trainingExample.setKeepsPreviousTrailer(features.keepsPreviousTrailer());
        trainingExample.setTrailerRequired(features.trailerRequired());
        trainingExample.setDriverAvailable(features.driverAvailable());
        trainingExample.setPrimaryVehicleAvailable(features.primaryVehicleAvailable());
        trainingExample.setTrailerAvailable(features.trailerAvailable());
        trainingExample.setMinutesUntilPickup(features.minutesUntilPickup());
        trainingExample.setDriverCompletedTripsLast30Days(features.driverCompletedTripsLast30Days());

        trainingExample.setWasSelected(wasSelected);
        trainingExample.setSource(source);
        trainingExample.setCreatedAt(LocalDateTime.now());

        return trainingExample;
    }
}