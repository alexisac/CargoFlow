package com.example.assignmentai.model.assignment;

public record OptimalAssignmentResult(
        Long tripId,
        Long driverId,
        Long primaryVehicleId,
        Long trailerId,
        double confidence,
        String modelType
) {}