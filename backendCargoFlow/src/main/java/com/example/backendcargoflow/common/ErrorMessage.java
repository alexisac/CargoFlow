package com.example.backendcargoflow.common;

public class ErrorMessage {
    public static final String USER_ALREADY_EXIST = "User already exist in database";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    public static final String USER_NOT_FOUND = "User with this id is not found in database";

    public static final String VEHICLE_ALREADY_EXIST = "Vehicle already exist in database";
    public static final String MAX_WEIGHT_REQUIRED = "MaxWeight is required for this vehicle type";
    public static final String MAX_WEIGHT_NOT_REQUIRED = "MaxWeight must be empty for this vehicle type";
    public static final String MAX_VOLUME_REQUIRED = "MaxVolume is required for this vehicle type";
    public static final String MAX_VOLUME_NOT_REQUIRED = "MaxVolume must be empty for this vehicle type";

    public static final String TRIP_NOT_FOUND = "Trip with this id is not found in database";
}