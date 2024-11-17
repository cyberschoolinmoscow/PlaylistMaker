package com.practicum.playlistmaker.data.preferences

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track

class TrackManager(context: Context) {
    fun clearHistory() {
        removeAll()
    }

    val sharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE)

    fun read(): List<Track> {
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

    private val TRACK_KEY = "TRACK_KEY"
    private val MAX_TRACKS_SIZE: Int = 10
//        private val sharedPreferences: SharedPreferences

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