package com.example.backendcargoflow.domain.tripAssignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trip_assignments")
@NoArgsConstructor
public class TripAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "primary_vehicle_id", nullable = false)
    private Long primaryVehicleId;

    @Column(name = "trailer_vehicle_id")
    private Long trailerVehicleId;

    @Column(name = "assigned_by_user_id", nullable = false)
    private Long assignedByUserId;

    @CreationTimestamp
    @Column(name = "assigned_date", nullable = false, updatable = false)
    private LocalDateTime assignedDate;
}
