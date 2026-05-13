package com.example.officeapp.features.authentication

import com.example.officeapp.models.user.AddNewUserRequest
import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.user.LoginUserRequest
import com.example.officeapp.models.user.LoginUserResponse
import com.example.officeapp.utils.ApiResult
import com.example.officeapp.utils.JWTDecoder
import com.example.officeapp.utils.SessionManager
import com.example.officeapp.utils.parseApiError
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val authenticationInterfaceAPI: AuthenticationInterfaceAPI,
    private val sessionManager: SessionManager
) {
    suspend fun loginUser(email: String, hashedPassword: String): ApiResult<LoginUserResponse> {
        return try {
            val request = LoginUserRequest(
                email = email,
                hashedPassword = hashedPassword
            )

            val response = authenticationInterfaceAPI.loginUser(request)

            if (response.isSuccessful){
                val body = response.body()
                if (body == null)
                    ApiResult.Error("Empty response from server.")
                else {
                    sessionManager.saveLoginSession(
                        accessToken = body.accessToken,
                        tokenType = body.tokenType
                    )
                    val payload = JWTDecoder.decodePayload(body.accessToken)

                    payload?.userId?.let { userId ->
                        sessionManager.saveUserId(userId)
                    }
                    payload?.role?.let { role ->
                        sessionManager.saveUserRole(role)
                    }
                    ApiResult.Success(body)
                }
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(
                message = ex.message ?: "Unknown error at LoginUser."
            )
        }
    }

    suspend fun addNewUser(addNewUserRequest: AddNewUserRequest): ApiResult<GenericApplicationResponse> {
        return try {
            val response = authenticationInterfaceAPI.addNewUser(addNewUserRequest)

            if (response.isSuccessful) {
                val body = response.body()

                if (body == null)
                    ApiResult.Error("Empty response from server.")
                else
                    ApiResult.Success(body)
            } else {
                parseApiError(response)
            }
        } catch (ex: Exception) {
            ApiResult.Error(message = ex.message ?: "Unknown error at AddNewUser.")
        }
    }

    suspend fun getAccessToken(): String? {
        return sessionManager.getAccessTokenOnce()
    }

    suspend fun getUserRole(): String? {
        return sessionManager.getUserRole()
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }
}