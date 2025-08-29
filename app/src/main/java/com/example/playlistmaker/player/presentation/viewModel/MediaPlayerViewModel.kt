package com.example.playlistmaker.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoriteTrackInteractor
import com.example.playlistmaker.player.presentation.state.AddingTrackState
import com.example.playlistmaker.playlist.PlaylistInteractor
import com.example.playlistmaker.playlist.model.Playlist
import com.example.playlistmaker.playlist.model.PlaylistState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaPlayerViewModel(val track: Track, private val mediaPlayerInteractor: MediaPlayerInteractor, private val favoriteInteractor: FavoriteTrackInteractor,private val playlistInteractor: PlaylistInteractor):ViewModel() {

    private var timerJob: Job? = null

    private val isFavoriteTrackLiveData = MutableLiveData<Boolean>()
    fun getIsFavoriteTrackState(): LiveData<Boolean> = isFavoriteTrackLiveData
    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    private val isAddedTrackStateLiveData = MutableLiveData<AddingTrackState>()
    fun getIsTrackAddedState(): LiveData<AddingTrackState> = isAddedTrackStateLiveData

    init {
        preparePlayer(track.previewUrl)
        viewModelScope.launch {
            favoriteInteractor.isTrackFavorite(track).collect {
                isFavoriteTrackLiveData.value = it
            }
        }
        getPlaylist()
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
    fun getPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) playlistStateLiveData.postValue(PlaylistState.Empty)
                else playlistStateLiveData.postValue(PlaylistState.Content(it))
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (track.trackId.toString() in playlist.tracksId) isAddedTrackStateLiveData.postValue(
            AddingTrackState.TrackAlreadyAdded(name = playlist.name)
        ) else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
                getPlaylist()
                isAddedTrackStateLiveData.postValue(
                    AddingTrackState.TrackAdded(name = playlist.name)
                )
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        release()
    }
}
