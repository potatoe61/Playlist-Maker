package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.data.dto.TrackDto

interface SearchHistoryRepository {
    fun saveTrackToHistory(tracks: ArrayList<TrackDto>)
    fun getHistoryTrack(): List<TrackDto>
    fun addTrackToHistory(track: TrackDto)
    fun clearHistory()
    fun registerChangeListener(onChange: () -> Unit)
}