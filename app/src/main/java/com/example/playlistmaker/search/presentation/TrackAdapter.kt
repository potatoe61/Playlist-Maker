package com.practicum.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackViewHolder


class TrackAdapter(
    private var tracks: ArrayList<Track>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            val track = tracks[position]
            clickListener.onItemClick(track)
        }
    }

    fun updateData(track: ArrayList<Track>) {
        this.tracks = track
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onItemClick(track: Track)
    }
}