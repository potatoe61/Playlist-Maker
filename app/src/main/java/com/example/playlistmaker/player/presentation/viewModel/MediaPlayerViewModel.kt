package com.example.playlistmaker.player.presentation.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import com.example.playlistmaker.player.presentation.state.PlayerState
import com.example.playlistmaker.player.presentation.state.PlayerState2

class MediaPlayerViewModel(url: String, private val mediaPlayerInteractor: MediaPlayerInteractor):ViewModel() {
    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaPlayerViewModel(
                    url,
                    Creator.provideMediaPlayerInteractor()
                )
            }
        }
    }
    init {
        preparePlayer(url)
    }

    val mainThreadHandler = Handler(Looper.getMainLooper())

    private val mediaPlayerState = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = mediaPlayerState
    private val _info = MutableLiveData(PlayerState())
    val info : LiveData<PlayerState> get() = _info

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl(
            onStarted = {
                mediaPlayerState.postValue(AudioPlayerState.State(PlayerState2.STATE_PLAYING))
                mainThreadHandler.post(trackTimerUpdater())
            },
            onPaused = {
                mediaPlayerState.postValue(AudioPlayerState.State(PlayerState2.STATE_PAUSED))
                mainThreadHandler.removeCallbacks(trackTimerUpdater())
            }
        )
    }
    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (mediaPlayerInteractor.isPlaying()) {
                mediaPlayerState.postValue(AudioPlayerState.Playing(mediaPlayerInteractor.getCurrentPosition()))
                mainThreadHandler.postDelayed(this, 300L)
            }
        }
    }
    private fun preparePlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(
            trackUrl = trackUrl,
            onPrepared = {
                mediaPlayerState.postValue(AudioPlayerState.State(PlayerState2.STATE_PREPARED))
            },
            onCompletion = {
                mainThreadHandler.removeCallbacks(trackTimerUpdater())
                mediaPlayerState.postValue(AudioPlayerState.State(PlayerState2.STATE_PREPARED))
            }
        )
    }
    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        mediaPlayerState.postValue(AudioPlayerState.State(PlayerState2.STATE_PAUSED))
    }

    fun release() {
        mainThreadHandler.removeCallbacks(trackTimerUpdater())
        mediaPlayerInteractor.releasePlayer()
    }
    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.releasePlayer()
    }
}
