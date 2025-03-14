package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.DELAY_MILLIS
import com.example.playlistmaker.domain.REFRESH_PLAY_TIME_MILLIS
import com.example.playlistmaker.domain.START_PLAY_TIME_MILLIS
import com.example.playlistmaker.domain.STATE_DEFAULT
import com.example.playlistmaker.domain.STATE_PAUSED
import com.example.playlistmaker.domain.STATE_PLAYING
import com.example.playlistmaker.domain.STATE_PREPARED
import com.example.playlistmaker.domain.api.PlayerInterface
import com.example.playlistmaker.utils.DateUtils.millisToStrFormat
import com.example.playlistmaker.utils.DateUtils.strDateFormat

class PlayerPresenter(private val player: PlayerInterface, val track: Track) {
    private var view: PlayerView? = null
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    fun attachView(view: PlayerView) {
        this.view = view
        view.setTrackName(track.trackName)
        view.setArtistName(track.artistName)
        view.setTrackTime(millisToStrFormat(track.trackTimeMillis))
        view.setArtwork(getArtworkUrl())
        view.setCollection(track.collectionName)
        view.setCollectionVisibility(isCollectionVisible())
        view.setReleaseDate(getReleaseDate())
        view.setPrimaryGenre(track.primaryGenreName)
        view.setCountry(track.country)
        view.setPlayButtonEnabled(false)
    }
    fun detachView() {
        this.view = null
        mainThreadHandler.removeCallbacksAndMessages(null)
        player.releasePlayer()
    }
    fun playbackControl() {
        when (player.getPlayerState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun startPlayer() {
        player.startPlayer()
        view?.setPlayButtonImage(R.drawable.pause)
        updateTimeAndButton()
    }
    fun pausePlayer() {
        player.pausePlayer()
        view?.setPlayButtonImage(R.drawable.play)
        mainThreadHandler.removeCallbacksAndMessages(null)
    }
    fun updateTimeAndButton() {
        mainThreadHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    conditionPlayButton()
                    val currentTime = player.getCurrentTime()
                    if (currentTime < REFRESH_PLAY_TIME_MILLIS) {
                        view?.setPlayTimeText(millisToStrFormat(currentTime))
                    } else {
                        view?.setPlayTimeText(millisToStrFormat(START_PLAY_TIME_MILLIS))
                        view?.setPlayButtonImage(R.drawable.play)
                    }
                    mainThreadHandler.postDelayed(
                        this,
                        DELAY_MILLIS,
                    )
                }
            },
            DELAY_MILLIS
        )
    }

    private fun isCollectionVisible(): Boolean = track.collectionName.isNotEmpty()

    private fun getArtworkUrl(): String = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun getReleaseDate(): String = strDateFormat(track.releaseDate)

    private fun conditionPlayButton() {
        view?.setPlayButtonEnabled(
            player.getPlayerState() != STATE_DEFAULT
        )

    }

}