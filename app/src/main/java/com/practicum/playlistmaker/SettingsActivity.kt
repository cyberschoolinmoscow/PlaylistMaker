package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSettingsToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }

//        val switchBtn: SwitchCompat = findViewById(R.id.switchBtn)
        val shareBtn: ImageView = findViewById(R.id.iv_share)
        val helpBtn: ImageView = findViewById(R.id.iv_help)
        val agreementBtn: ImageView = findViewById(R.id.iv_agreement)

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = getString(R.string.share_btn_link)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "Share"))
        }
        helpBtn.setOnClickListener {
            val message = getString(R.string.help_btn_message)
            val subject = getString(R.string.help_btn_subject)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(shareIntent)
        }
        agreementBtn.setOnClickListener {
            val url = getString(R.string.agreementLink)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

//        var editor: SharedPreferences.Editor
//        val sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
//        var nightMode: Boolean = sharedPreferences.getBoolean("nightMode", false)
//
//        if (nightMode) {
//            switchBtn.isChecked = true
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//        switchBtn.setOnClickListener {
//            if (nightMode) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                editor = sharedPreferences.edit()
//                editor.putBoolean("nightMode", false)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                editor = sharedPreferences.edit()
//                editor.putBoolean("nightMode", true)
//            }
//            editor.apply()
//        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}