package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var myToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myToolbar = findViewById(R.id.idMainToolbar)
        setSupportActionBar(myToolbar)
        val buttonSearch = findViewById<MaterialButton>(R.id.idSearch)
        val buttonMediateka = findViewById<MaterialButton>(R.id.idMediateka)
        val buttonSettings = findViewById<MaterialButton>(R.id.idSettings)
        buttonSearch.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        })
        buttonMediateka.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    MediatekaActivity::class.java
                )
            )
        })
        buttonSettings.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        })

    }
}