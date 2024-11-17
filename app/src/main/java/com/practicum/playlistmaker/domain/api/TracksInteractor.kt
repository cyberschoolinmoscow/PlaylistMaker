package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun clearHistory()
    fun addHistory(): List<Track>
    fun addTrack(item: Track)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}