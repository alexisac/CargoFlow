package com.example.assignmentai.common;

public class LogMessage {
    public static final String OPTIMIZE_TRIP_ASSIGNMENT = "OPTIMIZE_TRIP_ASSIGNMENT was invoked with: \n" +
            "tripId = %d, \n" +
            "candidatesCount = %s";
    public static final String SAVE_ASSIGNMENT_FEEDBACK = "SAVE_ASSIGNMENT_FEEDBACK was invoked with: \n" +
                    "tripId = %s, \n" +
                    "selectedDriverId = %s, \n" +
                    "selectedPrimaryVehicleId = %s, \n" +
                    "selectedTrailerId = %s, \n" +
                    "evaluatedCandidatesCount = %s";
    public static final String TRAIN_ASSIGNMENT_MODEL = "TRAIN_ASSIGNMENT_MODEL was invoked";
    public static final String AUTO_OPTIMIZE_TRIP_ASSIGNMENT = "AUTO_OPTIMIZE_TRIP_ASSIGNMENT was invoked with: \n" +
            "tripId = %s";
}