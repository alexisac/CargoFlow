package com.example.officeapp.screens.reusableComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    required: Boolean = false,
    enabled: Boolean = true,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    secondaryTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerColor: Color = Color.Transparent,
    borderColor: Color = MaterialTheme.colorScheme.outline
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val effectiveSecondaryColor = if (enabled) {
        secondaryTextColor
    } else {
        secondaryTextColor.copy(alpha = 0.45f)
    }

    val effectiveTextColor = if (enabled) {
        textColor
    } else {
        textColor.copy(alpha = 0.45f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 66.dp),
        shape = RoundedCornerShape(9.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) {
                borderColor
            } else {
                borderColor.copy(alpha = 0.35f)
            }
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
                .padding(start = 18.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = stringResource(R.string.description_password_icon),
                tint = if (enabled) {
                    iconColor
                } else {
                    iconColor.copy(alpha = 0.35f)
                }
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = effectiveTextColor,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(iconColor),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (value.isBlank() && !placeholder.isNullOrBlank()) {
                                Text(
                                    text = placeholder,
                                    color = effectiveSecondaryColor,
                                    fontSize = 16.sp
                                )
                            }

                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                )
            }

            IconButton(
                onClick = {
                    passwordVisible = !passwordVisible
                },
                enabled = enabled
            ) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Outlined.Visibility
                    } else {
                        Icons.Outlined.VisibilityOff
                    },
                    contentDescription = if (passwordVisible) {
                        stringResource(R.string.description_hide_password)
                    } else {
                        stringResource(R.string.description_show_password)
                    },
                    tint = if (enabled) {
                        iconColor
                    } else {
                        iconColor.copy(alpha = 0.35f)
                    }
                )
            }
        }
    }
}