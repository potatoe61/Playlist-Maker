package com.example.playlistmaker.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

class TracksAdapter(
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<TrackViewHolder>() {
    var track : ArrayList<Track> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)

    }

    override fun onBindViewHolder(viewer: TrackViewHolder, position: Int) {
        viewer.bind(track[position])

        viewer.itemView.setOnClickListener{ clickListener.click(track[position])}
    }

    override fun getItemCount(): Int = track.size
    fun interface ClickListener {
        fun click(track: Track)
    }

}