package com.example.officeapp.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        private val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME = stringPreferencesKey("user_last_name")
        private val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val userId: Flow<Long?> = context.dataStore.data
        .map { prefs -> prefs[USER_ID] }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[ACCESS_TOKEN] }

    val tokenType: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[TOKEN_TYPE] }

    val userRole: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[USER_ROLE] }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_THEME] ?: true }

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

    suspend fun saveUserFirstName(firstName: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_FIRST_NAME] = firstName
        }
    }

    suspend fun saveUserLastName(lastName: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_LAST_NAME] = lastName
        }
    }

    suspend fun saveDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME] = isDarkTheme
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

    suspend fun getUserFirstName(): String? {
        return context.dataStore.data
            .map { prefs -> prefs[USER_FIRST_NAME] }
            .firstOrNull()
    }

    suspend fun getUserLastName(): String? {
        return context.dataStore.data
            .map{ prefs -> prefs[USER_LAST_NAME] }
            .firstOrNull()
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_ID)
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(TOKEN_TYPE)
            prefs.remove(USER_ROLE)
            prefs.remove(USER_FIRST_NAME)
            prefs.remove(USER_LAST_NAME)
        }
    }
}