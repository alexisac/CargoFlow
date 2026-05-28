package com.example.officeapp.screens.manageUsers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.OldFormMessages
import com.example.officeapp.viewModels.AuthenticationViewModel

@Composable
fun ManageUsersScreen(
    viewModel: AuthenticationViewModel,
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
        Text(text = stringResource(R.string.manage_users_title))

        OutlinedButton(
            onClick = {
                viewModel.clearUsers()
                viewModel.clearMessages()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.button_back))
        }

        OldFormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier.padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessages() }
        )

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
            Text(
                text = stringResource(R.string.label_no_users_found),
                modifier = Modifier.padding(top = 24.dp)
            )

            return
        }

        LazyColumn(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}