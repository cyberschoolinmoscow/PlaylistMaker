package com.practicum.playlistmaker.domain.impl

import android.content.Context
import com.practicum.playlistmaker.data.preferences.SettingsManager
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SettingsInteractorImpl(
    var repository: SharedPreferenceRepository
) :
    SettingsInteractor {
    private val THEME_KEY = "theme_key"
    private lateinit var settingsManager: SettingsManager
//    private val sharedPreferenceRepository: SharedPreferenceRepository =
//        SharedPreferenceRepositoryImp(context)

    override fun getDarkTheme(): Boolean {
        return repository.getBoolean(THEME_KEY)
    }

    override fun setDarkTheme(valueDarkTheme: Boolean) {
        repository.putBoolean(THEME_KEY, valueDarkTheme)
    }

    override fun getThemePreference(): Boolean {
        return getDarkTheme()
    }

    override fun share(context: Context) {
        settingsManager = SettingsManager(context)
        settingsManager.share(context)
    }

    override fun help(context: Context) {
        settingsManager = SettingsManager(context)
        settingsManager.help(context)
    }

}
