package com.example.officeapp.models

data class JWTPayload(
    val userId: Long? = null,
    val role: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val sub: String? = null,
    val iat: Long? = null,
    val exp: Long? = null
)
