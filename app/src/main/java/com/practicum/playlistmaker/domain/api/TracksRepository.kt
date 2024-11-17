package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
    fun clearHistory()
    abstract fun addHistory(): List<Track>
    fun addTrack(item: Track)
}