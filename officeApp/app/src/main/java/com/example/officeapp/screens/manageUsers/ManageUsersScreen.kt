package com.example.officeapp.screens.manageUsers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.AuthenticationViewModel

@Composable
fun ManageUsersScreen(
    viewModel: AuthenticationViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val borderColor = if (isDarkTheme) BorderDark else BorderLight

    fun refreshUsers() {
        viewModel.clearUsers()
        viewModel.clearMessages()
        viewModel.getAllUsers(
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
    }

    LaunchedEffect(Unit) {
        refreshUsers()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearUsers()
            viewModel.clearMessages()
        }
    }

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
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.manage_users_title),
                subtitle = stringResource(R.string.view_and_manage_system_users_subtitle),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = borderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearUsers()
                    viewModel.clearMessages()
                    onBack()
                },
                onRefresh = { refreshUsers() },
                modifier = Modifier.fillMaxWidth()
            )

            when {
                uiState.isLoading && uiState.users.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.users.isEmpty() && uiState.errorMessage == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.label_no_users_found),
                            color = textColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(uiState.users) { index, user ->
                            val shouldLoadMore = index >= uiState.users.size - 5

                            if (
                                shouldLoadMore &&
                                !uiState.isLoading &&
                                !uiState.lastPage
                            ) {
                                LaunchedEffect(uiState.users.size) {
                                    viewModel.loadNextUsersPage()
                                }
                            }

                            UserManagementCard(
                                user = user,
                                colorIndex = index % 4,
                                isDarkTheme = isDarkTheme,
                                onChangeStatusClick = {
                                    viewModel.changeUserStatus(
                                        userId = user.id,
                                        active = !user.active
                                    )
                                }
                            )
                        }

                        if (uiState.isLoading && uiState.users.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isDarkTheme = isDarkTheme,
            onMessageShown = {
                viewModel.clearMessages()
            }
        )
    }
}