package com.example.officeapp.screens.searchTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Signpost
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.formatDate
import com.example.officeapp.screens.reusableComponents.formatHourMinuteSecond
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun buildFilterDateTime(
    date: String,
    time: String,
    boundary: DateTimeBoundary
): String {
    if (date.isBlank() && time.isBlank()) {
        return ""
    }

    val apiDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    val apiTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val resolvedDate = when {
        date.isNotBlank() -> LocalDate.parse(date)
        boundary == DateTimeBoundary.FROM -> LocalDate.of(1900, 1, 1)
        else -> LocalDate.of(2100, 12, 31)
    }

    val resolvedTime = when {
        time.isNotBlank() -> LocalTime.parse(time)
        boundary == DateTimeBoundary.FROM -> LocalTime.of(0, 0, 0)
        else -> LocalTime.of(23, 59, 59)
    }

    return resolvedDate.format(apiDateFormatter) + "T" + resolvedTime.format(apiTimeFormatter)
}

@Composable
fun StatusPill(
    text: String,
    color: Color,
    isDarkTheme: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = if (isDarkTheme) 0.18f else 0.12f))
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TripLocationBlock(
    title: String,
    location: String,
    dateTime: String,
    timeZone: String,
    accentColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    modifier: Modifier = Modifier
) {
    val displayDate = formatDate(dateTime)
    val displayTime = formatHourMinuteSecond(dateTime)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier
                .padding(top = 22.dp)
                .size(22.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = title,
                color = secondaryTextColor,
                fontSize = 13.sp
            )

            Text(
                text = location,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = displayDate,
                color = secondaryTextColor,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = displayTime,
                color = secondaryTextColor,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = timeZone,
                color = secondaryTextColor,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TripDetailSection(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    isDarkTheme: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor.copy(alpha = if (isDarkTheme) 0.45f else 0.35f)
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDarkTheme) 0.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = accentColor.copy(alpha = if (isDarkTheme) 0.18f else 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = accentColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                content()
            }
        }
    }
}

@Composable
fun AddressRows(
    country: String,
    administrativeArea: String,
    city: String,
    streetName: String,
    streetNumber: String,
    postalCode: String,
    additionalDetails: String?,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color
) {
    DetailRow(
        label = stringResource(R.string.label_country),
        value = country,
        icon = Icons.Outlined.Public,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_administrative_area_multiline),
        value = administrativeArea,
        icon = Icons.Outlined.Map,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_city),
        value = city,
        icon = Icons.Outlined.LocationCity,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_street_name),
        value = streetName,
        icon = Icons.Outlined.Signpost,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_street_number),
        value = streetNumber,
        icon = Icons.Outlined.Numbers,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_postal_code),
        value = postalCode,
        icon = Icons.Outlined.Description,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )

    DetailRow(
        label = stringResource(R.string.label_additional_details_multiline),
        value = additionalDetails ?: "-",
        icon = Icons.Outlined.Info,
        iconColor = iconColor,
        textColor = textColor,
        secondaryTextColor = secondaryTextColor
    )
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    valueColor: Color = textColor
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(17.dp)
        )

        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

        Text(
            text = label,
            color = secondaryTextColor,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SectionDivider(
    isDarkTheme: Boolean
) {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = if (isDarkTheme) BorderDark.copy(alpha = 0.75f) else BorderLight
    )
}
