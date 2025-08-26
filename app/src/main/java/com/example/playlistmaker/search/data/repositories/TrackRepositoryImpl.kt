package com.example.playlistmaker.search.data.repositories

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.toDomain
import com.example.playlistmaker.media.toDto
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val appDatabase: AppDatabase,
) : TrackRepository {

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val ERROR = "Error"
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as TrackResponse) {
                    val favoritesTrack = appDatabase.trackDao().getTracksId()
                    val data = results.map {
                        it.toDomain()
                    }
                    data.filter {
                        it.trackId in favoritesTrack
                    }.map { it.copy(isFavorite = true) }
                    emit(Resource.Success(data))
                }
            }

            -1 -> {
                emit(Resource.Error(NO_INTERNET_CONNECTION))
            }

            else -> {
                emit(Resource.Error("$ERROR: ${response.resultCode}"))
            }
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Track>) {
        val dtoTracks = tracks.map { it.toDto() }
        searchHistoryRepository.saveTrackToHistory(ArrayList(dtoTracks))
    }

    override fun getHistoryTrack(): Flow<ArrayList<Track>> = flow {
        val dtoTracks = searchHistoryRepository.getHistoryTrack()
        val favoritesTracks = appDatabase.trackDao().getTracksId()

        val tracks = dtoTracks.map { it.toDomain() }
        tracks.filter {
            it.trackId in favoritesTracks
        }.map { it.copy(isFavorite = true) }

        emit(ArrayList(tracks))
    }

    override fun addTrackToHistory(track: Track) {
        val dtoTrack = track.toDto()
        searchHistoryRepository.addTrackToHistory(dtoTrack)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}