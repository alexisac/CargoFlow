package com.example.officeapp.screens.searchTrips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.OfficeFormTextField

@Composable
fun AddTextFilterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    textColor: Color,
    secondaryTextColor: Color,
    containerColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    OfficeFormTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        icon = Icons.Outlined.LocationOn,
        iconColor = secondaryTextColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor,
        containerColor = containerColor,
        borderColor = borderColor,
        modifier = modifier,
        trailingIcon = if (value.isNotBlank()) {
            {
                IconButton(
                    onClick = onClear
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.description_remove_filter),
                        tint = secondaryTextColor
                    )
                }
            }
        } else {
            null
        }
    )
}