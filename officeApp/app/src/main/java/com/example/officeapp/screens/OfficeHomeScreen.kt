package com.example.officeapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.models.user.UserRole
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkSurface
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight

@Composable
fun OfficeHomeMenu(
    userRole: String?,
    isDarkTheme: Boolean,
    onGoToAddUser: () -> Unit,
    onGoToAddVehicle: () -> Unit,
    onGoToAddTrip: () -> Unit,
    onGoToSearchTrips: () -> Unit,
    onGoToManageUsers: () -> Unit,
    onGoToManageVehicles: () -> Unit,
    onGoToDriverLocationsMap: () -> Unit,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val surfaceColor = if (isDarkTheme) DarkSurface else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { showMenu = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 42.dp, start = 24.dp),
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
                    .fillMaxHeight()
                    .width(280.dp)
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
                        Text(
                            text = stringResource(R.string.button_menu),
                            color = textColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterStart)
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

                    if (userRole == UserRole.ADMIN.name) {
                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.add_new_user_title),
                            icon = Icons.Outlined.Person,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToAddUser()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.manage_users_title),
                            icon = Icons.Outlined.Person,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToManageUsers()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (userRole in listOf(
                            UserRole.DISPATCHER.name,
                            UserRole.MANAGER.name,
                            UserRole.ADMIN.name
                        )
                    ) {
                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.add_new_vehicle_title),
                            icon = Icons.Outlined.LocalShipping,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToAddVehicle()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.manage_vehicles_title),
                            icon = Icons.Outlined.LocalShipping,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToManageVehicles()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.add_new_trip_title),
                            icon = Icons.Outlined.Route,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToAddTrip()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.search_and_associate_trips_title),
                            icon = Icons.Outlined.Route,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToSearchTrips()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OfficeDrawerMenuButton(
                            text = stringResource(R.string.live_driver_locations_title),
                            icon = Icons.Outlined.LocationOn,
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            borderColor = borderColor,
                            onClick = {
                                showMenu = false
                                onGoToDriverLocationsMap()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    OfficeDrawerMenuButton(
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
private fun OfficeDrawerMenuButton(
    text: String,
    icon: ImageVector,
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