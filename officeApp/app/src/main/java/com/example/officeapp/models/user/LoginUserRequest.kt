package com.example.officeapp.models.user

data class LoginUserRequest(
    val email: String,
    val hashedPassword: String
)