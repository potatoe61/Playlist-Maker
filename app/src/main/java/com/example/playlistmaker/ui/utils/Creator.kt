package com.example.playlistmaker.utils

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.PlayerInterface
import com.example.playlistmaker.data.impl.PlayerImplementation
import com.example.playlistmaker.presentation.player.PlayerPresenter

object Creator {

    private fun providePlayerInteractor(
        track: Track,
        mediaPlayer: MediaPlayer,
    ): PlayerInterface {
        return PlayerImplementation(track, mediaPlayer)
    }

    fun providePlayerPresenter(track: Track, mediaPlayer: MediaPlayer): PlayerPresenter {
        return PlayerPresenter(providePlayerInteractor(track, mediaPlayer), track)
    }
}