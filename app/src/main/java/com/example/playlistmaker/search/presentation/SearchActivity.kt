package com.example.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import android.widget.LinearLayout
import com.example.playlistmaker.player.presentation.PlayerActivity
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    companion object {
        const val TRACK = "TRACK"
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
    }
    private lateinit var arrowBackButton: Button
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var editText: EditText
    private lateinit var refreshButton: Button

    private lateinit var btnClearHistory: Button
    private lateinit var tvSearchHistory: TextView

    private lateinit var progressBar: ProgressBar
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var rvForSearchTrack: RecyclerView
    private lateinit var layoutPlaceholderError: LinearLayout

    private val trackList = ArrayList<Track>()

    private var savedQuery: String = ""
    private val viewModel by viewModel< TrackSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        rvForSearchTrack = findViewById(R.id.tracks)
        arrowBackButton = findViewById(R.id.back)
        queryInput = findViewById(R.id.searchEditText)
        editText = findViewById(R.id.searchEditText)
        clearButton = findViewById(R.id.clear)
        refreshButton = findViewById(R.id.refresh)
        progressBar = findViewById(R.id.progressBar)

        tvSearchHistory = findViewById(R.id.history_title)
        btnClearHistory = findViewById(R.id.clear_history_button)
        layoutPlaceholderError = findViewById(R.id.layoutPlaceholderError)

        searchAdapter = TrackAdapter(ArrayList()) {
            viewModel.clickDebounce(it)
        }

        rvForSearchTrack.adapter = searchAdapter

        onTrackClickEvents()

        viewModel.getScreenState().observe(this) {
            renderScreenState(it)
        }

        arrowBackButton.setOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            viewModel.setHistoryTrackList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            viewModel.makeSearch(queryInput.text.toString())
        }

        queryInput.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.makeSearch(view.text.toString())
                true
            }
            false
        }

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                viewModel.setHistoryTrackList()
            } else {
                historyVisibilityView(ViewVisibility.GONE)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (queryInput.hasFocus() && s?.isEmpty() == true) {
                    progressBar.isVisible = false
                    viewModel.removeCallback()
                    viewModel.currentRequestId++
                    viewModel.setHistoryTrackList()
                } else {
                    historyVisibilityView(ViewVisibility.GONE)
                    rvForSearchTrack.isVisible = false
                    layoutPlaceholderError.isVisible = false
                    viewModel.searchDebounce(
                        query = s.toString()
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        queryInput.addTextChangedListener(textWatcher)
    }
    private fun onTrackClickEvents() {
        viewModel.getClickDebounce().observe(this) { track ->
            if (queryInput.text.toString().isEmpty()) {
                viewModel.setHistoryTrackList()
            }
            val audioPlayerIntent = Intent(this, PlayerActivity::class.java).apply {
                val itemMedia = track
                val gson = Gson()
                val json = gson.toJson(itemMedia)
                putExtra(TRACK, json)
            }
            startActivity(audioPlayerIntent)
        }
    }

    private fun renderScreenState(state: SearchScreenState) {
        val placeholderImage = findViewById<ImageView>(R.id.iv_Error)
        val placeholderMessage = findViewById<TextView>(R.id.tv_Error)

        val errorMessage =
            getString(R.string.connectivity_issue)

        progressBar.isVisible = state is SearchScreenState.Loading

        when (state) {
            is SearchScreenState.Loading -> {
                rvForSearchTrack.isVisible = false
            }

            is SearchScreenState.Success -> {
                layoutPlaceholderError.isVisible = false
                searchAdapter.updateData(ArrayList(state.tracks))
                rvForSearchTrack.isVisible = true
            }

            is SearchScreenState.NothingFound -> {
                layoutPlaceholderError.isVisible = true
                rvForSearchTrack.isVisible = false
                refreshButton.isVisible = false
                placeholderImage.setImageResource(R.drawable.sad_face)
                placeholderMessage.text = getString(R.string.not_found)
            }

            is SearchScreenState.NoConnection -> {
                layoutPlaceholderError.isVisible = true
                refreshButton.isVisible = true
                rvForSearchTrack.isVisible = false
                placeholderImage.setImageResource(R.drawable.no_internet)
                placeholderMessage.text = errorMessage
            }

            is SearchScreenState.EmptyHistory -> {
                historyVisibilityView(ViewVisibility.GONE)
                rvForSearchTrack.isVisible = false
                layoutPlaceholderError.isVisible = false
            }
            is SearchScreenState.HistoryContent -> {
                searchAdapter.updateData(ArrayList(state.history))
                historyVisibilityView(ViewVisibility.VISIBLE)
                rvForSearchTrack.isVisible = true
                layoutPlaceholderError.isVisible = false
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
                btnClearHistory.isVisible = true
                tvSearchHistory.isVisible = true
            }

            ViewVisibility.GONE -> {
                btnClearHistory.isVisible = false
                tvSearchHistory.isVisible = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VIEW_KEY, editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedQuery = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY, "")
        editText.setText(savedQuery)
    }
}

enum class ViewVisibility {
    VISIBLE,
    GONE
}