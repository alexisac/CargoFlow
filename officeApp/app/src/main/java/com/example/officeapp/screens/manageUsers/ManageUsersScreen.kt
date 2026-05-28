package com.example.officeapp.screens.manageUsers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.viewModels.AuthenticationViewModel

@Composable
fun ManageUsersScreen(
    viewModel: AuthenticationViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllUsers(
            pageNumber = 0,
            pageSize = 20,
            append = false
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearUsers()
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onBack() }
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
                    text = stringResource(R.string.manage_users_title),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.view_and_manage_system_users_subtitle),
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(
                onClick = {
                    // TODO: refresh users
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(R.string.button_refresh)
                )
            }
        }

        if (uiState.isLoading && uiState.users.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

            return
        }

        if (uiState.users.isEmpty() && uiState.errorMessage == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.label_no_users_found),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            return
        }

        LazyColumn(
            modifier = Modifier.padding(top = 24.dp),
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