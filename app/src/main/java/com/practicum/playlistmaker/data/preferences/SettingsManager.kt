package com.practicum.playlistmaker.data.preferences

import android.content.Context

class SettingsManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE)
}