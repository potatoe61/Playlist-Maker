package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    fun saveTrackToHistory(tracks: ArrayList<Track>)
    fun getHistoryTrack(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()

}
