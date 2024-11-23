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

        settingsInteractor = Creator.getSettingInteractor(this)

        viewBinding.ivShare.setOnClickListener {
            settingsInteractor.share(this)
        }

        viewBinding.ivHelp.setOnClickListener {
            settingsInteractor.help(this)

//            val message = getString(R.string.help_btn_message)
//            val subject = getString(R.string.help_btn_subject)
//            val shareIntent = Intent(Intent.ACTION_SENDTO)
//            shareIntent.data = Uri.parse("mailto:")
//            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
//            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//            startActivity(shareIntent)
        }

        viewBinding.ivAgreement.setOnClickListener {
            val url = getString(R.string.agreementLink)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        viewBinding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            settingsInteractor.setDarkTheme(checked)
        }
    }
}