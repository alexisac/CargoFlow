package com.example.officeapp.screens.manageUsers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.user.UserSummary
import com.example.officeapp.ui.theme.AccentBlue
import com.example.officeapp.ui.theme.AccentCyan
import com.example.officeapp.ui.theme.AccentPink
import com.example.officeapp.ui.theme.AccentViolet
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.ErrorRed
import com.example.officeapp.ui.theme.InfoBlue
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.SuccessGreen
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.ui.theme.WarningOrange

@Composable
fun UserManagementCard(
    user: UserSummary,
    colorIndex: Int,
    isDarkTheme: Boolean,
    onChangeStatusClick: () -> Unit
) {
    val accentColor = when (colorIndex) {
        0 -> AccentBlue
        1 -> AccentCyan
        2 -> AccentViolet
        else -> AccentPink
    }

    val containerColor = if (isDarkTheme) DarkCard else LightSurface
    val primaryTextColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor.copy(alpha = if (isDarkTheme) 0.45f else 0.35f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDarkTheme) 0.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = if (isDarkTheme) 0.18f else 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        color = primaryTextColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusBadge(
                        active = user.active,
                        isDarkTheme = isDarkTheme
                    )
                }

                Text(
                    text = user.email,
                    color = secondaryTextColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = stringResource(R.string.label_role) + ": " + user.role.name,
                    color = secondaryTextColor,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = stringResource(R.string.label_status) + ": " +
                            if (user.active) {
                                stringResource(R.string.label_active).lowercase()
                            } else {
                                stringResource(R.string.label_inactive).lowercase()
                            },
                    color = secondaryTextColor,
                    modifier = Modifier.padding(top = 8.dp)
                )

                OutlinedButton(
                    onClick = onChangeStatusClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = accentColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Block,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (user.active) {
                            stringResource(R.string.label_make_inactive)
                        } else {
                            stringResource(R.string.label_make_active)
                        },
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    active: Boolean,
    isDarkTheme: Boolean
) {
    val color = if (active) SuccessGreen else ErrorRed

    val text = if (active) {
        stringResource(R.string.label_active)
    } else {
        stringResource(R.string.label_inactive)
    }

    val backgroundAlpha = if (isDarkTheme) 0.18f else 0.12f

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = backgroundAlpha))
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = color,
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}