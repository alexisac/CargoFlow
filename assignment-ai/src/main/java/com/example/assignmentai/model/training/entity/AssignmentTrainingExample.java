package com.example.assignmentai.model.training.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "assignment_training_examples")
@NoArgsConstructor
public class AssignmentTrainingExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "primary_vehicle_id", nullable = false)
    private Long primaryVehicleId;

    @Column(name = "trailer_id")
    private Long trailerId;

    @Column(name = "road_distance_to_pickup_km", nullable = false)
    private Double roadDistanceToPickupKm;

    @Column(name = "cargo_weight")
    private Double cargoWeight;

    @Column(name = "cargo_volume")
    private Double cargoVolume;

    @Column(name = "weight_usage_ratio", nullable = false)
    private Double weightUsageRatio;

    @Column(name = "volume_usage_ratio", nullable = false)
    private Double volumeUsageRatio;

    @Column(name = "has_weight_capacity", nullable = false)
    private Double hasWeightCapacity;

    @Column(name = "has_volume_capacity", nullable = false)
    private Double hasVolumeCapacity;

    @Column(name = "keeps_previous_primary_vehicle", nullable = false)
    private Double keepsPreviousPrimaryVehicle;

    @Column(name = "keeps_previous_trailer", nullable = false)
    private Double keepsPreviousTrailer;

    @Column(name = "trailer_required", nullable = false)
    private Double trailerRequired;

    @Column(name = "driver_available", nullable = false)
    private Double driverAvailable;

    @Column(name = "primary_vehicle_available", nullable = false)
    private Double primaryVehicleAvailable;

    @Column(name = "trailer_available", nullable = false)
    private Double trailerAvailable;

    @Column(name = "minutes_until_pickup", nullable = false)
    private Double minutesUntilPickup;

    @Column(name = "driver_completed_trips_last_30_days", nullable = false)
    private Double driverCompletedTripsLast30Days;

    @Column(name = "was_selected", nullable = false)
    private Boolean wasSelected;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private AssignmentTrainingExampleSource source;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}