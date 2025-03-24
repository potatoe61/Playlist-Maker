package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            val result = repository.searchTracks(expression)
            when (result) {
                is Resource.Success -> consumer.consume(result.data)
                is Resource.Error -> consumer.onError(result.message)
            }
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Track>) {
        repository.saveTrackToHistory(tracks)
    }

    override fun getHistoryTrack(): ArrayList<Track> {
        return repository.getHistoryTrack()
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}