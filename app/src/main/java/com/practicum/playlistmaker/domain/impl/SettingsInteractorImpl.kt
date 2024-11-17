package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SettingsInteractorImpl(var repository: SharedPreferenceRepository) :
    SettingsInteractor {
    private val THEME_KEY = "theme_key"

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

}
