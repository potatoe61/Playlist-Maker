package com.example.playlistmaker.player.domain.repositories

interface MediaPlayerRepositories {
    fun prepare(trackUrl:String,onPrepared:()->Unit,onCompletion:()->Unit)
    fun startPlayer(onStarted: () -> Unit)
    fun pausePlayer(onPaused: () -> Unit)
    fun resetPlayer()
    fun releasePlayer()
    fun  isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun playbackControl(
        onStarted: () -> Unit = {},
        onPaused: () -> Unit = {}
    )
}