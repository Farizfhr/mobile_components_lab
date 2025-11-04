package com.faris165.mobilecomponentslab.datastore


import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore("user_prefs")

object UserPrefsKeys {
    val NAME = stringPreferencesKey("name")
    val NOTE = stringPreferencesKey("note")
}

class UserPrefsRepo(private val context: Context) {
    val data: Flow<Pair<String, String>> = context.userDataStore.data.map { p: Preferences ->
        (p[UserPrefsKeys.NAME] ?: "") to (p[UserPrefsKeys.NOTE] ?: "")
    }
    suspend fun save(name: String, note: String) {
        context.userDataStore.edit { p ->
            p[UserPrefsKeys.NAME] = name
            p[UserPrefsKeys.NOTE] = note
        }
    }
}