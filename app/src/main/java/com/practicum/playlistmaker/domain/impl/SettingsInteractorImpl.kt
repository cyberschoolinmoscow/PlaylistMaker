package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SettingsInteractorImpl(
    private var repository: SharedPreferenceRepository
) : SettingsInteractor {
    private val THEME_KEY = "theme_key"
    override fun getDarkTheme(): Boolean {
        return repository.getBoolean(THEME_KEY)
    }

    override fun setDarkTheme(valueDarkTheme: Boolean) {
        repository.putBoolean(THEME_KEY, valueDarkTheme)
    }

    override fun getThemePreference(): Boolean {
        return getDarkTheme()
    }

    override fun share() {
        repository.share()
    }

    override fun help() {
        repository.help()
    }

}
