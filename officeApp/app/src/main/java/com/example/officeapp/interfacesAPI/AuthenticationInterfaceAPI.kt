package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.user.AddNewUserRequest
import com.example.officeapp.models.user.LoginUserRequest
import com.example.officeapp.models.user.LoginUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationInterfaceAPI {
    @Headers("Content-Type: application/json")
    @POST("/users/login")
    suspend fun loginUser(
        @Body request: LoginUserRequest
    ): Response<LoginUserResponse>

    @Headers("Content-Type: application/json")
    @POST("/users/create")
    suspend fun addNewUser(
        @Body request: AddNewUserRequest
    ): Response<GenericApplicationResponse>
}