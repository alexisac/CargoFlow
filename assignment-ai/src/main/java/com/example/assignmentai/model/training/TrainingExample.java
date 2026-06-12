package com.example.assignmentai.model.training;

import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;

public record TrainingExample(
        AssignmentCandidateFeatures features,
        boolean wasSelected
) {}