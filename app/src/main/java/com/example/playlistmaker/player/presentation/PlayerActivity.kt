package com.example.playlistmaker.player.presentation


import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import com.example.playlistmaker.player.presentation.state.PlayerState2
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    companion object {
        const val TRACK = "TRACK"
    }
    private lateinit var playOrPauseButton: ImageView
    private var mainThreadHandler: Handler? = null
    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }
    private var songUrl: String = ""
    private val viewModel by viewModel<MediaPlayerViewModel> {
        parametersOf(songUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)
        val backButton = findViewById<Toolbar>(R.id.toolbar_id)
        val intent = getIntent()
        val track: String? = intent.getStringExtra(TRACK)
        val gson = Gson()
        val historyTrackClick = gson.fromJson(track, Track::class.java)
        val tvTrackName = findViewById<TextView>(R.id.trackName)
        val tvArtistName = findViewById<TextView>(R.id.artistName)
        val tvDuration = findViewById<TextView>(R.id.trackTime)
        val tvCountry = findViewById<TextView>(R.id.country)
        val tvYears = findViewById<TextView>(R.id.releaseDate)
        val tvAlbum = findViewById<TextView>(R.id.collectionName)
        val tvGenre = findViewById<TextView>(R.id.primaryGenre)
        val ivTitle = findViewById<ImageView>(R.id.artwork)
        songUrl = historyTrackClick.previewUrl
        tvTrackName.text = historyTrackClick.trackName
        tvArtistName.text = historyTrackClick.artistName
        tvCountry.text = historyTrackClick.country
        tvGenre.text = historyTrackClick.primaryGenreName
        tvAlbum.text = historyTrackClick.collectionName
        tvYears.text = historyTrackClick.releaseDate.substring(0, 4)
        tvDuration?.text = dateFormat.format(historyTrackClick.trackTimeMillis)

        mainThreadHandler = Handler(Looper.getMainLooper())

        playOrPauseButton = findViewById<ImageView>(R.id.play_butt)

        viewModel.getPlayerState().observe(this) {
            render(it)
        }
        playOrPauseButton.setOnClickListener {
            viewModel.playbackControl()
        }

        Glide.with(this)
            .load(historyTrackClick.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.art_work_radius)))
            .into(ivTitle)

        backButton.setNavigationOnClickListener {
            finish()
        }
    }

    private fun render(state: AudioPlayerState) {
        val tvTime = findViewById<TextView>(R.id.play_time)
        when (state) {
            is AudioPlayerState.Prepared -> {
                playOrPauseButton.isEnabled = true
                tvTime.text = getString(R.string.default_play_time)
                playOrPauseButton.setImageResource(R.drawable.play)
            }

            is AudioPlayerState.Playing -> {
                tvTime.text = state.currentPosition
                playOrPauseButton.setImageResource(R.drawable.pause)
            }

            is AudioPlayerState.Paused -> {
                playOrPauseButton.setImageResource(R.drawable.play)
                tvTime.text = state.lastPosition
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            viewModel.release()
        }
    }
}