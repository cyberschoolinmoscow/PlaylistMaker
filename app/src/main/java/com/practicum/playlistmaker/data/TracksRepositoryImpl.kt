package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {

        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
//                Track(it.trackName, it.artistName, it.trackTime, it.artworkUrl100, it.collectionName,it.releaseDate,it.primaryGenreName,it.country,it.previewUrl)  }
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
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
}