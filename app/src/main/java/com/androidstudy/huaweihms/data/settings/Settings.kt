package com.androidstudy.huaweihms.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(
    private val settings: SharedPreferences
) {

    fun hasLoadedMapInfo(): Boolean {
        return settings.getBoolean(SettingsConstants.HAS_LOADED_MAP_INFO_KEY, false)
    }

    fun setHasLoadedMapInfo(has_loaded_map_info: Boolean) {
        settings.edit {
            putBoolean(SettingsConstants.HAS_LOADED_MAP_INFO_KEY, has_loaded_map_info)
        }
    }

    fun clearData() {
        settings.edit { clear() }
    }
}

object SettingsConstants {
    const val HAS_LOADED_MAP_INFO_KEY = "has_loaded_map_info"
}