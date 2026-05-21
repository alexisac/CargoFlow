package com.example.officeapp.screens.searchTrips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.officeapp.R

@Composable
fun FilterChipItem(
    text: String,
    onRemove: () -> Unit
) {
    AssistChip(
        onClick = {},
        label = {
            Text(text)
        },
        trailingIcon = {
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.description_remove_filter)
                )
            }
        }
    )
}