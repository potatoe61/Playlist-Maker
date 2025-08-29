package com.example.playlistmaker.playlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.media.presentation.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.TabMediaFragmentBinding
import com.example.playlistmaker.playlist.model.PlaylistState

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: TabMediaFragmentBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TabMediaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNewPlaylist.setOnClickListener {
            clickEventToOpenCreatePlaylist()
        }

        adapter = PlaylistAdapter()

        binding.playlistView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistView.adapter = adapter

        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            playlistsRender(it)
        }

        viewModel.getPlaylists()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun clickEventToOpenCreatePlaylist() {
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
    }

    private fun playlistsRender(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> {
                binding.placeholderContainer.isVisible = false
                binding.playlistView.isVisible = true
                adapter.submitList(state.playlist) {
                    binding.playlistView.scrollToPosition(0)
                }
            }

            is PlaylistState.Empty -> {
                binding.placeholderContainer.isVisible = true
                binding.playlistView.isVisible = false
            }
        }
    }
}