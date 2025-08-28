package com.example.playlistmaker.media.model

import com.example.playlistmaker.search.domain.model.Track

sealed class FavoriteTrackState {

    data object Empty : FavoriteTrackState()

    data class Content(val tracks: List<Track>) : FavoriteTrackState()

}