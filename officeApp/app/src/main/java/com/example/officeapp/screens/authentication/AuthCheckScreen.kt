package com.example.officeapp.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.officeapp.viewModels.AuthenticationViewModel

@Composable
fun AuthCheckScreen(
    viewModel: AuthenticationViewModel,
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(uiState.isCheckingSession, uiState.isLoggedIn) {
        if(!uiState.isCheckingSession) {
            if(uiState.isLoggedIn)
                onAuthenticated()
            else
                onUnauthenticated()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Checking session...")
    }
}