package com.example.officeapp.screens.reusableComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R

@Composable
fun FormScreenHeader(
    title: String,
    subtitle: String? = null,
    textColor: Color,
    subtitleColor: Color,
    borderColor: Color,
    iconColor: Color,
    onBack: () -> Unit,
    onRefresh: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedIconButton(
            onClick = onBack,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, borderColor),
            colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = iconColor)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.button_back)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    color = subtitleColor,
                    fontSize = 15.sp
                )
            }
        }

        if (onRefresh != null) {
            IconButton(
                onClick = onRefresh
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(R.string.button_refresh),
                    tint = iconColor
                )
            }
        }
    }
}