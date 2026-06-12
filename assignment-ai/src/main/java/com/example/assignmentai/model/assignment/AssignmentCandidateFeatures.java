package com.example.assignmentai.model.assignment;

public record AssignmentCandidateFeatures(
        double roadDistanceToPickupKm,
        double cargoWeight,
        double cargoVolume,
        double weightUsageRatio,
        double volumeUsageRatio,
        double hasWeightCapacity,
        double hasVolumeCapacity,
        double keepsPreviousPrimaryVehicle,
        double keepsPreviousTrailer,
        double trailerRequired,
        double driverAvailable,
        double primaryVehicleAvailable,
        double trailerAvailable,
        double minutesUntilPickup,
        double driverCompletedTripsLast30Days
) {
    public double[] toArray() {
        return new double[] {
                roadDistanceToPickupKm,
                cargoWeight,
                cargoVolume,
                weightUsageRatio,
                volumeUsageRatio,
                hasWeightCapacity,
                hasVolumeCapacity,
                keepsPreviousPrimaryVehicle,
                keepsPreviousTrailer,
                trailerRequired,
                driverAvailable,
                primaryVehicleAvailable,
                trailerAvailable,
                minutesUntilPickup,
                driverCompletedTripsLast30Days
        };
    }
}