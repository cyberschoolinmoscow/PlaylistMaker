package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
//        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)


        val settingsInteractor = Creator.provideSettingsInteractor(this)

        darkTheme = settingsInteractor.getThemePreference()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
//        var editor: SharedPreferences.Editor
//        editor = sharedPreferences.edit()
//        editor.putBoolean("nightMode", darkTheme)
//        editor.apply()
    }

//    companion object {
//        lateinit var sharedPreferences: SharedPreferences
//    }
}