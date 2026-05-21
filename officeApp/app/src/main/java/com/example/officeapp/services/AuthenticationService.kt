package com.example.officeapp.services

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.user.AddNewUserRequest
import com.example.officeapp.models.user.LoginUserResponse
import com.example.officeapp.models.user.UserRole
import com.example.officeapp.repositories.AuthenticationRepository
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.JWTDecoder
import com.example.officeapp.utils.PasswordHasher
import javax.inject.Inject

class AuthenticationService @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun loginUser(email: String, password: String): ApiResult<LoginUserResponse> {
        if(email.isBlank())
            return ApiResult.Error(ValidationMessages.EMAIL_REQUIRED)

        if(email.length < 5)
            return ApiResult.Error(ValidationMessages.EMAIL_MIN_LENGTH)

        if(password.isBlank())
            return ApiResult.Error(ValidationMessages.PASSWORD_REQUIRED)

        val hashedPassword = PasswordHasher.sha256(password)

        return authenticationRepository.loginUser(
            email = email.trim(),
            hashedPassword = hashedPassword
        )
    }

    suspend fun addNewUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmedPassword: String,
        role: UserRole
    ): ApiResult<GenericApplicationResponse> {
        if(firstName.length < 3)
            return ApiResult.Error(ValidationMessages.FIRST_NAME_MIN_LENGTH)

        if(lastName.length < 3)
            return ApiResult.Error(ValidationMessages.LAST_NAME_MIN_LENGTH)

        if(email.length < 5)
            return ApiResult.Error(ValidationMessages.EMAIL_MIN_LENGTH)

        if(password.length < 8)
            return ApiResult.Error(ValidationMessages.PASSWORD_MIN_LENGTH)

        if(password != confirmedPassword)
            return ApiResult.Error(ValidationMessages.PASSWORDS_DO_NOT_MATCH)

        val hashedPassword = PasswordHasher.sha256(password)

        val request = AddNewUserRequest(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email.trim(),
            hashedPassword = hashedPassword,
            role = role
        )

        return authenticationRepository.addNewUser(request)
    }

    suspend fun isUserSessionValid(): Boolean {
        val token = authenticationRepository.getAccessToken()

        if (token == null) {
            return false
        }

        val isExpired = JWTDecoder.isTokenExpiredOrExpiresToday(token)

        if (isExpired) {
            authenticationRepository.logout()
            return false
        }
        return true
    }

    suspend fun getUserRole(): String? {
        return authenticationRepository.getUserRole()
    }

    suspend fun logout() {
        authenticationRepository.logout()
    }
}