package com.example.playlistmaker.player.presentation.state

sealed interface AddingTrackState {

    data class TrackAdded(val name: String) : AddingTrackState

    data class TrackAlreadyAdded(val name: String) : AddingTrackState
}