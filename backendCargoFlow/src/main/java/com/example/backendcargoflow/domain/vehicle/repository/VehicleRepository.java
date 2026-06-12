package com.example.backendcargoflow.domain.vehicle.repository;

import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.vehicle.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicencePlateOrVin(String licencePlate, String vin);

    @Query("""
        SELECT v.id AS id,
               v.licencePlate AS licencePlate,
               v.vehicleType AS vehicleType
        FROM Vehicle v
        WHERE v.vehicleStatus = :vehicleStatus
          AND v.vehicleType IN :vehicleTypes
          AND NOT EXISTS (
              SELECT 1
              FROM TripAssignment ta
              JOIN Trip t ON t.id = ta.tripId
              WHERE t.tripStatus IN :blockingTripStatuses
                AND t.pickupInstant < :newTripEndInstant
                AND t.deliveryInstant > :newTripStartInstant
                AND (
                    ta.primaryVehicleId = v.id
                    OR ta.trailerVehicleId = v.id
                )
          )
        ORDER BY v.vehicleType, v.licencePlate
        """)
    Page<AvailableVehicleProjection> findAvailableVehiclesForTrip(
            @Param("vehicleStatus") VehicleStatus vehicleStatus,
            @Param("vehicleTypes") List<VehicleType> vehicleTypes,
            @Param("blockingTripStatuses") List<TripStatus> blockingTripStatuses,
            @Param("newTripStartInstant") Instant newTripStartInstant,
            @Param("newTripEndInstant") Instant newTripEndInstant,
            Pageable pageable
    );

    @Query("""
    SELECT v.id AS id,
           v.vehicleType AS vehicleType,
           v.maxWeight AS maxWeight,
           v.maxVolume AS maxVolume
    FROM Vehicle v
    WHERE v.vehicleStatus = :vehicleStatus
      AND v.vehicleType IN :vehicleTypes
      AND NOT EXISTS (
          SELECT 1
          FROM TripAssignment ta
          JOIN Trip t ON t.id = ta.tripId
          WHERE t.tripStatus IN :blockingTripStatuses
            AND t.pickupInstant < :newTripEndInstant
            AND t.deliveryInstant > :newTripStartInstant
            AND (
                ta.primaryVehicleId = v.id
                OR ta.trailerVehicleId = v.id
            )
      )
    ORDER BY v.vehicleType, v.id
    """)
    List<AssignmentAiAvailableVehicleProjection> findAssignmentAiAvailableVehiclesForTrip(
            @Param("vehicleStatus") VehicleStatus vehicleStatus,
            @Param("vehicleTypes") List<VehicleType> vehicleTypes,
            @Param("blockingTripStatuses") List<TripStatus> blockingTripStatuses,
            @Param("newTripStartInstant") Instant newTripStartInstant,
            @Param("newTripEndInstant") Instant newTripEndInstant
    );

    Page<Vehicle> findAllByOrderByCreateDateDesc(Pageable pageable);
}
