package com.example.playlistmaker.playlist.model

sealed class PlaylistState {

    data object Empty : PlaylistState()

    data class Content(val playlist: List<Playlist>) : PlaylistState()

}