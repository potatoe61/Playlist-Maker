package com.example.playlistmaker.player.presentation

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.PlaylistViewBottomSheetBinding
import com.example.playlistmaker.media.getLocalizedContext
import com.example.playlistmaker.playlist.model.Playlist
import java.util.Locale
import com.example.playlistmaker.R
import com.example.playlistmaker.media.dpToPx

class PlaylistsBottomSheetViewHolder(private val binding: PlaylistViewBottomSheetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist, clickListener: PlaylistsBottomSheetAdapter.PlaylistClickListener) {

        val contextRu = getLocalizedContext(binding.root.context, Locale("ru"))

        binding.tvTitle.text = item.name
        binding.countTracks.text = contextRu.resources.getQuantityString(
            R.plurals.track_count,
            item.tracksCount,
            item.tracksCount
        )

        binding.root.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                clickListener.onPlaylistClick(item)
            }
        }

        Glide.with(itemView)
            .load(item.imageUri)
            .transform(CenterCrop(), RoundedCorners(dpToPx(2.0f, this.itemView.context)))
            .placeholder(R.drawable.placeholder_track)
            .into(binding.ivCover)
    }
}
