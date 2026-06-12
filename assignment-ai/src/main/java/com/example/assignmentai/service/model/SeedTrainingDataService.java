package com.example.assignmentai.service.model;

import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.training.TrainingExample;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeedTrainingDataService {

    public List<TrainingExample> getSeedTrainingExamples() {
        return List.of(
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                5.0,
                                8000.0,
                                35.0,
                                0.4,
                                0.5,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                180.0,
                                8.0
                        ),
                        true
                ),
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                80.0,
                                8000.0,
                                35.0,
                                0.4,
                                0.5,
                                1.0,
                                1.0,
                                0.0,
                                0.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                180.0,
                                8.0
                        ),
                        false
                ),
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                10.0,
                                26000.0,
                                120.0,
                                1.3,
                                1.4,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                120.0,
                                5.0
                        ),
                        false
                ),
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                12.0,
                                0.0,
                                45.0,
                                0.0,
                                0.6,
                                0.0,
                                1.0,
                                1.0,
                                1.0,
                                0.0,
                                1.0,
                                1.0,
                                1.0,
                                240.0,
                                10.0
                        ),
                        true
                ),
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                25.0,
                                0.0,
                                90.0,
                                0.0,
                                0.95,
                                0.0,
                                1.0,
                                0.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                1.0,
                                300.0,
                                6.0
                        ),
                        true
                ),
                new TrainingExample(
                        new AssignmentCandidateFeatures(
                                15.0,
                                18000.0,
                                70.0,
                                0.85,
                                0.9,
                                1.0,
                                1.0,
                                0.0,
                                0.0,
                                1.0,
                                0.0,
                                1.0,
                                1.0,
                                90.0,
                                2.0
                        ),
                        false
                )
        );
    }
}