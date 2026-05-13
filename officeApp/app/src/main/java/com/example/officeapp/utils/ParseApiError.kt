package com.example.officeapp.utils

import com.example.officeapp.models.GenericApplicationResponse
import com.google.gson.Gson
import retrofit2.Response

fun <T> parseApiError(response: Response<T>): ApiResult.Error {
    return try {
        val errorJson = response.errorBody()?.string()

        if(errorJson.isNullOrBlank()) {
            ApiResult.Error(
                message = "Server error: ${response.code()}",
                code = response.code().toString()
            )
        } else {
            val errorResponse = Gson().fromJson(errorJson, GenericApplicationResponse::class.java)
            ApiResult.Error(
                message = errorResponse.message ?: "Server error.",
                code = errorResponse.code ?: response.code().toString()
            )
        }
    } catch (ex: Exception) {
        ApiResult.Error(
            message = "Server error: ${response.code()}",
            code = response.code().toString()
        )
    }
}