package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {

        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun addHistory(): List<Track> {
        return repository.addHistory()
    }

    override fun addTrack(item: Track) {
        repository.addTrack(item)
    }
}