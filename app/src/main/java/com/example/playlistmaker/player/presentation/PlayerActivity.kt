package com.example.playlistmaker.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.media.dpToPx
import com.example.playlistmaker.player.presentation.state.AddingTrackState
import com.example.playlistmaker.player.presentation.state.AudioPlayerState
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.playlist.model.PlaylistState
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlayerActivity : Fragment() {

    companion object {
        const val TRACK = "TRACK"
        const val RADIUS_IMAGE = 8.0f
    }

    private lateinit var binding: FragmentAudioPlayerBinding
    private lateinit var playlistsAdapter: PlaylistsBottomSheetAdapter

    private val track by lazy {
        requireArguments().getParcelable<Track>(TRACK) ?: error("Трек не найден")
    }

    private var songUrl: String = ""

    private val viewModelPlayer by viewModel<MediaPlayerViewModel> {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTrackData(track)

        playlistsAdapter = PlaylistsBottomSheetAdapter {
            viewModelPlayer.addTrackToPlaylist(track, it)
        }
        binding.rvPlaylists.adapter = playlistsAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ibAddToAlbum.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.ibPlayButton.setOnClickListener {
            viewModelPlayer.playbackControl()
        }

        binding.ibLikeTrack.setOnClickListener {
            viewModelPlayer.onFavoriteClicked()
        }

        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)
        }

        viewModelPlayer.getPlayerState().observe(viewLifecycleOwner) {
            renderPlayer(it)
        }

        viewModelPlayer.getIsFavoriteTrackState().observe(viewLifecycleOwner) {
            renderLikeButton(it)
        }

        viewModelPlayer.getPlaylistState().observe(viewLifecycleOwner) {
            renderPlaylistsBottomSheet(it)
        }

        viewModelPlayer.getIsTrackAddedState().observe(viewLifecycleOwner) {
            renderAddedTrackToPlaylist(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelPlayer.getPlaylist()
    }

    override fun onPause() {
        super.onPause()
        if (!requireActivity().isChangingConfigurations) {
            viewModelPlayer.stopPlayback()
        }
    }

    private fun bindTrackData(track: Track) {
        binding.tvCountryName.text = track.country
        binding.tvGenreName.text = track.primaryGenreName
        binding.tvYearValue.text = track.releaseDate?.take(4) ?: ""
        binding.tvAlbumName.text = track.collectionName ?: run {
            binding.tvAlbumName.isVisible = false
            binding.tvAlbum.isVisible = false
            return@run ""
        }
        binding.tvTrackDuration.text = track.formattedTrackTime
        binding.tvTrackTime.text = getString(R.string.default_play_time)
        binding.tvArtistName.text = track.artistName
        binding.tvHeadTrackName.text = track.trackName
        songUrl = track.previewUrl

        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(this)
                .load(track.getCoverArtwork)
                .placeholder(R.drawable.olaceholder_312px)
                .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, requireContext())))
                .into(binding.ivCover)
        } else {
            binding.ivCover.setImageResource(R.drawable.sad_face)
        }
    }

    private fun renderPlayer(state: AudioPlayerState) {
        when (state) {

            is AudioPlayerState.Prepared -> {
                binding.ibPlayButton.isEnabled = true
                binding.tvTrackTime.text = getString(R.string.default_play_time)
                binding.ibPlayButton.setImageResource(R.drawable.play)
            }

            is AudioPlayerState.Playing -> {
                binding.tvTrackTime.text = state.currentPosition
                binding.ibPlayButton.setImageResource(R.drawable.pause)
            }

            is AudioPlayerState.Paused -> {
                binding.ibPlayButton.setImageResource(R.drawable.play)
                binding.tvTrackTime.text = state.lastPosition
            }

            is AudioPlayerState.Stopped -> {
                binding.ibPlayButton.setImageResource(R.drawable.play)
            }
        }
    }

    private fun renderLikeButton(active: Boolean) {
        if (active) {
            binding.ibLikeTrack.setImageResource(R.drawable.active_like_button)
        } else {
            binding.ibLikeTrack.setImageResource(R.drawable.liked)
        }
    }

    private fun renderPlaylistsBottomSheet(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> {
                playlistsAdapter.submitList(state.playlist)
            }

            is PlaylistState.Empty -> {
                playlistsAdapter.submitList(emptyList())
            }
        }
    }

    private fun renderAddedTrackToPlaylist(state: AddingTrackState) {
        when (state) {
            is AddingTrackState.TrackAdded -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.added_to_playlist, state.name),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AddingTrackState.TrackAlreadyAdded -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.already_added_to_playlist, state.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}