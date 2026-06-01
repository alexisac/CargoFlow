package com.example.officeapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.ThemeToggle
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkSurface
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight

@Composable
fun DriverHomeContent(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    firstName: String,
    lastName: String,
    role: String,
    isLoading: Boolean,
    errorMessage: String?,
    hasCurrentTrip: Boolean,
    currentTripContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    onGoToDriverCompletedTrips: () -> Unit,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val shouldShowError = errorMessage != null && !errorMessage.contains("not found", ignoreCase = true)

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.align(Alignment.CenterStart),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = surfaceColor,
                        contentColor = textColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = stringResource(R.string.button_menu)
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = surfaceColor,
                        contentColor = textColor
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = stringResource(R.string.button_refresh)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Dashboard - $firstName $lastName - $role",
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(34.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = primaryColor
                            )
                        }
                    }

                    shouldShowError -> {
                        Text(
                            text = errorMessage.orEmpty(),
                            color = secondaryTextColor,
                            fontSize = 15.sp
                        )
                    }


                    hasCurrentTrip -> {
                        currentTripContent()
                    }

                    else -> {
                        DriverEmptyTripState(
                            isDarkTheme = isDarkTheme,
                            textColor = textColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        showMenu = false
                    }
            )

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .width(260.dp)
                    .align(Alignment.CenterStart),
                color = surfaceColor,
                shape = RoundedCornerShape(
                    topEnd = 22.dp,
                    bottomEnd = 22.dp
                ),
                shadowElevation = if (isDarkTheme) 0.dp else 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp)
                        .padding(top = 48.dp, bottom = 28.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ) {
                        ThemeToggle(
                            isDarkTheme = isDarkTheme,
                            onThemeChange = onThemeChange,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )

                        Text(
                            text = stringResource(R.string.button_menu),
                            color = textColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        IconButton(
                            onClick = { showMenu = false },
                            modifier = Modifier.align(Alignment.CenterEnd),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = backgroundColor,
                                contentColor = textColor
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    DrawerMenuButton(
                        text = stringResource(R.string.button_completed_trips_history),
                        icon = Icons.Outlined.History,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        borderColor = borderColor,
                        onClick = {
                            showMenu = false
                            onGoToDriverCompletedTrips()
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerMenuButton(
                        text = stringResource(R.string.button_logout),
                        icon = Icons.Outlined.Logout,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        borderColor = borderColor,
                        onClick = {
                            showMenu = false
                            onLogout()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerMenuButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    textColor: Color,
    secondaryTextColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = secondaryTextColor
        )

        Spacer(modifier = Modifier.padding(horizontal = 6.dp))

        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DriverEmptyTripState(
    isDarkTheme: Boolean,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = if (isDarkTheme) {
                    R.drawable.logo_dark
                } else {
                    R.drawable.logo_light
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(190.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.label_no_current_trip_found),
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}