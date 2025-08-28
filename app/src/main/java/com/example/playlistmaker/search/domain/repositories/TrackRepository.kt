package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    fun saveTrackToHistory(tracks: ArrayList<Track>)
    fun getHistoryTrack(): Flow<ArrayList<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}