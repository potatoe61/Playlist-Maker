package com.example.playlistmaker.player.presentation.state

sealed class AudioPlayerState {
    data class State(val playerState: PlayerState2): AudioPlayerState()
    data class Playing(val currentPosition: String): AudioPlayerState()
}