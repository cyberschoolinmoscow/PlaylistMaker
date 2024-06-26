package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide


class AudioPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSearchToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }
        myToolbar.title = ""
        val trackName: TextView = findViewById(R.id.trackName)
        val artistName: TextView = findViewById(R.id.artistName)
        val artistImage: ImageView = findViewById(R.id.artistImage)
        val trackDuration: TextView = findViewById(R.id.trackDuration)
        val collectionName: TextView = findViewById(R.id.collectionName)
        val releaseDate: TextView = findViewById(R.id.releaseDate)
        val primaryGenreName: TextView = findViewById(R.id.primaryGenreName)
        val country: TextView = findViewById(R.id.country)
        val collectionGroup: Group = findViewById(R.id.collectionGroup)
        val arguments = intent.extras
        val name = arguments!!.getString("track")
        val track = Track.deserializeTrack(name)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.getDuration()
        if (track.collectionName.isNullOrEmpty()) {
            collectionGroup.isVisible = false
        } else {
            collectionName.text = track.collectionName
        }
        releaseDate.text = track.getReleaseYear()
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(artistImage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}