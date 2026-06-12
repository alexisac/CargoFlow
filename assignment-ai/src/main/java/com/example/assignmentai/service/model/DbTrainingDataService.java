package com.example.assignmentai.service.model;

import com.example.assignmentai.model.training.TrainingExample;
import com.example.assignmentai.model.training.entity.AssignmentTrainingExample;
import com.example.assignmentai.model.mapper.TrainingExampleMapper;
import com.example.assignmentai.model.training.repository.AssignmentTrainingExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbTrainingDataService {
    private final AssignmentTrainingExampleRepository assignmentTrainingExampleRepository;
    private final TrainingExampleMapper trainingExampleMapper;

    public List<TrainingExample> getTrainingExamplesFromDb() {
        List<AssignmentTrainingExample> assignmentTrainingExamples = assignmentTrainingExampleRepository.findAll();
        return trainingExampleMapper.mapAssignmentTrainingExamplesToTrainingExamples(assignmentTrainingExamples);
    }
}