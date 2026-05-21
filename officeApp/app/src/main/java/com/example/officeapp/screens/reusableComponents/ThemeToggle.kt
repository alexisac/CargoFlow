package com.example.officeapp.screens.reusableComponents

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkSurface
import com.example.officeapp.ui.theme.LightCard
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isDarkTheme) 40.dp else 0.dp,
        label = stringResource(R.string.label_theme_toggle)
    )

    val trackColor = if (isDarkTheme) {
        DarkSurface
    } else {
        LightCard
    }

    val borderColor = if (isDarkTheme) {
        BorderDark
    } else {
        BorderLight
    }

    val selectedColor = if (isDarkTheme) {
        PrimaryBlueDark
    } else {
        PrimaryBlueLight
    }

    val inactiveIconColor = if (isDarkTheme) {
        TextSecondaryDark
    } else {
        TextSecondaryLight
    }

    Box(
        modifier = modifier
            .size(width = 84.dp, height = 44.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(selectedColor)
        )

        Row(
            modifier = Modifier.matchParentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        onThemeChange(false)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = stringResource(R.string.description_light_mode),
                    tint = if (!isDarkTheme) TextPrimaryDark else inactiveIconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        onThemeChange(true)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = stringResource(R.string.description_dark_mode),
                    tint = if (isDarkTheme) TextPrimaryDark else inactiveIconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}