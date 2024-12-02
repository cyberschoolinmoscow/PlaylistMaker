package com.practicum.playlistmaker.domain.api

interface SettingsInteractor {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(valueDarkTheme: Boolean)
    fun getThemePreference(): Boolean
    fun share()
    fun help()
}
