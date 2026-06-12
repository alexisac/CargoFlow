package com.example.assignmentai.model.assignment;

public record DriverDistanceEstimate(
        Long driverId,
        Double driverLatitude,
        Double driverLongitude,
        double airDistanceToPickupKm
) {}