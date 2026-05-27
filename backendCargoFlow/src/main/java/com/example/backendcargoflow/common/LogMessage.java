package com.example.backendcargoflow.common;

public class LogMessage {
    public static final String ADD_NEW_USER = "ADD_NEW_USER operation was invoked with: \n" +
            " firstName = %s, \n" +
            " lastName = %s, \n" +
            " email = %s, \n" +
            " role = %s";
    public static final String LOGIN_USER = "LOGIN_USER operation was invoked with: \n" +
            " email = %s, \n" +
            " password = %s";
    public static final String GET_ALL_USERS = "GET_ALL_USERS operation was invoked";
    public static final String CHANGE_USER_STATUS = "CHANGE_USER_STATUS operation was invoked with:\n" +
            "userId: %s, \n" +
            "active: %s";

    public static final String ADD_NEW_VEHICLE = "ADD_NEW_VEHICLE operation was invoked with: \n" +
            " licencePlate = %s, \n" +
            " VIN = %s, \n" +
            " brand = %s, \n" +
            " model = %s, \n" +
            " manufactureYear = %d, \n" +
            " type = %s, \n" +
            " maxWeight = %s, \n" +
            " maxVolume = %s, \n" +
            " status = %s, \n" +
            " additionalInfo = %s";


    public static final String ADD_NEW_TRIP = "ADD_NEW_TRIP operation was invoked with: \n" +
            "PICKUP ADDRESS: \n " +
            "country = %s, \n" +
            "administrativeAre = %s, \n" +
            "city = %s, \n" +
            "streetName = %s, \n" +
            "streetNumber = %s, \n" +
            "postalCode = %s, \n" +
            "additionalDetails = %s, \n" +
            "\n" +
            "DELIVERY ADDRESS: \n " +
            "country = %s, \n" +
            "administrativeAre = %s, \n" +
            "city = %s, \n" +
            "streetName = %s, \n" +
            "streetNumber = %s, \n" +
            "postalCode = %s, \n" +
            "additionalDetails = %s, \n" +
            "\n" +
            "pickupDateTime = %s, \n" +
            "pickupTimeZone = %s, \n" +
            "deliveryDateTime = %s, \n" +
            "deliveryTimeZone = %s, \n" +
            "cargoDescription = %s, \n" +
            "cargoWeight = %s, \n" +
            "cargoVolume = %s, \n" +
            "cargoType = %s, \n" +
            "price = %s, \n" +
            "currency = %s, \n" +
            "additionalInfo = %s";
    public static final String SEARCH_TRIPS = "SEARCH_TRIPS operation was invoked with: \n" +
            "tripStatuses = %s, \n" +
            "pickupCountries = %s, \n" +
            "pickupCities = %s, \n" +
            "deliveryCountries = %s, \n" +
            "deliveryCities = %s, \n" +
            "pickupDateTimeFrom = %s, \n" +
            "pickupDateTimeTo = %s, \n" +
            "deliveryDateTimeFrom = %s, \n" +
            "deliveryDateTimeTo = %s, \n" +
            "pageNumber = %d, \n" +
            "pageSize = %d";
    public static final String GET_TRIP = "GET_TRIP operation was invoked with: tripId = %d";
    public static final String GET_CURRENT_TRIP = "GET_CURRENT_TRIP operation was invoked";
    public static final String GET_COMPLETED_TRIPS = "GET_COMPLETED_TRIPS operation was invoked with days: %s";

    public static final String GET_AVAILABLE_DRIVERS_FOR_TRIP = "GET_AVAILABLE_DRIVERS_FOR_TRIP operation was invoked with:" +
            "tripId: %s, \n" +
            "pageNumber: %d, \n" +
            "pageSize: %d";
    public static final String GET_AVAILABLE_PRIMARY_VEHICLES_FOR_TRIP = "GET_AVAILABLE_PRIMARY_VEHICLES_FOR_TRIP operation was invoked with:" +
            "tripId: %s, \n" +
            "pageNumber: %d, \n" +
            "pageSize: %d";
    public static final String GET_AVAILABLE_TRAILERS_FOR_TRIP = "GET_AVAILABLE_TRAILERS_FOR_TRIP operation was invoked with:" +
            "tripId: %s, \n" +
            "pageNumber: %d, \n" +
            "pageSize: %d";
    public static final String ASSIGN_TRIP = "ASSIGN_TRIP operation was invoked with: \n" +
            "tripID: %s, \n" +
            "driverID: %s, \n" +
            "primaryVehicleID: %s, \n" +
            "trailerID: %s";
}
