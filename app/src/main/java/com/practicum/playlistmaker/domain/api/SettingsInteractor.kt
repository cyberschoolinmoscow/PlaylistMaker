package com.practicum.playlistmaker.domain.api

import android.content.Context

interface SettingsInteractor {
    fun getDarkTheme(): Boolean
    fun setDarkTheme(valueDarkTheme: Boolean)
    fun getThemePreference(): Boolean
    fun share(context: Context)
    abstract fun help(context: Context)
}
