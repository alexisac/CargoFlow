package com.example.backendcargoflow.domain.vehicle.entity;

public interface AvailableVehicleProjection {
    Long getId();
    String getLicencePlate();
    VehicleType getVehicleType();
}
