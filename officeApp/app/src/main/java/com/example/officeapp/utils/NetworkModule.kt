package com.example.officeapp.utils

import com.example.officeapp.interfacesAPI.AssignmentAiInterfaceAPI
import com.example.officeapp.interfacesAPI.TripInterfaceAPI
import com.example.officeapp.interfacesAPI.VehicleInterfaceAPI
import com.example.officeapp.interfacesAPI.AuthenticationInterfaceAPI
import com.example.officeapp.interfacesAPI.LocationInterfaceAPI
import com.example.officeapp.interfacesAPI.TripAssignmentInterfaceAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseWebSocketUrl

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val IP = "192.168.1.5:8081"
    private const val BASE_URL = "http://${IP}"
    private const val BASE_WS_URL = "ws://${IP}"

    @Provides
    @Singleton
    @BaseWebSocketUrl
    fun provideBaseWebSocketUrl(): String {
        return BASE_WS_URL
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationInterfaceAPI(retrofit: Retrofit): AuthenticationInterfaceAPI {
        return retrofit.create(AuthenticationInterfaceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideVehicleInterfaceAPI(retrofit: Retrofit): VehicleInterfaceAPI {
        return retrofit.create(VehicleInterfaceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideTripInterfaceAPI(retrofit: Retrofit): TripInterfaceAPI {
        return retrofit.create(TripInterfaceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideTripAssignmentInterfaceAPI(retrofit: Retrofit): TripAssignmentInterfaceAPI {
        return retrofit.create(TripAssignmentInterfaceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationInterfaceAPI(retrofit: Retrofit): LocationInterfaceAPI {
        return retrofit.create(LocationInterfaceAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAssignmentAiInterfaceAPI(retrofit: Retrofit): AssignmentAiInterfaceAPI {
        return retrofit.create(AssignmentAiInterfaceAPI::class.java)
    }
}