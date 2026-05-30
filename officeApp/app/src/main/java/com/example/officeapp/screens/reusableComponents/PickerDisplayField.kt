package com.example.officeapp.screens.reusableComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PickerDisplayField(
    value: String,
    label: String,
    placeholder: String?,
    icon: ImageVector,
    required: Boolean,
    enabled: Boolean,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    containerColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val effectiveTextColor = if (enabled) textColor else textColor.copy(alpha = 0.45f)
    val effectiveSecondaryColor = if (enabled) secondaryTextColor else secondaryTextColor.copy(alpha = 0.45f)
    val effectiveIconColor = if (enabled) iconColor else iconColor.copy(alpha = 0.35f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 66.dp)
            .clickable(enabled = enabled) {
                onClick()
            },
        shape = RoundedCornerShape(9.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) borderColor else borderColor.copy(alpha = 0.35f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 66.dp)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = effectiveIconColor
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = label,
                        color = effectiveTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (required) {
                        Text(
                            text = " *",
                            color = Color(0xFFFF5A52),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                val displayText = if (value.isBlank()) placeholder.orEmpty() else value

                if (displayText.isNotBlank()) {
                    Text(
                        text = displayText,
                        color = if (value.isBlank()) effectiveSecondaryColor else effectiveTextColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = effectiveIconColor
            )
        }
    }
}