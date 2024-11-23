package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.preferences.TrackManager
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackManager: TrackManager
) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {

        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId.toInt(),
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis.toInt(),
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }

        } else {
            return emptyList()
        }
    }

    override fun clearHistory() {
        trackManager.clearHistory()
    }

    override fun addHistory(): List<Track> {
        return trackManager.read()
    }

    override fun addTrack(item: Track) {
        trackManager.writeTrack(item)
    }
}