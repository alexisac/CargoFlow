package com.example.officeapp.features.authentication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.officeapp.features.authentication.AuthenticationViewModel
import com.example.officeapp.features.reusableComponents.FormMessages
import com.example.officeapp.features.reusableComponents.LoadingButton
import com.example.officeapp.features.reusableComponents.PasswordField
import com.example.officeapp.models.user.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewUserScreen(
    viewModel: AuthenticationViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.DRIVER) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Add new user",
            modifier = Modifier
                .padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        PasswordField(
            value = confirmedPassword,
            onValueChange = { confirmedPassword = it },
            label = "Confirmed password",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            OutlinedTextField(
                value = selectedRole.name,
                onValueChange = {},
                readOnly = true,
                label = {Text("Role")},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                UserRole.entries.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role.name) },
                        onClick = {
                            selectedRole = role
                            expanded = false
                        }
                    )
                }
            }
        }

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            modifier = Modifier
                .padding(top = 16.dp),
            onMessageShown = { viewModel.clearMessages() }
        )

        LoadingButton(
            text = "Create user",
            isLoading = uiState.isLoading,
            onClick = {
                viewModel.addNewUser(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    confirmedPassword = confirmedPassword,
                    role = selectedRole
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            enabled = !uiState.isLoading
        )

        OutlinedButton(
            onClick = {
                viewModel.clearMessages()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Back")
        }
    }
}