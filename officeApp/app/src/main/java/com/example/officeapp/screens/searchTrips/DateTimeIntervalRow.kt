package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.DatePickerField
import com.example.officeapp.screens.reusableComponents.TimePickerField

@Composable
fun DateTimeIntervalRow(
    fromDateLabel: String,
    fromTimeLabel: String,
    toDateLabel: String,
    toTimeLabel: String,
    fromDateTime: String,
    toDateTime: String,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    containerColor: Color,
    borderColor: Color
) {
    var fromDate by androidx.compose.runtime.remember(fromDateTime) {
        androidx.compose.runtime.mutableStateOf(fromDateTime.substringBefore("T", ""))
    }
    var fromTime by androidx.compose.runtime.remember(fromDateTime) {
        androidx.compose.runtime.mutableStateOf(fromDateTime.substringAfter("T", "").take(5))
    }

    var toDate by androidx.compose.runtime.remember(toDateTime) {
        androidx.compose.runtime.mutableStateOf(toDateTime.substringBefore("T", ""))
    }
    var toTime by androidx.compose.runtime.remember(toDateTime) {
        androidx.compose.runtime.mutableStateOf(toDateTime.substringAfter("T", "").take(5))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DatePickerField(
                label = fromDateLabel,
                value = fromDate,
                onDateSelected = {
                    fromDate = it
                    onFromChange(
                        buildFilterDateTime(
                            date = fromDate,
                            time = fromTime,
                            boundary = DateTimeBoundary.FROM
                        )
                    )
                },
                placeholder = stringResource(R.string.label_date),
                iconColor = iconColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                containerColor = containerColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )

            TimePickerField(
                label = fromTimeLabel,
                value = fromTime,
                onTimeSelected = {
                    fromTime = it
                    onFromChange(
                        buildFilterDateTime(
                            date = fromDate,
                            time = fromTime,
                            boundary = DateTimeBoundary.FROM
                        )
                    )
                },
                placeholder = stringResource(R.string.label_time),
                iconColor = iconColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                containerColor = containerColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DatePickerField(
                label = toDateLabel,
                value = toDate,
                onDateSelected = {
                    toDate = it
                    onToChange(
                        buildFilterDateTime(
                            date = toDate,
                            time = toTime,
                            boundary = DateTimeBoundary.TO
                        )
                    )
                },
                placeholder = stringResource(R.string.label_date),
                iconColor = iconColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                containerColor = containerColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )

            TimePickerField(
                label = toTimeLabel,
                value = toTime,
                onTimeSelected = {
                    toTime = it
                    onToChange(
                        buildFilterDateTime(
                            date = toDate,
                            time = toTime,
                            boundary = DateTimeBoundary.TO
                        )
                    )
                },
                placeholder = stringResource(R.string.label_time),
                iconColor = iconColor,
                textColor = textColor,
                secondaryTextColor = secondaryTextColor,
                containerColor = containerColor,
                borderColor = borderColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}