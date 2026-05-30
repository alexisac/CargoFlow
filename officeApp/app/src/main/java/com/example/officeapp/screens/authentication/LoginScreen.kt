package com.example.officeapp.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.officeapp.R
import com.example.officeapp.screens.reusableComponents.FormMessages
import com.example.officeapp.viewModels.AuthenticationViewModel
import com.example.officeapp.screens.reusableComponents.LoadingButton
import com.example.officeapp.screens.reusableComponents.OfficeFormTextField
import com.example.officeapp.screens.reusableComponents.PasswordField
import com.example.officeapp.screens.reusableComponents.ThemeToggle
import com.example.officeapp.ui.theme.DarkBackground
import com.example.officeapp.ui.theme.DarkCard
import com.example.officeapp.ui.theme.LightBackground
import com.example.officeapp.ui.theme.LightSurface
import com.example.officeapp.ui.theme.PrimaryBlueDark
import com.example.officeapp.ui.theme.PrimaryBlueLight

@Composable
fun LoginScreen(
    viewModel: AuthenticationViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onLoggedIn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val isDark = isDarkTheme

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val backgroundColor = if (isDark) DarkBackground else LightBackground
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val fieldContainerColor = if (isDark) DarkCard else LightSurface
    val primaryColor = if (isDark) PrimaryBlueDark else PrimaryBlueLight
    val fieldBorderColor = if (isDark) {
        PrimaryBlueDark.copy(alpha = 0.85f)
    } else {
        PrimaryBlueLight.copy(alpha = 0.75f)
    }

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = if (isDark) {
                        R.drawable.logo_dark
                    } else {
                        R.drawable.logo_light
                    }
                ),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.login_screen_title),
                color = textColor,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_screen_subtitle),
                color = secondaryTextColor,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(52.dp))

            OfficeFormTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.label_email),
                icon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                iconColor = primaryColor,
                textColor = MaterialTheme.colorScheme.onSurface,
                secondaryTextColor = secondaryTextColor,
                containerColor = fieldContainerColor,
                borderColor = fieldBorderColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            PasswordField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.label_password),
                iconColor = primaryColor,
                textColor = MaterialTheme.colorScheme.onSurface,
                secondaryTextColor = secondaryTextColor,
                containerColor = fieldContainerColor,
                borderColor = fieldBorderColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(44.dp))

            LoadingButton(
                text = stringResource(R.string.button_login),
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.loginUser(
                        email = email,
                        password = password,
                        onSuccess = onLoggedIn
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                enabled = !uiState.isLoading
            )
        }

        ThemeToggle(
            isDarkTheme = isDark,
            onThemeChange = onThemeChange,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 42.dp,
                    end = 24.dp
                )
        )

        FormMessages(
            errorMessage = uiState.errorMessage,
            successMessage = uiState.successMessage,
            isDarkTheme = isDark,
            onMessageShown = { viewModel.clearMessages() }
        )
    }
}