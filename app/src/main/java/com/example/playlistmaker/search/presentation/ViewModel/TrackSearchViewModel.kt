package com.example.playlistmaker.search.presentation.ViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.utils.SingleEventLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    interactor: TrackInteractor
) : ViewModel() {

    var currentRequestId: Int = 0
    private var currentQuery = ""

    private var isClickAllowed = true
    private val tracksInteractor = interactor
    private var searchJob: Job? = null

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun getScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    private val clickDebounceLiveData = SingleEventLiveData<Track>()
    fun getClickDebounce(): LiveData<Track> = clickDebounceLiveData

    init {
        setHistoryTrackList()
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        setHistoryTrackList()
    }

    fun setHistoryTrackList() {
        val historyTrackList = tracksInteractor.getHistoryTrack()
        screenStateLiveData.postValue(
            if (historyTrackList.isEmpty()) SearchScreenState.EmptyHistory else
                SearchScreenState.HistoryContent(historyTrackList)
        )
    }

    private fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun makeSearch(query: String) {

        if (query.isNotEmpty()) {
            currentRequestId = (currentRequestId + 1) % MAX_REQUEST_ID
            val requestId = currentRequestId

            screenStateLiveData.postValue(SearchScreenState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        val foundTracks = pair.first
                        val errorMessage = pair.second

                        when {
                            requestId != currentRequestId -> return@collect

                            errorMessage != null -> screenStateLiveData.postValue(
                                SearchScreenState.NoConnection
                            )

                            foundTracks.isNullOrEmpty() -> screenStateLiveData.postValue(
                                SearchScreenState.NothingFound
                            )

                            else -> screenStateLiveData.postValue(
                                SearchScreenState.Success(foundTracks)
                            )
                        }
                    }
            }
        }
    }

    fun searchDebounce(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (currentQuery != query) {
                currentQuery = query
                delay(SEARCH_DEBOUNCE_DELAY_IN_SECONDS)
                makeSearch(query)
            }
        }
    }

    fun clickDebounce(track: Track) {
        if (isClickAllowed) {
            isClickAllowed = false
            addTrackToHistory(track)
            clickDebounceLiveData.value = track
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_IN_SECONDS)
                isClickAllowed = true
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_SECONDS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_IN_SECONDS = 1000L
        private const val MAX_REQUEST_ID = 1000
    }

    fun searchJobCancel() {
        searchJob?.cancel()
    }
}