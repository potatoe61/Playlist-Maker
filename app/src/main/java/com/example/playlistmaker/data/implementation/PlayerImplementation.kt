package com.example.playlistmaker.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.STATE_DEFAULT
import com.example.playlistmaker.domain.STATE_PAUSED
import com.example.playlistmaker.domain.STATE_PLAYING
import com.example.playlistmaker.domain.STATE_PREPARED
import com.example.playlistmaker.domain.api.PlayerInterface

class PlayerImplementation(track: Track, private val mediaPlayer: MediaPlayer) :
    PlayerInterface {

    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

}