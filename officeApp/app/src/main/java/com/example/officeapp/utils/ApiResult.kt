package com.example.officeapp.utils

sealed class ApiResult<out T> {
    data class Success<T>(
        val data: T
    ): ApiResult<T>()

    data class Error(
        val message: String,
        val code: String? = null
    ): ApiResult<Nothing>()

    data object Loading: ApiResult<Nothing>()
}
