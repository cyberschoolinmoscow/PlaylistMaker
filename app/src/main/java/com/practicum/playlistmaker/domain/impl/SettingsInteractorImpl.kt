package com.practicum.playlistmaker.domain.impl

import android.content.Context
import com.practicum.playlistmaker.data.SharedPreferenceRepositoryImp
import com.practicum.playlistmaker.domain.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SettingsInteractorImpl(context: Context) :
    SettingsInteractor {
    private val THEME_KEY = "theme_key"

    private val sharedPreferenceRepository: SharedPreferenceRepository =
        SharedPreferenceRepositoryImp(context)

    override fun getDarkTheme(): Boolean {
        return sharedPreferenceRepository.getBoolean(THEME_KEY)
    }

    override fun setDarkTheme(valueDarkTheme: Boolean) {
        sharedPreferenceRepository.putBoolean(THEME_KEY, valueDarkTheme)
    }

}
