package com.example.playlistmaker.playlist.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUri: String,
    val tracksId: String = "[]",
    val tracksCount: Int = 0,
)