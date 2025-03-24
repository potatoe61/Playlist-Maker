package com.example.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    companion object {
        const val TRACK = "TRACK"
    }

    private val mediaPlayer = MediaPlayer()
    private lateinit var playOrPauseButton: ImageView
    private var mainThreadHandler: Handler? = null
    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
    private val viewModel by viewModels<MediaPlayerViewModel> { MediaPlayerViewModel.getViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<Toolbar>(R.id.toolbar_id)
        val intent = getIntent()
        val track: String? = intent.getStringExtra(TRACK)
        val gson = Gson()
        val historyTrackClick = gson.fromJson(track, Track::class.java)
        val tvTrackName = findViewById<TextView>(R.id.trackName)
        val tvAlbumName = findViewById<TextView>(R.id.collectionNameTitle)
        val tvDuration = findViewById<TextView>(R.id.trackTime)
        val tvCountry = findViewById<TextView>(R.id.country)
        val tvYears = findViewById<TextView>(R.id.releaseDate)
        val tvAlbum = findViewById<TextView>(R.id.collectionName)
        val tvGenre = findViewById<TextView>(R.id.primaryGenre)
        val ivTitle = findViewById<ImageView>(R.id.artwork)
        val tvTime = findViewById<TextView>(R.id.play_time)
        tvTrackName.text = historyTrackClick.trackName
        tvAlbumName.text = historyTrackClick.artistName
        tvCountry.text = historyTrackClick.country
        tvGenre.text = historyTrackClick.primaryGenreName
        tvAlbum.text = historyTrackClick.collectionName
        tvYears.text = viewModel.formatReleaseDate(historyTrackClick.releaseDate)
        tvDuration?.text = dateFormat.format(historyTrackClick.trackTimeMillis)
        val url = historyTrackClick.previewUrl

        mainThreadHandler = Handler(Looper.getMainLooper())

        playOrPauseButton = findViewById<ImageView>(R.id.play_butt)

        viewModel.preparePlayer(url)

        playOrPauseButton.setOnClickListener {
            playbackControl()
        }

        viewModel.state.observe(this, { state ->
            when (state) {
                MediaPlayerViewModel.STATE_PLAYING -> playOrPauseButton.setImageResource(R.drawable.pause)
                MediaPlayerViewModel.STATE_PAUSED, MediaPlayerViewModel.STATE_PREPARED -> playOrPauseButton.setImageResource(
                    R.drawable.play
                )

                MediaPlayerViewModel.STATE_COMPLETE -> {
                    playOrPauseButton.setImageResource(R.drawable.play)
                }
            }
        })
        Glide.with(this)
            .load(viewModel.formatArtworkUrl(historyTrackClick.artworkUrl100))
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.art_work_radius)))
            .into(ivTitle)

        backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.info.observe(this, Observer { info ->
            tvTime?.text = info.currentPosition
        })
    }

    private fun playbackControl() {
        when (viewModel.state.value) {
            MediaPlayerViewModel.STATE_PLAYING -> viewModel.pausePlayer()
            MediaPlayerViewModel.STATE_PREPARED, MediaPlayerViewModel.STATE_PAUSED -> viewModel.startPlayer()
            MediaPlayerViewModel.STATE_COMPLETE -> viewModel.startPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
}