package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {

    suspend fun addTrackToFavorites(track:Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun getAllFavoriteTracks(): Flow<List<Track>>

    fun isTrackFavorite(trackId: Int): Flow<Boolean>
}