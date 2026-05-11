package com.example.officeapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("session_prefs")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private val USER_ID = longPreferencesKey("user_id")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val TOKEN_TYPE = stringPreferencesKey("token_type")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    val userId: Flow<Long?> = context.dataStore.data
        .map { prefs -> prefs[USER_ID] }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[ACCESS_TOKEN] }

    val tokenType: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[TOKEN_TYPE] }

    val userRole: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[USER_ROLE] }

    suspend fun saveUserId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    suspend fun saveLoginSession(accessToken: String, tokenType: String){
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[TOKEN_TYPE] = tokenType
        }
    }

    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ROLE] = role
        }
    }

    suspend fun getUserIdOnce(): Long? {
        return context.dataStore.data
            .map{ prefs -> prefs[USER_ID]}
            .firstOrNull()
    }

    suspend fun getAccessTokenOnce(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[ACCESS_TOKEN] }
            .firstOrNull()
    }

    suspend fun getTokenTypeOnce(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[TOKEN_TYPE]}
            .firstOrNull()
    }

    suspend fun getUserRole(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[USER_ROLE] }
            .firstOrNull()
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}