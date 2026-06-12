package com.example.backendcargoflow.domain.tripAssignment.repository;

import com.example.backendcargoflow.domain.tripAssignment.entity.TripAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripAssignmentRepository extends JpaRepository<TripAssignment, Long> {
    @Transactional
    void deleteByTripId(Long tripId);
    List<TripAssignment> findByDriverIdInOrderByAssignedDateDesc(List<Long> driverIds);
}
