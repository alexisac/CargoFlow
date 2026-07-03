package com.example.backendcargoflow.common;

public class ErrorMessage {
    public static final String USER_ALREADY_EXIST = "User already exist in database";
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    public static final String USER_EXISTS_BUT_IS_INACTIVE = "User exists but is inactive";
    public static final String USER_NOT_FOUND = "User with this id is not found in database";

    public static final String VEHICLE_ALREADY_EXIST = "Vehicle already exist in database";
    public static final String MAX_WEIGHT_REQUIRED = "MaxWeight is required for this vehicle type";
    public static final String MAX_WEIGHT_NOT_REQUIRED = "MaxWeight must be empty for this vehicle type";
    public static final String MAX_VOLUME_REQUIRED = "MaxVolume is required for this vehicle type";
    public static final String MAX_VOLUME_NOT_REQUIRED = "MaxVolume must be empty for this vehicle type";
    public static final String VEHICLE_NOT_FOUND = "Vehicle was not found";

    public static final String TRIP_NOT_FOUND = "Trip with this id is not found in database";
    public static final String INVALID_TIME_ZONE = "Invalid time zone: %s";
    public static final String TRIP_IS_NOT_PLANNED = "Only planned trips can be assigned to a driver";
    public static final String CURRENT_TRIP_NOT_FOUND = "Current trip was not found";
    public static final String INVALID_COMPLETED_TRIPS_PERIOD = "Completed trips period must be 30, 60 or 90 days";
    public static final String TRIP_CANNOT_BE_CANCELED = "Only planned or assigned trips can be canceled";
    public static final String TRIP_STATUS_CANNOT_BE_ADVANCED = "Trip status cannot be advanced from the current status.";
}