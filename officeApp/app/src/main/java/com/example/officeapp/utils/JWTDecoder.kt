package com.example.officeapp.utils

import com.example.officeapp.models.JWTPayload
import android.util.Base64
import com.google.gson.Gson

object JWTDecoder {
    fun decodePayload(token: String): JWTPayload? {
        return try {
            val parts = token.split(".")

            if (parts.size < 2) {
                return null
            }

            val payload = parts[1]

            val decodedBytes = Base64.decode(
                payload,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )

            val decodeJson = String(decodedBytes, Charsets.UTF_8)

            Gson().fromJson(decodeJson, JWTPayload::class.java)
        } catch (ex: Exception) {
            null
        }
    }

    fun isTokenExpiredOrExpiresToday(token: String): Boolean {
        val payload = decodePayload(token) ?: return true
        val expirationTimeSeconds = payload.exp ?: return true
        val currentTimeSeconds = System.currentTimeMillis()/1000

        // Token is already expired
        if(expirationTimeSeconds <= currentTimeSeconds) {
            return true
        }

        val zoneId = java.time.ZoneId.systemDefault()
        val expirationDate = java.time.Instant
            .ofEpochSecond(expirationTimeSeconds)
            .atZone(zoneId)
            .toLocalDate()

        val today = java.time.LocalDate.now(zoneId)

        // Token will expire today
        return expirationDate == today
    }
}