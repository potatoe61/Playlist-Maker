package com.example.playlistmaker.presentation.search
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.utils.Time
import com.example.playlistmaker.domain.models.Track

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parentView.context).inflate(R.layout.activity_tracks, parentView, false)
    ) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_length)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.album_cover)

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        //Log.d("TrackTimeDebug", "Track Time: ${track.trackTime}")
        trackTimeTextView.text = Time.millisToStrFormat(track.trackTimeMillis)
        val density = Resources.getSystem().displayMetrics.density
        val cornerRadius = (2 * density).toInt()
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder_track)
            .transform(RoundedCorners(cornerRadius))
            .error(R.drawable.placeholder_track)
            .into(artworkImageView)
    }
}