package com.example.playlistmaker.search.data.repositories

import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.presentation.utils.toDomain
import com.example.playlistmaker.search.presentation.utils.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryRepository: SearchHistoryRepository
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
                    val data = results.map {
                        it.toDomain()
                    }
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

    override fun getHistoryTrack(): ArrayList<Track> {
        val dtoTracks = searchHistoryRepository.getHistoryTrack()
        return ArrayList(dtoTracks.map { it.toDomain() })
    }

    override fun addTrackToHistory(track: Track) {
        val dtoTrack = track.toDto()
        searchHistoryRepository.addTrackToHistory(dtoTrack)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}