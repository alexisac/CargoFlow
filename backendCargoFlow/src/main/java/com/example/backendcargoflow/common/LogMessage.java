package com.example.backendcargoflow.common;

public class LogMessage {
    public static final String ADD_NEW_USER = "ADD_NEW_USER operation was invoked with: \n firstName = %s, \n lastName = %s, \n email = %s, \n role = %s";
    public static final String LOGIN_USER = "LOGIN_USER operation was invoked with: \n email = %s, \n password = %s";

    public static final String ADD_NEW_VEHICLE = "ADD_NEW_VEHICLE operation was invoked with: \n licencePlate = %s, \n VIN = %s, \n brand = %s, \n model = %s, \n manufactureYear = %d, \n type = %s, \n maxWeight = %d, \n maxVolume = %d, \n status = %s, \n additionalInfo = %s";
}
