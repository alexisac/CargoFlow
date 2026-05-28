package com.example.officeapp.screens.reusableComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R

@Composable
fun OfficeFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    containerColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    required: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    val effectiveSecondaryColor = if (enabled) secondaryTextColor else secondaryTextColor.copy(alpha = 0.45f)
    val effectiveTextColor = if (enabled) textColor else textColor.copy(alpha = 0.45f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(9.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) borderColor else borderColor.copy(alpha = 0.35f)
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
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
                tint = if (enabled) iconColor else iconColor.copy(alpha = 0.35f)
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
                            text = stringResource(R.string.label_required_field),
                            color = Color(0xFFFF5A52),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    textStyle = LocalTextStyle.current.copy(
                        color = effectiveTextColor,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(iconColor),
                    decorationBox = { innerTextField ->
                        if (value.isBlank() && !placeholder.isNullOrBlank()) {
                            Text(
                                text = placeholder,
                                color = effectiveSecondaryColor,
                                fontSize = 16.sp
                            )
                        }

                        innerTextField()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                )
            }
        }
    }
}