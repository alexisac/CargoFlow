package com.example.officeapp.screens.manageUsers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.user.UserSummary

@Composable
fun UserManagementCard(
    user: UserSummary,
    onChangeStatusClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${user.firstName} ${user.lastName}")

            Text(
                text = user.email,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.label_role) + ": " + user.role.name,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = if (user.active)
                    stringResource(R.string.label_status_active)
                else
                    stringResource(R.string.label_status_inactive),
                modifier = Modifier.padding(top = 4.dp)
            )

            OutlinedButton(
                onClick = onChangeStatusClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = if (user.active) {
                        stringResource(R.string.label_make_inactive)
                    } else {
                        stringResource(R.string.label_make_active)
                    }
                )
            }
        }
    }
}