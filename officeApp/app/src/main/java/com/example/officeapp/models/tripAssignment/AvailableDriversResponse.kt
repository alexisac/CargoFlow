package com.example.officeapp.models.tripAssignment

data class AvailableDriversResponse(
    val drivers: List<AvailableDriver>,
    val pageNumber: Int,
    val pageSize: Int,
    val lastPage: Boolean
)
