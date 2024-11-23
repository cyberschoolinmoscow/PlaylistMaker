package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

class App : Application() {

    //    var darkTheme = false
//public lateinit var context:Context
//  var  darkTheme: Boolean=false
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val settingsInteractor = Creator.provideSettingsInteractor()


        darkTheme = settingsInteractor.getThemePreference()
        settingsInteractor.setDarkTheme(settingsInteractor.getThemePreference())
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
    companion object {
        fun getContext(): Context {
            return context
        }

        private lateinit var context: Context
        var darkTheme: Boolean = false

    }
}
