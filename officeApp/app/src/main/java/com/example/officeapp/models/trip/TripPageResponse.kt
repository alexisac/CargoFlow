package com.example.officeapp.models.trip

data class TripPageResponse(
    val trips: List<TripSummary>,
    val pageNumber: Int,
    val pageSize: Int,
    val lastPage: Boolean
)
