package com.practicum.playlistmaker.domain

interface SettingsInteractor {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(valueDarkTheme: Boolean)
}
