package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)

class TrackPreferences {
    companion object {
        fun read(sharedPreferences: SharedPreferences): List<Track> {
            if (sharedPreferences.getString(TRACK_KEY, null) == null) {
                Log.e("my", "null")
                return emptyList()
            } else {

                Log.e("my", "sharedPreferences.getString(TRACK_KEY, null).toString()")

                val json = sharedPreferences.getString(TRACK_KEY, null)
//                ?: return emptyList()
                if (json == null) {
                    return emptyList()
                }
                Log.e("my", sharedPreferences.getString(TRACK_KEY, null)!!)
                if (Gson().fromJson(json, Array<Track>::class.java).isNullOrEmpty()) {
                    return emptyList()
                }
                return Gson().fromJson(json, Array<Track>::class.java).toList()
            }
        }

        const val TRACK_KEY = "TRACK_KEY"
        const val MAX_TRACKS_SIZE: Int = 10
        val sharedPreferences: SharedPreferences = App.sharedPreferences

        fun writeTrackList(tracks: ArrayList<Track>?) {
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
            var json = sharedPreferences.getString(TRACK_KEY, null)
            writeTrackList(null)
        }
    }
}
