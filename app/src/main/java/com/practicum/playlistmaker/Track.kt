package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
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
    val country: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun serializeTrack(): String = Gson().toJson(this).toString()
    fun getDuration(): CharSequence? = SimpleDateFormat(
        "mm:ss",
        Locale.getDefault()
    ).format(trackTimeMillis)

    companion object {
        fun deserializeTrack(json: String?): Track = Gson().fromJson(json, Track::class.java)
    }
}

private fun Gson.fromJson(json: Gson, java: Class<Track>): Track =
    Gson().fromJson(json, Track::class.java)

class TrackPreferences {
    companion object {
        fun read(sharedPreferences: SharedPreferences): List<Track> {
            if (sharedPreferences.getString(TRACK_KEY, null) == null) {
                Log.e("my", "null")
                return emptyList()
            } else {

                Log.e("my", "sharedPreferences.getString(TRACK_KEY, null).toString()")

                val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
                Log.e("my", sharedPreferences.getString(TRACK_KEY, null)!!)
                if (Gson().fromJson(json, Array<Track>::class.java).isNullOrEmpty()) {
                    return emptyList()
                }
                return Gson().fromJson(json, Array<Track>::class.java).toList()
            }
        }

        private const val TRACK_KEY = "TRACK_KEY"
        private const val MAX_TRACKS_SIZE: Int = 10
        private val sharedPreferences: SharedPreferences = App.sharedPreferences

        private fun writeTrackList(tracks: ArrayList<Track>?) {
            val json = Gson().toJson(tracks)
            sharedPreferences.edit()
                .putString(TRACK_KEY, json)
                .apply()
        }

        fun writeTrack(track: Track) {
            var json = sharedPreferences.getString(TRACK_KEY, null)
            if (json.isNullOrEmpty()) {
                var tracks = ArrayList<Track>()
                tracks.add(0, track)
                writeTrackList(tracks)
            } else {
                var tracks = ArrayList<Track>()
                if (!Gson().fromJson(json, Array<Track>::class.java).isNullOrEmpty()) {
                    tracks.addAll(Gson().fromJson(json, Array<Track>::class.java))
                }
                if (!tracks.contains(track)) {
                    if (tracks.size == MAX_TRACKS_SIZE) {
                        tracks.removeAt(MAX_TRACKS_SIZE - 1)
                    }
                    tracks.add(0, track)

                } else {
                    tracks.removeAt(tracks.indexOf(track))
                    tracks.add(0, track)
                }
                writeTrackList(tracks)
            }
        }

        fun removeAll() {
            writeTrackList(null)
        }
    }
}
