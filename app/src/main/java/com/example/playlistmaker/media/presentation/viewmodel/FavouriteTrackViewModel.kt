package com.example.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoriteTrackInteractor
import com.example.playlistmaker.media.model.FavoriteTrackState
import kotlinx.coroutines.launch

class FavoriteTrackViewModel(private val favoriteInteractor: FavoriteTrackInteractor) :
    ViewModel() {

    private val favoriteTrackLiveData = MutableLiveData<FavoriteTrackState>()
    fun favoriteTrackState(): LiveData<FavoriteTrackState> = favoriteTrackLiveData

    init {
        viewModelScope.launch {
            getFavoriteTracks()
        }
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            favoriteInteractor.getAllFavoriteTracks().collect {
                if (it.isEmpty()) setState(FavoriteTrackState.Empty) else setState(
                    FavoriteTrackState.Content(it)
                )
            }
        }
    }

    private fun setState(state: FavoriteTrackState) {
        favoriteTrackLiveData.postValue(state)
    }
}