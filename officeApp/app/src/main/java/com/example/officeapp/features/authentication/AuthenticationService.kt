package com.example.officeapp.features.authentication

import com.example.officeapp.models.AddNewUserRequest
import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.LoginUserResponse
import com.example.officeapp.models.UserRole
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.JWTDecoder
import com.example.officeapp.utils.PasswordHasher
import javax.inject.Inject

class AuthenticationService @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun loginUser(email: String, password: String): ApiResult<LoginUserResponse> {
        if(email.isBlank())
            return ApiResult.Error("Email is required")

        if(email.length < 5)
            return ApiResult.Error("Email needs to has minimum 5 characters")

        if(password.isBlank())
            return ApiResult.Error("Password is required")

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
        role: UserRole
    ): ApiResult<GenericApplicationResponse> {
        if(firstName.length < 3)
            return ApiResult.Error("FirstName needs to has minimum 3 characters")

        if(lastName.length < 3)
            return ApiResult.Error("FirstName needs to has minimum 3 characters")

        if(email.length < 5)
            return ApiResult.Error("Email needs to has minimum 5 characters")

        if(password.length < 8)
            return ApiResult.Error("Password needs to has minimum 8 characters")

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

    suspend fun logout() {
        authenticationRepository.logout()
    }
}