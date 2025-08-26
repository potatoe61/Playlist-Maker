package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import java.util.concurrent.Executors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
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