package com.example.playlistmaker.playlist.presentation


import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.media.getLocalizedContext
import com.example.playlistmaker.playlist.model.Playlist
import java.util.Locale

class PlaylistViewHolder(private val binding: PlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {

        val contextRu = getLocalizedContext(binding.root.context, Locale("ru"))

        binding.tvTitle.text = item.name
        binding.tvTracksCount.text = contextRu.resources.getQuantityString(
            R.plurals.track_count,
            item.tracksCount,
            item.tracksCount
        )

        if (item.imageUri.isNullOrBlank()) {
            binding.ivCover.scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            binding.ivCover.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        Glide.with(itemView)
            .load(item.imageUri)
            .placeholder(R.drawable.placeholder_104px)
            .error(R.drawable.placeholder_104px)
            .into(binding.ivCover)
    }
}