package com.androidstudy.huaweihms.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(
    private val settings: SharedPreferences
) {

    fun isLoggedIn(): Boolean {
        return settings.getBoolean(SettingsConstants.LOGGED_IN_KEY, false)
    }

    fun setIsLoggedIn(is_logged_in: Boolean) {
        settings.edit {
            putBoolean(SettingsConstants.LOGGED_IN_KEY, is_logged_in)
        }
    }

    fun clearData() {
        settings.edit { clear() }
    }
}

object SettingsConstants {
    const val LOGGED_IN_KEY = "is_logged_in"
}