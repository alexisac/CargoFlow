package com.example.backendcargoflow.domain.tripAssignment.repository;

import com.example.backendcargoflow.domain.tripAssignment.entity.TripAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripAssignmentRepository extends JpaRepository<TripAssignment, Long> {
}
