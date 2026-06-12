package com.example.assignmentai.model.assignment;

public record AssignmentCandidate(
        Long tripId,
        Long driverId,
        Long primaryVehicleId,
        Long trailerId,

        Double driverLatitude,
        Double driverLongitude,
        AddressData pickupAddress,

        Double cargoWeight,
        Double cargoVolume,

        VehicleTypeData primaryVehicleType,
        Double primaryVehicleMaxWeight,
        Double primaryVehicleMaxVolume,

        VehicleTypeData trailerType,
        Double trailerMaxWeight,
        Double trailerMaxVolume,

        Boolean keepsPreviousPrimaryVehicle,
        Boolean keepsPreviousTrailer,
        Boolean trailerRequired,
        Boolean driverAvailable,
        Boolean primaryVehicleAvailable,
        Boolean trailerAvailable,

        Integer minutesUntilPickup,
        Integer driverCompletedTripsLast30Days
) {}