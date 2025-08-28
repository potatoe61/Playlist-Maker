package com.example.playlistmaker.media.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TabFavoritesFragmentBinding
import com.example.playlistmaker.media.model.FavoriteTrackState
import com.example.playlistmaker.media.presentation.viewmodel.FavoriteTrackViewModel
import com.example.playlistmaker.player.presentation.PlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = TabFavoriteFragment()
    }

    private lateinit var binding: TabFavoritesFragmentBinding
    private lateinit var adapter:TrackAdapter
    private val viewModel by viewModel<FavoriteTrackViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = TabFavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(ArrayList()) {
            onTrackClickEvents(it)
        }

        binding.rvFavoriteList.adapter = adapter

        viewModel.getFavoriteTracks()

        viewModel.favoriteTrackState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    private fun render(state: FavoriteTrackState) {
        when(state) {
            is FavoriteTrackState.Content -> {
                adapter.updateData(ArrayList(state.tracks))
                binding.rvFavoriteList.isVisible = true
                binding.placeholderContainer.isVisible = false
            }
            FavoriteTrackState.Empty -> {
                binding.rvFavoriteList.isVisible = false
                binding.placeholderContainer.isVisible = true
            }
        }
    }

    private fun onTrackClickEvents(track: Track) {
        val audioPlayerIntent =
            Intent(requireContext(), PlayerActivity::class.java).apply {
                val itemMedia = track
                val gson = Gson()
                val json = gson.toJson(itemMedia)
                putExtra(PlayerActivity.TRACK, json)
            }
        startActivity(audioPlayerIntent)
    }
}