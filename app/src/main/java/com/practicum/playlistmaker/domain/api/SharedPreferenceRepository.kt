package com.practicum.playlistmaker.domain.api

interface SharedPreferenceRepository {
    fun getBoolean(themeKey: String): Boolean
    fun putBoolean(themeKey: String, valueDarkTheme: Boolean)

}
