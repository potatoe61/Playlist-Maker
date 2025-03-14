package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerPresenter
import com.example.playlistmaker.presentation.player.PlayerView
import com.example.playlistmaker.ui.search.SearchActivity.Companion.TRACK
import com.example.playlistmaker.utils.Creator

class PlayerActivity : AppCompatActivity(), PlayerView {
    private lateinit var presenter: PlayerPresenter

    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artwork: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var playBtn: ImageView
    private lateinit var playTimeText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        }
        playTimeText = findViewById(R.id.play_time)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        artwork = findViewById(R.id.artwork)
        collectionName = findViewById(R.id.collectionName)
        collectionTitle = findViewById(R.id.collectionNameTitle)
        releaseDate = findViewById(R.id.releaseDate)
        primaryGenreName = findViewById(R.id.primaryGenre)
        country = findViewById(R.id.country)

        playBtn = findViewById(R.id.play_butt)


        val track = Gson().fromJson(
            intent.getStringExtra(TRACK),
            Track::class.java
        )

        presenter = Creator.providePlayerPresenter(
            track = track,
            mediaPlayer = MediaPlayer()
        )
        presenter.attachView(this)
        presenter.updateTimeAndButton()

        playBtn.setOnClickListener {
            presenter.playbackControl()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun setTrackName(name: String) {
        trackName.text = name
    }

    override fun setArtistName(name: String) {
        artistName.text = name
    }

    override fun setTrackTime(time: String) {
        trackTime.text = time
    }

    override fun setArtwork(url: String) {
        Glide.with(artwork)
            .load(url.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_track)
            .into(artwork)
    }

    override fun setCollection(name: String) {
        collectionName.text = name
    }

    override fun setCollectionVisibility(visible: Boolean) {
        collectionName.isVisible = visible
        collectionTitle.isVisible = visible
    }

    override fun setReleaseDate(date: String) {
        releaseDate.text = date
    }

    override fun setPrimaryGenre(name: String) {
        primaryGenreName.text = name
    }

    override fun setCountry(name: String) {
        country.text = name
    }

    override fun setPlayButtonEnabled(isEnabled: Boolean) {
        playBtn.isEnabled = isEnabled
    }

    override fun setPlayButtonImage(resId: Int) {
        playBtn.setImageResource(resId)
    }

    override fun setPlayTimeText(time: String) {
        playTimeText.text = time
    }
}