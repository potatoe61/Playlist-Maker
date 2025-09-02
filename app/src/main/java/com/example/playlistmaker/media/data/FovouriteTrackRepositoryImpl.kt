package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.domain.FavoriteTrackRepository
import com.example.playlistmaker.media.toFavoriteEntity
import com.example.playlistmaker.media.toTrackEntity
import com.example.playlistmaker.media.toTracksDomain
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(private val appDatabase: AppDatabase) :
    FavoriteTrackRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDaoFavorite().insertTrack(track.toFavoriteEntity())
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase.trackDaoFavorite().deleteTrack(track.toFavoriteEntity())
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDaoFavorite().getTracks().map { it.toTracksDomain() }
        emit(tracks)
    }

    override fun isTrackFavorite(trackId: Int): Flow<Boolean> = flow {
        emit(appDatabase.trackDaoFavorite().isTrackFavorite(trackId))
    }
}