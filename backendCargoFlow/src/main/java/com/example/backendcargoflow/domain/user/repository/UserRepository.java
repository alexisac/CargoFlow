package com.example.backendcargoflow.domain.user.repository;

import com.example.backendcargoflow.domain.trip.entity.TripStatus;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import com.example.backendcargoflow.domain.user.entity.User;
import com.example.backendcargoflow.domain.user.entity.UserFullNameProjection;
import com.example.backendcargoflow.domain.user.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<UserFullNameProjection> findFullNameProjectionById(Long id);
    @Query("""
    SELECT u.id AS id,
           u.firstName AS firstName,
           u.lastName AS lastName
    FROM User u
    WHERE u.role = :driverRole
      AND u.active = true
      AND NOT EXISTS (
          SELECT 1
          FROM TripAssignment ta
          JOIN Trip t ON t.id = ta.tripId
          WHERE ta.driverId = u.id
            AND t.tripStatus IN :blockingTripStatuses
            AND t.pickupInstant < :newTripEndInstant
            AND t.deliveryInstant > :newTripStartInstant
      )
    ORDER BY u.firstName, u.lastName
    """)
    Page<AvailableDriverProjection> findAvailableDriversForTrip(
            @Param("driverRole") UserRole driverRole,
            @Param("blockingTripStatuses") List<TripStatus> blockingTripStatuses,
            @Param("newTripStartInstant") Instant newTripStartInstant,
            @Param("newTripEndInstant") Instant newTripEndInstant,
            Pageable pageable
    );

    Page<User> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
