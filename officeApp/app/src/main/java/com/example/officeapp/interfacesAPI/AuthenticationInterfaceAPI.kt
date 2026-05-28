package com.example.officeapp.interfacesAPI

import com.example.officeapp.models.GenericApplicationResponse
import com.example.officeapp.models.user.AddNewUserRequest
import com.example.officeapp.models.user.ChangeUserStatusRequest
import com.example.officeapp.models.user.GetAllUsersResponse
import com.example.officeapp.models.user.LoginUserRequest
import com.example.officeapp.models.user.LoginUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Headers("Content-Type: application/json")
    @GET("users/getAll")
    suspend fun getAllUsers(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<GetAllUsersResponse>

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}/changeStatus")
    suspend fun changeUserStatus(
        @Path("userId") userId: Long,
        @Body request: ChangeUserStatusRequest
    ): Response<GenericApplicationResponse>
}