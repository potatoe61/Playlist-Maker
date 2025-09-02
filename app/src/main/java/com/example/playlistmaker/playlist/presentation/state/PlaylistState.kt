package com.example.playlistmaker.playlist.presentation.state

sealed class PlaylistState {

    data object Empty : PlaylistState()

    data class Content(val playlist: List<Playlist>) : PlaylistState()

}