package com.example.playlistmaker.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoriteTrackInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(val track: Track, private val mediaPlayerInteractor: MediaPlayerInteractor, private val favoriteInteractor: FavoriteTrackInteractor,):ViewModel() {

    private var timerJob: Job? = null
    private var isTrackCompleted = false

    private val isFavoriteTrackLiveData = MutableLiveData<Boolean>()
    fun getIsFavoriteTrackState(): LiveData<Boolean> = isFavoriteTrackLiveData

    init {
        preparePlayer(track.previewUrl)
        viewModelScope.launch {
            favoriteInteractor.isTrackFavorite(track).collect {
                isFavoriteTrackLiveData.value = it
            }
        }
    }

    private val mediaPlayerState = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = mediaPlayerState


    fun playbackControl() {
        mediaPlayerInteractor.playbackControl(
            onStarted = {
                trackTimerUpdater()
            },
            onPaused = {
                pausePlayer()
            }
        )
    }
    private fun trackTimerUpdater() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                mediaPlayerState.postValue(AudioPlayerState.Playing(mediaPlayerInteractor.getCurrentPosition()))
                delay(300L)
            }
        }
    }
    private fun preparePlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(
            trackUrl = trackUrl,
            onPrepared = {
                mediaPlayerState.postValue(AudioPlayerState.Prepared)
            },
            onCompletion = {
                isTrackCompleted = true
                timerJob?.cancel()
                mediaPlayerState.postValue(AudioPlayerState.Prepared)
            }
        )
    }
    fun pausePlayer() {
        pauseAndPostState(AudioPlayerState.Paused(mediaPlayerInteractor.getCurrentPosition()))
    }

    fun stopPlayback() {
        pauseAndPostState(
            AudioPlayerState.Stopped
        )
    }

    private fun pauseAndPostState(state: AudioPlayerState) {
        timerJob?.cancel()
        mediaPlayerInteractor.pausePlayer()
        mediaPlayerState.postValue(state)
    }

    fun release() {
        timerJob?.cancel()
        timerJob = null
        mediaPlayerInteractor.releasePlayer()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            val isFavorite = isFavoriteTrackLiveData.value ?: false

            if (!isFavorite) {
                favoriteInteractor.addTrackToFavorites(track)
            } else {
                favoriteInteractor.removeTrackFromFavorites(track)
            }

            isFavoriteTrackLiveData.value = !isFavorite
        }
    }
}
