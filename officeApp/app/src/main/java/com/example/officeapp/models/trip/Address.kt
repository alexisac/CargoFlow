package com.example.officeapp.models.trip

data class Address(
    val country: String,
    val administrativeArea: String,
    val city: String,
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val additionalDetails: String?
)
