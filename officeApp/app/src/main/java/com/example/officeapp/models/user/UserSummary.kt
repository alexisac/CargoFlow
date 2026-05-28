package com.example.officeapp.models.user

data class UserSummary(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val active: Boolean
)