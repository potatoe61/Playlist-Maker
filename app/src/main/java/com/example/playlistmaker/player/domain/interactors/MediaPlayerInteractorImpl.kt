package com.example.playlistmaker.player.domain.interactors

import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerInteractorImpl(private val repository:MediaPlayerRepositories):MediaPlayerInteractor {
    override fun prepare(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        return repository.prepare(trackUrl, onPrepared, onCompletion)
    }
    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    override fun startPlayer(onStarted: () -> Unit) {
        return repository.startPlayer(onStarted)
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        return repository.pausePlayer(onPaused)
    }

    override fun resetPlayer() {
        return repository.resetPlayer()
    }

    override fun releasePlayer() {
        return repository.releasePlayer()
    }

    override fun isPlaying(): Boolean {
        return  repository.isPlaying()
    }

    override fun getCurrentPosition(): String {
        return dateFormat.format(repository.getCurrentPosition())
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        repository.playbackControl(onStarted, onPaused)
    }
}