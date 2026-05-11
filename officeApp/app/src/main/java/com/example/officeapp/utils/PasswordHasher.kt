package com.example.officeapp.utils

import java.security.MessageDigest

object PasswordHasher {
    fun sha256(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())

        return bytes.joinToString("") {byte ->
            "%02x".format(byte)
        }
    }
}