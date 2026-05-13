package com.example.officeapp.models.user

data class LoginUserResponse(
    val accessToken: String,
    val tokenType: String
)