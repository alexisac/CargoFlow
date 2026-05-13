package com.example.officeapp.models.user

import com.example.officeapp.models.user.UserRole

data class AddNewUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val hashedPassword: String,
    val role: UserRole
)