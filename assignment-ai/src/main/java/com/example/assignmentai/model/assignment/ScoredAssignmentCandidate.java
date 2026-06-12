package com.example.assignmentai.model.assignment;

public record ScoredAssignmentCandidate(
        EnrichedAssignmentCandidate enrichedAssignmentCandidate,
        double probabilitySelected
) {}