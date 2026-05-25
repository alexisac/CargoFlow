package com.example.backendcargoflow.domain.trip.repository;

import com.example.backendcargoflow.domain.trip.entity.Trip;
import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    @Query("""
        SELECT t
        FROM Trip t
        JOIN TripAssignment ta ON ta.tripId = t.id
        WHERE ta.driverId = :driverId
          AND t.tripStatus = :tripStatus
        ORDER BY ta.assignedDate ASC
        LIMIT 1
        """)
    Optional<Trip> findOldestAssignedTripForDriver(
            @Param("driverId") Long driverId,
            @Param("tripStatus") TripStatus tripStatus
    );

    @Query("""
        SELECT t
        FROM Trip t
        JOIN TripAssignment ta ON ta.tripId = t.id
        WHERE ta.driverId = :driverId
          AND t.tripStatus = :tripStatus
        ORDER BY t.pickupInstant ASC
        LIMIT 1
        """)
    Optional<Trip> findCurrentInProgressTripForDriver(
            @Param("driverId") Long driverId,
            @Param("tripStatus") TripStatus tripStatus
    );

    @Query("""
        SELECT t
        FROM Trip t
        JOIN TripAssignment ta ON ta.tripId = t.id
        WHERE ta.driverId = :driverId
          AND t.tripStatus = :tripStatus
          AND t.deliveryInstant >= :fromInstant
        ORDER BY t.deliveryInstant DESC
        """)
    List<Trip> findCompletedTripsForDriverFromDate(
            @Param("driverId") Long driverId,
            @Param("tripStatus") TripStatus tripStatus,
            @Param("fromInstant") Instant fromInstant
    );
}
