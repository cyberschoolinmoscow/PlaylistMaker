package com.practicum.playlistmaker.presentation.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityAudioPlayerBinding

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TRACK_TIME_UPDATE_DELAY = 300L
    }

    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { updateTrackTime() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSearchToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }
        myToolbar.title = ""
        val arguments = intent.extras
        val name = arguments!!.getString("track")
        val track = Track.deserializeTrack(name)
        viewBinding.trackName.text = track.trackName
        viewBinding.artistName.text = track.artistName
        viewBinding.trackDuration.text = track.getDuration()
        if (track.collectionName.isNullOrEmpty()) {
            viewBinding.collectionGroup.isVisible = false
        } else {
            viewBinding.collectionName.text = track.collectionName
        }
        viewBinding.releaseDate.text = track.getReleaseYear()
        viewBinding.primaryGenreName.text = track.primaryGenreName
        viewBinding.country.text = track.country
        Glide.with(this).load(track.getCoverArtwork()).centerCrop()
            .placeholder(R.drawable.placeholder).into(viewBinding.artistImage)
        preparePlayer(track.previewUrl)
        viewBinding.playBtn.setOnClickListener {
            playbackControl()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun updateTrackTime() {
        if (playerState == STATE_PLAYING) {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, TRACK_TIME_UPDATE_DELAY)
            viewBinding.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            viewBinding.playBtn.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            viewBinding.playBtn.background = getDrawable(R.drawable.play_button)
            playerState = STATE_PREPARED
            handler.removeCallbacks(runnable)
            viewBinding.trackTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        viewBinding.playBtn.background = getDrawable(R.drawable.pause_button)
        playerState = STATE_PLAYING
        updateTrackTime()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        viewBinding.playBtn.background = getDrawable(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(runnable)

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
        handler.removeCallbacks(runnable)
    }
}