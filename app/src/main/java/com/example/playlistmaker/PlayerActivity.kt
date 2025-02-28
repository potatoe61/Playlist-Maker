package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.example.playlistmaker.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK),Track::class.java)

        val trackName = findViewById<TextView>(R.id.trackName)
        trackName.text = track.trackName

        val artistName = findViewById<TextView>(R.id.artistName)
        artistName.text = track.artistName

        val trackTime = findViewById<TextView>(R.id.trackTime)
        trackTime.text = Time.millisToStrFormat(track.trackTimeMillis)

        val artwork = findViewById<ImageView>(R.id.artwork)
        Glide.with(artwork)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_track)
            .transform(RoundedCorners(artwork.resources.getDimensionPixelSize(R.dimen.art_work_radius)))
            .into(artwork)

        val collectionName = findViewById<TextView>(R.id.collectionName)
        val collectionTitle = findViewById<TextView>(R.id.collectionNameTitle)
        if(track.collectionName.isNullOrEmpty()){
            collectionName.visibility = View.GONE
            collectionTitle.visibility = View.GONE
        } else {
            collectionName.text = track.collectionName
        }

        val releaseDate = findViewById<TextView>(R.id.releaseDate)
        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        val data = SimpleDateFormat("yyyy", Locale.getDefault()).format(formatDate)
        releaseDate.text =  data

        val primaryGenreName = findViewById<TextView>(R.id.primaryGenre)
        primaryGenreName.text = track.primaryGenreName

        val country = findViewById<TextView>(R.id.country)
        country.text = track.country
    }
}
