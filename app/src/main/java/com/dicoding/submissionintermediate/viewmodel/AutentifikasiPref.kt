package com.dicoding.submissionintermediate.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.submissionintermediate.respon.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AutentifikasiPref private constructor(private val dataStore: DataStore<Preferences>) {
    fun getUser(): Flow<ModelUser> {
        return dataStore.data.map { preferences ->
            ModelUser(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[TOKEN_KEY] ?:"",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[USER_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[TOKEN_KEY] = ""
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AutentifikasiPref? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_KEY = stringPreferencesKey("userid")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): AutentifikasiPref {
            return INSTANCE ?: synchronized(this) {
                val instance = AutentifikasiPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}