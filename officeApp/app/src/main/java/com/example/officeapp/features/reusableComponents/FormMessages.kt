package com.example.officeapp.features.reusableComponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FormMessages(
    errorMessage: String?,
    successMessage: String?,
    modifier: Modifier = Modifier
) {
    val message = if (!errorMessage.isNullOrBlank()) {
        errorMessage
    } else {
        successMessage
    }

    if (!message.isNullOrBlank()) {
        val color = if (errorMessage != null) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        }

        Text(
            text = message,
            color = color,
            modifier = modifier
        )
    }
}