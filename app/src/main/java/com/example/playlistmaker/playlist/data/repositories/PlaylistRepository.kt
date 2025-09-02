package com.example.playlistmaker.playlist.data.repositories

import android.net.Uri
import com.example.playlistmaker.playlist.presentation.state.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist): Long

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun saveImage(uri: Uri): String?
}