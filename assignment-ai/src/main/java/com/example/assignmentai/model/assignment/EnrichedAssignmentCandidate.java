package com.example.assignmentai.model.assignment;

public record EnrichedAssignmentCandidate(
        AssignmentCandidate candidate,
        double roadDistanceToPickupKm
) {}