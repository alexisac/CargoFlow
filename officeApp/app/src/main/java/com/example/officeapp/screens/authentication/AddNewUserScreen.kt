package com.example.officeapp.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.officeapp.R
import com.example.officeapp.models.user.UserRole
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.screens.reusableComponents.FormScreenHeader
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.screens.reusableComponents.OfficeFormDropdownField
import com.example.officeapp.screens.reusableComponents.OfficeFormTextField
import com.example.officeapp.screens.reusableComponents.PasswordField
import com.example.officeapp.ui.theme.BorderDark
import com.example.officeapp.ui.theme.BorderLight
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight
import com.example.officeapp.ui.theme.TextPrimaryDark
import com.example.officeapp.ui.theme.TextPrimaryLight
import com.example.officeapp.ui.theme.TextSecondaryDark
import com.example.officeapp.ui.theme.TextSecondaryLight
import com.example.officeapp.viewModels.AuthenticationViewModel

@Composable
fun AddNewUserScreen(
    viewModel: AuthenticationViewModel,
    isDarkTheme: Boolean,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val fieldContainerColor = if (isDarkTheme) DarkCard else LightSurface
    val textColor = if (isDarkTheme) TextPrimaryDark else TextPrimaryLight
    val secondaryTextColor = if (isDarkTheme) TextSecondaryDark else TextSecondaryLight
    val subtleBorderColor = if (isDarkTheme) BorderDark else BorderLight
    val primaryColor = if (isDarkTheme) PrimaryBlueDark else PrimaryBlueLight
    val fieldBorderColor = if (isDarkTheme) {
        PrimaryBlueDark.copy(alpha = 0.85f)
    } else {
        PrimaryBlueLight.copy(alpha = 0.75f)
    }

    fun resetForm() {
        firstName = ""
        lastName = ""
        email = ""
        password = ""
        confirmedPassword = ""
        selectedRole = null
        viewModel.clearMessages()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessages()
            onBack()
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp)
        ) {
            FormScreenHeader(
                title = stringResource(R.string.add_new_user_title),
                subtitle = stringResource(R.string.add_new_user_subtitle),
                textColor = textColor,
                subtitleColor = secondaryTextColor,
                borderColor = subtleBorderColor,
                iconColor = textColor,
                onBack = {
                    viewModel.clearMessages()
                    onBack()
                },
                onRefresh = { resetForm() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OfficeFormTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = stringResource(R.string.label_first_name),
                    icon = Icons.Outlined.Person,
                    required = true,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )

                OfficeFormTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = stringResource(R.string.label_last_name),
                    icon = Icons.Outlined.Person,
                    required = true,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )

                OfficeFormTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.label_email),
                    icon = Icons.Outlined.Email,
                    keyboardType = KeyboardType.Email,
                    required = true,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )

                PasswordField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.label_password),
                    modifier = Modifier.fillMaxWidth(),
                    required = true,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )

                PasswordField(
                    value = confirmedPassword,
                    onValueChange = { confirmedPassword = it },
                    label = stringResource(R.string.label_confirm_password),
                    modifier = Modifier.fillMaxWidth(),
                    required = true,
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )

                OfficeFormDropdownField(
                    selectedValue = selectedRole,
                    values = UserRole.entries,
                    label = stringResource(R.string.label_role),
                    icon = Icons.Outlined.AdminPanelSettings,
                    required = true,
                    itemText = { it.name },
                    onValueSelected = { selectedRole = it },
                    iconColor = primaryColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    containerColor = fieldContainerColor,
                    borderColor = fieldBorderColor
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            LoadingButton(
                text = stringResource(R.string.button_create_user),
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.addNewUser(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password,
                        confirmedPassword = confirmedPassword,
                        role = selectedRole ?: UserRole.DRIVER
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !uiState.isLoading
            )
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