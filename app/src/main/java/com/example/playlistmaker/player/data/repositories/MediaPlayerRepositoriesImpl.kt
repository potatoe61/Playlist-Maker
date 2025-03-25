package com.example.playlistmaker.player.data.repositories

import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.presentation.state.PlayerState2


class MediaPlayerRepositoriesImpl(): MediaPlayerRepositories {

    private val mediaPlayer = Creator.provideMediaPlayer()
    private var playerState = PlayerState2.STATE_DEFAULT

    override fun prepare(trackUrl: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState2.STATE_PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState2.STATE_PREPARED
            onCompletion()
        }
    }

    override fun startPlayer(onStarted: () -> Unit) {
        mediaPlayer.start()
        playerState = PlayerState2.STATE_PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        mediaPlayer.pause()
        playerState = PlayerState2.STATE_PAUSED
        onPaused()
    }

    override fun resetPlayer() {
        mediaPlayer.reset()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        when (playerState) {
            PlayerState2.STATE_PLAYING -> {
                pausePlayer(onPaused)
            }

            PlayerState2.STATE_PREPARED, PlayerState2.STATE_PAUSED -> {
                startPlayer(onStarted)
            }

            else -> {}
        }
    }
}