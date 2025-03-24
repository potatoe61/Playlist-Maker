package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.activity_tracks, parent, false)
    ) {

    private val ivCoverTrack = itemView.findViewById<ImageView>(R.id.album_cover)
    private val tvTrackName = itemView.findViewById<TextView>(R.id.track_name)
    private val tvArtistName = itemView.findViewById<TextView>(R.id.artist_name)
    private val tvTrackTime = itemView.findViewById<TextView>(R.id.track_length)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounding)))
            .into(ivCoverTrack)

        tvTrackName.text = model.trackName
        tvArtistName.text = model.artistName
        tvTrackTime.text = model.formattedTrackTime
        tvArtistName.requestLayout()
        tvTrackTime.requestLayout()
    }
}