package com.practicum.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.api.SettingsInteractor


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySettingsBinding

    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSettingsToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }

        settingsInteractor = Creator.getSettingInteractor()

        viewBinding.ivShare.setOnClickListener {
            settingsInteractor.share()
        }

        viewBinding.ivHelp.setOnClickListener {
            settingsInteractor.help()
        }

        viewBinding.ivAgreement.setOnClickListener {
            val url = getString(R.string.agreementLink)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
        viewBinding.themeSwitcher.isChecked = App.darkTheme
        viewBinding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            settingsInteractor.setDarkTheme(checked)
            settingsInteractor.setDarkTheme(viewBinding.themeSwitcher.isChecked)
            App.darkTheme = settingsInteractor.getDarkTheme()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsInteractor.setDarkTheme(viewBinding.themeSwitcher.isChecked)
        App.darkTheme = settingsInteractor.getDarkTheme()
    }
}