package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.model.Track

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>

    fun saveTrackToHistory(tracks: ArrayList<Track>)
    fun getHistoryTrack(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}