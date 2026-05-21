package com.example.officeapp.models.trip

data class TripSearchRequest(
    val tripStatusList: List<TripStatus>? = null,
    val pickupCountries: List<String>? = null,
    val pickupCities: List<String>? = null,
    val deliveryCountries: List<String>? = null,
    val deliveryCities: List<String>? = null,
    val pickupDateTimeFrom: String? = null,
    val pickupDateTimeTo: String? = null,
    val deliveryDateTimeFrom: String? = null,
    val deliveryDateTimeTo: String? = null,
    val pageNumber: Int,
    val pageSize: Int
)
