package com.example.playlistmaker.data.dr

import com.example.playlistmaker.domain.models.Track

data class TrackResponse(val resultCount: Int, val results: List<Track>)
