package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var play: ImageButton
    private var mediaPlayer = MediaPlayer()

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
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
        play = findViewById(R.id.play_btn)
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
        preparePlayer(track.previewUrl)
        play.setOnClickListener {
            playbackControl()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.background = getDrawable(R.drawable.play_button)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.background = getDrawable(R.drawable.pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.background = getDrawable(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}