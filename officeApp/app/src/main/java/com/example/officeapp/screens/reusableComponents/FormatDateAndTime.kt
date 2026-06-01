package com.example.officeapp.screens.reusableComponents

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(value: String): String {
    return try {
        LocalDateTime.parse(value)
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } catch (_: Exception) {
        value.substringBefore("T")
    }
}

fun formatHourMinute(value: String): String {
    return try {
        LocalDateTime.parse(value)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (_: Exception) {
        value.substringAfter("T", "").take(5)
    }
}

fun formatHourMinuteSecond(value: String): String {
    return try {
        val parsed = LocalDateTime.parse(value)
        parsed.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    } catch (_: Exception) {
        value.substringAfter("T", "").take(8)
    }
}

fun formatDateTime(value: String): String {
    return try {
        val dateTime = LocalDateTime.parse(value)
        dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    } catch (_: Exception) {
        value.replace("T", " ")
    }
}