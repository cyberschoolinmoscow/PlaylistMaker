package com.practicum.playlistmaker.domain.models

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getReleaseYear(): CharSequence? =
        SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)

    fun serializeTrack(): String = Gson().toJson(this).toString()
    fun getDuration(): CharSequence? = SimpleDateFormat(
        "mm:ss",
        Locale.getDefault()
    ).format(trackTimeMillis)

    companion object {
        fun deserializeTrack(json: String?): Track = Gson().fromJson(json, Track::class.java)
    }
}