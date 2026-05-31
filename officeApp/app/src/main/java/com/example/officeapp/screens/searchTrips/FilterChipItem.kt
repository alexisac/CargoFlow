package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R

@Composable
fun FilterChipItem(
    text: String,
    accentColor: Color,
    textColor: Color,
    containerColor: Color,
    borderColor: Color,
    onRemove: () -> Unit
) {
    AssistChip(
        onClick = onRemove,
        label = {
            Text(
                text = text,
                color = textColor
            )
        },
        leadingIcon = {
            Spacer(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.description_remove_filter),
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = AssistChipDefaults.assistChipColors(containerColor = containerColor),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(9.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
    )
}