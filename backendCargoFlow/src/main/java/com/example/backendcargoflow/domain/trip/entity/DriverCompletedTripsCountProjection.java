package com.example.backendcargoflow.domain.trip.entity;

public interface DriverCompletedTripsCountProjection {
    Long getDriverId();
    Integer getCompletedTripsCount();
}