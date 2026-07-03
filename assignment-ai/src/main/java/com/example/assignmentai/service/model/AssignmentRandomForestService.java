package com.example.assignmentai.service.model;

import com.example.assignmentai.model.assignment.AssignmentCandidateFeatures;
import com.example.assignmentai.model.training.TrainingExample;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentRandomForestService {
    private final SeedTrainingDataService seedTrainingDataService;
    private final DbTrainingDataService dbTrainingDataService;

    private RandomForest randomForest;

    @PostConstruct
    public void trainInitialModel() {
        List<TrainingExample> trainingExamples = seedTrainingDataService.getSeedTrainingExamples();
        train(trainingExamples);
    }

    public int trainModel() {
        List<TrainingExample> seedTrainingExamples = seedTrainingDataService.getSeedTrainingExamples();
        List<TrainingExample> dbTrainingExamples = dbTrainingDataService.getTrainingExamplesFromDb();

        List<TrainingExample> allTrainingExamples = new java.util.ArrayList<>();
        allTrainingExamples.addAll(seedTrainingExamples);
        allTrainingExamples.addAll(dbTrainingExamples);

        train(allTrainingExamples);

        return allTrainingExamples.size();
    }

    public double predictProbabilitySelected(
            AssignmentCandidateFeatures features
    ) {
        if (randomForest == null) {
            return 0.0;
        }

        DataFrame predictionDataFrame = buildPredictionDataFrame(features);

        Tuple tuple = predictionDataFrame.get(0);

        double[] posteriori = new double[2];

        int predictedClass = randomForest.predict(tuple, posteriori);

        if (posteriori.length > 1 && !Double.isNaN(posteriori[1])) {
            return posteriori[1];
        }

        return predictedClass == 1 ? 0.75 : 0.25;
    }

    private void train(List<TrainingExample> trainingExamples) {
        DataFrame trainingDataFrame = buildTrainingDataFrame(trainingExamples);
        Formula formula = Formula.lhs("wasSelected");
        randomForest = RandomForest.fit(formula, trainingDataFrame);
    }

    private DataFrame buildTrainingDataFrame(List<TrainingExample> trainingExamples) {
        int size = trainingExamples.size();

        double[] roadDistanceToPickupKm = new double[size];
        double[] cargoWeight = new double[size];
        double[] cargoVolume = new double[size];
        double[] weightUsageRatio = new double[size];
        double[] volumeUsageRatio = new double[size];
        double[] hasWeightCapacity = new double[size];
        double[] hasVolumeCapacity = new double[size];
        double[] keepsPreviousPrimaryVehicle = new double[size];
        double[] keepsPreviousTrailer = new double[size];
        double[] trailerRequired = new double[size];
        double[] driverAvailable = new double[size];
        double[] primaryVehicleAvailable = new double[size];
        double[] trailerAvailable = new double[size];
        double[] minutesUntilPickup = new double[size];
        double[] driverCompletedTripsLast30Days = new double[size];
        int[] wasSelected = new int[size];

        for (int index = 0; index < size; index++) {
            AssignmentCandidateFeatures features = trainingExamples.get(index).features();

            roadDistanceToPickupKm[index] = features.roadDistanceToPickupKm();
            cargoWeight[index] = features.cargoWeight();
            cargoVolume[index] = features.cargoVolume();
            weightUsageRatio[index] = features.weightUsageRatio();
            volumeUsageRatio[index] = features.volumeUsageRatio();
            hasWeightCapacity[index] = features.hasWeightCapacity();
            hasVolumeCapacity[index] = features.hasVolumeCapacity();
            keepsPreviousPrimaryVehicle[index] = features.keepsPreviousPrimaryVehicle();
            keepsPreviousTrailer[index] = features.keepsPreviousTrailer();
            trailerRequired[index] = features.trailerRequired();
            driverAvailable[index] = features.driverAvailable();
            primaryVehicleAvailable[index] = features.primaryVehicleAvailable();
            trailerAvailable[index] = features.trailerAvailable();
            minutesUntilPickup[index] = features.minutesUntilPickup();
            driverCompletedTripsLast30Days[index] = features.driverCompletedTripsLast30Days();

            wasSelected[index] = trainingExamples.get(index).wasSelected() ? 1 : 0;
        }

        return new DataFrame(
                new DoubleVector("roadDistanceToPickupKm", roadDistanceToPickupKm),
                new DoubleVector("cargoWeight", cargoWeight),
                new DoubleVector("cargoVolume", cargoVolume),
                new DoubleVector("weightUsageRatio", weightUsageRatio),
                new DoubleVector("volumeUsageRatio", volumeUsageRatio),
                new DoubleVector("hasWeightCapacity", hasWeightCapacity),
                new DoubleVector("hasVolumeCapacity", hasVolumeCapacity),
                new DoubleVector("keepsPreviousPrimaryVehicle", keepsPreviousPrimaryVehicle),
                new DoubleVector("keepsPreviousTrailer", keepsPreviousTrailer),
                new DoubleVector("trailerRequired", trailerRequired),
                new DoubleVector("driverAvailable", driverAvailable),
                new DoubleVector("primaryVehicleAvailable", primaryVehicleAvailable),
                new DoubleVector("trailerAvailable", trailerAvailable),
                new DoubleVector("minutesUntilPickup", minutesUntilPickup),
                new DoubleVector("driverCompletedTripsLast30Days", driverCompletedTripsLast30Days),
                new IntVector("wasSelected", wasSelected)
        );
    }

    private DataFrame buildPredictionDataFrame(
            AssignmentCandidateFeatures features
    ) {
        return new DataFrame(
                new DoubleVector("roadDistanceToPickupKm", new double[] { features.roadDistanceToPickupKm() }),
                new DoubleVector("cargoWeight", new double[] { features.cargoWeight() }),
                new DoubleVector("cargoVolume", new double[] { features.cargoVolume() }),
                new DoubleVector("weightUsageRatio", new double[] { features.weightUsageRatio() }),
                new DoubleVector("volumeUsageRatio", new double[] { features.volumeUsageRatio() }),
                new DoubleVector("hasWeightCapacity", new double[] { features.hasWeightCapacity() }),
                new DoubleVector("hasVolumeCapacity", new double[] { features.hasVolumeCapacity() }),
                new DoubleVector("keepsPreviousPrimaryVehicle", new double[] { features.keepsPreviousPrimaryVehicle() }),
                new DoubleVector("keepsPreviousTrailer", new double[] { features.keepsPreviousTrailer() }),
                new DoubleVector("trailerRequired", new double[] { features.trailerRequired() }),
                new DoubleVector("driverAvailable", new double[] { features.driverAvailable() }),
                new DoubleVector("primaryVehicleAvailable", new double[] { features.primaryVehicleAvailable() }),
                new DoubleVector("trailerAvailable", new double[] { features.trailerAvailable() }),
                new DoubleVector("minutesUntilPickup", new double[] { features.minutesUntilPickup() }),
                new DoubleVector("driverCompletedTripsLast30Days", new double[] { features.driverCompletedTripsLast30Days() })
        );
    }
}