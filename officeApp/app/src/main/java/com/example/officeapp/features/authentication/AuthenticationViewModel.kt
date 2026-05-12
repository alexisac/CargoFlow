package com.example.officeapp.features.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officeapp.models.UserRole
import com.example.officeapp.models.uiStates.AuthenticationUiState
import com.example.officeapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationService: AuthenticationService
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )
            when (
                val result = authenticationService.loginUser(
                email = email,
                password = password
                )
            ) {
                is ApiResult.Success -> {
                    val role = authenticationService.getUserRole()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        isCheckingSession = false,
                        userRole = role,
                        successMessage = "Success authentication.",
                        errorMessage = null
                    )

                    onSuccess()
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage = result.message,
                        successMessage = null
                    )
                }

                ApiResult.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun addNewUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String,
        role: UserRole
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            when (
                val result = authenticationService.addNewUser(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    confirmedPassword = confirmedPassword,
                    role = role
                )
            ){
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = result.data.message ?: "User was created with success.",
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        successMessage = null
                    )
                }

                ApiResult.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticationService.logout()

            _uiState.value = AuthenticationUiState(
                isLoading = false,
                isLoggedIn = false,
                isCheckingSession = false,
                userRole = null,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }

    fun checkSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCheckingSession = true,
                errorMessage = null,
                successMessage = null
            )

            val isValid = authenticationService.isUserSessionValid()

            val role = if (isValid) {
                authenticationService.getUserRole()
            } else {
                null
            }

            _uiState.value = _uiState.value.copy(
                isCheckingSession = false,
                isLoggedIn = isValid,
                userRole = role
            )
        }
    }
}