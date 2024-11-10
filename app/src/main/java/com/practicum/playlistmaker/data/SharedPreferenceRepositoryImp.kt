package com.practicum.playlistmaker.data

import android.content.Context
import com.practicum.playlistmaker.App.Companion.sharedPreferences
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository

class SharedPreferenceRepositoryImp(context: Context) :
    SharedPreferenceRepository {
    override fun getBoolean(themeKey: String): Boolean {
        return sharedPreferences.getBoolean(themeKey, false)
    }

    override fun putBoolean(themeKey: String, valueDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(themeKey, valueDarkTheme).apply()
    }

}
