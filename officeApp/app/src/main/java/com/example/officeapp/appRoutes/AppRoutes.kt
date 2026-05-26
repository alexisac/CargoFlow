package com.example.officeapp.appRoutes

object AppRoutes {
    const val AUTH_CHECK_ROUTE = "auth_check"
    const val LOGIN_ROUTE = "login"
    const val HOME_ROUTE = "home"
    const val ADD_USER_ROUTE = "add_user"
    const val ADD_VEHICLE_ROUTE = "add_vehicle"
    const val ADD_TRIP_ROUTE = "add_trip"
    const val SEARCH_TRIPS_ROUTE = "search_trips"
    const val TRIP_ID_ARGUMENT = "tripId"
    const val TRIP_DETAILS_BASE_ROUTE = "trip_details"
    const val TRIP_DETAILS_ROUTE = "${TRIP_DETAILS_BASE_ROUTE}/{$TRIP_ID_ARGUMENT}"
    const val ASSIGN_DRIVER_BASE_ROUTE = "assign_driver"
    const val ASSIGN_DRIVER_ROUTE = "$ASSIGN_DRIVER_BASE_ROUTE/{$TRIP_ID_ARGUMENT}"
    const val DRIVER_COMPLETED_TRIPS_ROUTE = "driver_completed_trips"
}