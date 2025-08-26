package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(private val repository: FavoriteTrackRepository) :
    FavoriteTrackInteractor {

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return repository.getAllFavoriteTracks()
    }

    override fun isTrackFavorite(track: Track): Flow<Boolean> {
        return repository.isTrackFavorite(track.trackId)
    }

}