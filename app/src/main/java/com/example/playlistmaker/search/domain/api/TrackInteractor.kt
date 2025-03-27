package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun saveTrackToHistory(tracks: ArrayList<Track>)
    fun getHistoryTrack(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?)
        fun onError(errorMessage: String?)
    }
}
