package com.example.backendcargoflow.service;

import com.example.backendcargoflow.domain.tripAssignment.repository.TripAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripAssignmentCleanupService {
    private final TripAssignmentRepository tripAssignmentRepository;

    public void deleteAssignmentsForTrip(Long tripId) {
        tripAssignmentRepository.deleteByTripId(tripId);
    }
}
