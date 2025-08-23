package com.example.playlistmaker.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(url: String, private val mediaPlayerInteractor: MediaPlayerInteractor):ViewModel() {

    private var timerJob: Job? = null
    private var isTrackCompleted = false

    init {
        preparePlayer(url)
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
}
