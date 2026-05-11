package com.example.officeapp.utils

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val noAuth = originalRequest.header(Strings.NO_AUTH) == "true"

        if (noAuth) {
            val cleanRequest = originalRequest.newBuilder()
                .removeHeader(Strings.NO_AUTH)
                .build()
            return chain.proceed(cleanRequest)
        }

        val authHeader = runBlocking {
            if(sessionManager.getTokenTypeOnce() != null && sessionManager.getAccessTokenOnce() != null)
                "${sessionManager.getTokenTypeOnce()} ${sessionManager.getAccessTokenOnce()}"
            else
                null
        }

        val newRequest = if(authHeader != null) {
            originalRequest.newBuilder()
                .addHeader(Strings.AUTHORIZATION, authHeader)
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}