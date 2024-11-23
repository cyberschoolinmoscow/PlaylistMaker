package com.practicum.playlistmaker.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.presentation.mediateka.MediatekaActivity
import com.practicum.playlistmaker.presentation.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.tracks.SearchActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var myToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        myToolbar = findViewById(R.id.idMainToolbar)
        setSupportActionBar(myToolbar)

        viewBinding.idSearch.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        })

        viewBinding.idMediateka.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    MediatekaActivity::class.java
                )
            )
        })

        viewBinding.idSettings.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        })

        val settingsInteractor = Creator.getSettingInteractor(this)
        settingsInteractor.setDarkTheme(settingsInteractor.getDarkTheme())

    }
}