package com.example.backendcargoflow.domain.vehicle.entity;

public interface AssignmentAiAvailableVehicleProjection {
    Long getId();
    VehicleType getVehicleType();
    Integer getMaxWeight();
    Integer getMaxVolume();
}