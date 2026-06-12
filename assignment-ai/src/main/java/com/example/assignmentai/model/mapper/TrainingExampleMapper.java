package com.example.assignmentai.model.mapper;

import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.training.TrainingExample;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExample;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TrainingExampleMapper {

    default TrainingExample mapAssignmentTrainingExampleToTrainingExample(
            AssignmentTrainingExample assignmentTrainingExample
    ) {
        AssignmentCandidateFeatures features = new AssignmentCandidateFeatures(
                assignmentTrainingExample.getRoadDistanceToPickupKm(),
                nullToZero(assignmentTrainingExample.getCargoWeight()),
                nullToZero(assignmentTrainingExample.getCargoVolume()),
                assignmentTrainingExample.getWeightUsageRatio(),
                assignmentTrainingExample.getVolumeUsageRatio(),
                assignmentTrainingExample.getHasWeightCapacity(),
                assignmentTrainingExample.getHasVolumeCapacity(),
                assignmentTrainingExample.getKeepsPreviousPrimaryVehicle(),
                assignmentTrainingExample.getKeepsPreviousTrailer(),
                assignmentTrainingExample.getTrailerRequired(),
                assignmentTrainingExample.getDriverAvailable(),
                assignmentTrainingExample.getPrimaryVehicleAvailable(),
                assignmentTrainingExample.getTrailerAvailable(),
                assignmentTrainingExample.getMinutesUntilPickup(),
                assignmentTrainingExample.getDriverCompletedTripsLast30Days()
        );

        return new TrainingExample(features, assignmentTrainingExample.getWasSelected());
    }

    List<TrainingExample> mapAssignmentTrainingExamplesToTrainingExamples(List<AssignmentTrainingExample> assignmentTrainingExamples);

    private double nullToZero(Double value) {
        return value == null ? 0.0 : value;
    }
}