package com.example.playlistmaker
import android.util.Log;
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlin.collections.ArrayList


class TrackAdapter(private val trackList: ArrayList<Track>) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_tracks, parent, false)
        return TrackViewHolder(parent)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
    }
    override fun getItemCount(): Int {
        return trackList.size
    }
}


//class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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