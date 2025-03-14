package com.example.playlistmaker.domain.api

interface PlayerInterface {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
}