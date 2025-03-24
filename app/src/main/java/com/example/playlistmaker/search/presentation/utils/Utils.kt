package com.example.playlistmaker.search.presentation.utils

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track

fun Track.toDto(): TrackDto = TrackDto(
    trackName, artistName, trackTimeMillis, artworkUrl100, trackId, collectionName, releaseDate,
    primaryGenreName, country, previewUrl
)

fun TrackDto.toDomain(): Track = Track(
    trackName, artistName, trackTimeMillis, artworkUrl100, trackId, collectionName, releaseDate,
    primaryGenreName, country, previewUrl
)