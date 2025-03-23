package com.example.playlistmaker.ui.search


import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_length)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.album_cover)


    fun bind(item: Track, onItemClickListener : OnItemClickListener?) {
        trackNameTextView.text = item.trackName
        artistNameTextView.text = item.artistName
        trackTimeTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounding)))
            .into(artworkImageView)

        itemView.setOnClickListener{
            onItemClickListener?.onItemClick(item)
        }

    }
    fun interface OnItemClickListener {
        fun onItemClick(item: Track)
    }
}