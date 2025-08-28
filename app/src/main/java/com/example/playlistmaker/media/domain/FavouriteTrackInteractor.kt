package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun getAllFavoriteTracks(): Flow<List<Track>>

    fun isTrackFavorite(track: Track): Flow<Boolean>
}