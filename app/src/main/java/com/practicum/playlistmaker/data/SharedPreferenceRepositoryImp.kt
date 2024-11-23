package com.practicum.playlistmaker.data

import android.content.Context
import com.practicum.playlistmaker.data.preferences.SettingsManager
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SharedPreferenceRepositoryImp(
    val context: Context,
    val settingsManager: SettingsManager = SettingsManager(context)
) : SharedPreferenceRepository {

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
