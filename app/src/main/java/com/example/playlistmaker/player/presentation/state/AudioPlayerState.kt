package com.example.playlistmaker.player.presentation.state

sealed class AudioPlayerState {
    object Prepared : AudioPlayerState()
    data class Playing(val currentPosition: String): AudioPlayerState()
    data class Paused(val lastPosition: String) : AudioPlayerState()
    object Stopped : AudioPlayerState()
}