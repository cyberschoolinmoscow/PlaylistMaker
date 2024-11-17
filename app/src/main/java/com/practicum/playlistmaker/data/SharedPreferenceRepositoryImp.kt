package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.preferences.SettingsManager
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SharedPreferenceRepositoryImp(var settingsManager: SettingsManager) :
    SharedPreferenceRepository {
    override fun getBoolean(themeKey: String): Boolean {
        return settingsManager.sharedPreferences.getBoolean(themeKey, false)
    }

    override fun putBoolean(themeKey: String, valueDarkTheme: Boolean) {
        settingsManager.sharedPreferences.edit().putBoolean(themeKey, valueDarkTheme).apply()
    }

    override fun saveThemePreferences(darkTheme: Boolean) {
        putBoolean("nightMode", darkTheme)
    }

}
