package com.example.backendcargoflow.domain.vehicle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "vehicles")
@NoArgsConstructor
public class Vehicle {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "licence_plate", nullable = false, unique = true)
    private String licencePlate;

    @Column(name = "vin", nullable = false, unique = true)
    private String vin;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    private Integer manufactureYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "max_weight")
    private Integer maxWeight;

    @Column(name = "max_volume")
    private Integer maxVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_status", nullable = false)
    private VehicleStatus vehicleStatus;

    @Column(name = "additional_info")
    private String additionalInfo;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;
}
