package com.example.officeapp.models

data class LoginUserRequest(
    val email: String,
    val hashedPassword: String
)
