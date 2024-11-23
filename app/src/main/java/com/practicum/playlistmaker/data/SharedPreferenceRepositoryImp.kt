package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.preferences.SettingsManager
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SharedPreferenceRepositoryImp(
    private val settingsManager: SettingsManager = SettingsManager(App.getContext())
) : SharedPreferenceRepository {

    override fun getBoolean(themeKey: String): Boolean {
        return settingsManager.sharedPreferences.getBoolean(themeKey, false)
    }

    override fun putBoolean(themeKey: String, valueDarkTheme: Boolean) {
        settingsManager.sharedPreferences.edit().putBoolean(themeKey, valueDarkTheme).apply()
    }

    override fun share() {
        settingsManager.share()
    }

    override fun help() {
        settingsManager.help()
    }

}
