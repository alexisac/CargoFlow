package com.example.officeapp.models

data class AddNewUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val hashedPassword: String,
    val role: UserRole
)
