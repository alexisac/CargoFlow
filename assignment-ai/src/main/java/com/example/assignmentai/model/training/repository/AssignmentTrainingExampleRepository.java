package com.example.assignmentai.model.training.repository;

import com.example.assignmentai.model.training.entity.AssignmentTrainingExample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentTrainingExampleRepository
        extends JpaRepository<AssignmentTrainingExample, Long> {
}