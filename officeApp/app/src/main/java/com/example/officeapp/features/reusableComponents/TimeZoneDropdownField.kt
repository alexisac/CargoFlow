package com.example.officeapp.features.reusableComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneDropdownField(
    label: String,
    selectedTimeZone: String,
    onTimeZoneSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: (@Composable () -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    val timeZones = remember {
        ZoneId.getAvailableZoneIds()
            .filter { it.startsWith("Europe/") }
            .sorted()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedTimeZone,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            supportingText = supportingText,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            timeZones.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone) },
                    onClick = {
                        onTimeZoneSelected(zone)
                        expanded = false
                    }
                )
            }
        }
    }
}