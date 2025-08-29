package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.player.presentation.PlayerActivity
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.ui.TrackAdapter

class SearchActivity : Fragment() {
    companion object {
        const val TRACK = "TRACK"
    }
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private val trackList = ArrayList<Track>()
    private var savedQuery = ""
    private val viewModel by viewModel< TrackSearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = TrackAdapter(ArrayList()) {
            viewModel.clickDebounce(it)
        }

        binding.tracks.adapter = searchAdapter

        onTrackClickEvents()

        viewModel.getScreenState().observe(viewLifecycleOwner) {
            renderScreenState(it)
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.clear.setOnClickListener {
            binding.searchEditText.setText("")
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            viewModel.setHistoryTrackList()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clear.windowToken, 0)
        }

        binding.refresh.setOnClickListener {
            viewModel.makeSearch(binding.searchEditText.text.toString())
        }

        binding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.makeSearch(view.text.toString())
                true
            }
            false
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty()) {
                viewModel.setHistoryTrackList()
            } else {
                historyVisibilityView(ViewVisibility.GONE)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clear.visibility = clearButtonVisibility(s)

                if (binding.searchEditText.hasFocus() && s?.isEmpty() == true) {
                    binding.progressBar.isVisible = false
                    viewModel.searchJobCancel()
                    viewModel.currentRequestId++
                    viewModel.setHistoryTrackList()
                } else {
                    historyVisibilityView(ViewVisibility.GONE)
                    binding.tracks.isVisible = false
                    binding.layoutPlaceholderError.isVisible = false
                    viewModel.searchDebounce(
                        query = s.toString()
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.searchEditText.addTextChangedListener(textWatcher)
        savedInstanceState?.let { bundle ->
            savedQuery = bundle.getString(TRACK, "")
            binding.searchEditText.setText(savedQuery)
        }
    }
    private fun onTrackClickEvents() {
        viewModel.getClickDebounce().observe(viewLifecycleOwner) { track ->
            if (binding.searchEditText.text.toString().isEmpty()) {
                viewModel.setHistoryTrackList()
            }
            val bundle = Bundle().apply {
                putParcelable(PlayerActivity.TRACK, track)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                bundle
            )
        }
    }

    private fun renderScreenState(state: SearchScreenState) {


        val errorMessage =
            getString(R.string.connectivity_issue)

        binding.progressBar.isVisible = state is SearchScreenState.Loading

        when (state) {
            is SearchScreenState.Loading -> {
                binding.tracks.isVisible = false
            }

            is SearchScreenState.Success -> {
                binding.layoutPlaceholderError.isVisible = false
                searchAdapter.updateData(ArrayList(state.tracks))
                binding.tracks.isVisible = true
            }

            is SearchScreenState.NothingFound -> {
                binding.layoutPlaceholderError.isVisible = true
                binding.tracks.isVisible = false
                binding.refresh.isVisible = false
                binding.ivError.setImageResource(R.drawable.sad_face)
                binding.tvError.text = getString(R.string.not_found)
            }

            is SearchScreenState.NoConnection -> {
                binding.layoutPlaceholderError.isVisible = true
                binding.refresh.isVisible = true
                binding.tracks.isVisible = false
                binding.ivError.setImageResource(R.drawable.no_internet)
                binding.tvError.text = errorMessage
            }

            is SearchScreenState.EmptyHistory -> {
                historyVisibilityView(ViewVisibility.GONE)
                binding.tracks.isVisible = false
                binding.layoutPlaceholderError.isVisible = false
            }
            is SearchScreenState.HistoryContent -> {
                searchAdapter.updateData(ArrayList(state.history))
                historyVisibilityView(ViewVisibility.VISIBLE)
                binding.tracks.isVisible = true
                binding.layoutPlaceholderError.isVisible = false
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun historyVisibilityView(state: ViewVisibility) {
        when (state) {
            ViewVisibility.VISIBLE -> {
                binding.clearHistoryButton.isVisible = true
                binding.historyTitle.isVisible = true
            }

            ViewVisibility.GONE -> {
                binding.clearHistoryButton.isVisible = false
                binding.historyTitle.isVisible = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TRACK, binding.searchEditText.text.toString())
}

enum class ViewVisibility {
    VISIBLE,
    GONE
}
}