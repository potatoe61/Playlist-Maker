package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.content.Intent
import android.media.MediaPlayer
import com.google.gson.Gson
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

import com.example.playlistmaker.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.domain.api.Consumer
import com.example.playlistmaker.domain.api.ConsumerData
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.search.TrackViewHolder

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
        const val TRACK = "TRACK"
        private const val CLICK_ITEM_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val KEY = "key"
    }
    private lateinit var searchEditText: EditText
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var refreshButt: Button
    private lateinit var errorText: TextView
    private lateinit var errorIc: ImageView
    private val track = ArrayList<Track>()
    private var savedValue: String? = null
    private var trackSearch = listOf<Track>()
    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }
    private lateinit var clearHistoryButton: Button
    lateinit var historyLayout: ViewGroup
    lateinit var history: RecyclerView
    lateinit var adapterHistory: TrackAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var titleHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        history = findViewById<RecyclerView>(R.id.list)
        clearHistoryButton = findViewById<Button>(R.id.clear_history_button)
        errorIc = findViewById(R.id.iv_Error)
        errorText = findViewById(R.id.tv_Error)
        refreshButt = findViewById(R.id.refresh)
        recyclerViewTrack = findViewById<RecyclerView>(R.id.tracks)
        historyLayout = findViewById(R.id.HistoryLayout)
        searchEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        titleHistory = findViewById(R.id.history_title)
        val backButton = findViewById<Button>(R.id.back)


        adapterHistory = TrackAdapter()
        val searchHistoryInteractor = Creator.provideHistoryInteractor()
        trackSearch = searchHistoryInteractor.getTrack()

        adapterHistory.updateItems(trackSearch)

        adapter.notifyDataSetChanged()
        history.adapter = adapterHistory
        val clearButton = findViewById<ImageView>(R.id.clear)


        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (searchEditText.hasFocus() && searchEditText.text.isEmpty() && trackSearch.isNotEmpty()) {
                showHistory()
            } else showMessage(StatusResponse.SUCCESS)
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            track.clear()
            adapter.updateItems(track)
            adapter.notifyDataSetChanged()
            refreshButt.visibility = View.GONE
            errorText.visibility = View.GONE
            errorIc.visibility = View.GONE
            if (trackSearch.isEmpty()) {
                historyLayout.visibility = View.GONE
            } else historyLayout.visibility = View.VISIBLE
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !p0.isNullOrEmpty()

                if (searchEditText.hasFocus() && p0.toString().isEmpty()) {
                    historyLayout.visibility = View.GONE
                    recyclerViewTrack.visibility = View.GONE
                    errorIc.visibility = View.GONE
                    errorText.visibility = View.GONE
                    refreshButt.visibility = View.GONE
                } else {
                    showMessage(StatusResponse.SUCCESS)
                    searchDebounce()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

        refreshButt.setOnClickListener {
            refreshButt.visibility = View.GONE
            errorIc.visibility = View.GONE
            errorText.visibility = View.GONE
            search()
        }

        adapter.updateItems(track)

        recyclerViewTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTrack.adapter = adapter

        history.layoutManager = LinearLayoutManager(this)
        history.adapter = adapterHistory


        adapter.onItemClickListener = TrackViewHolder.OnItemClickListener { track ->
            openMedia(track)


            trackSearch = searchHistoryInteractor.checkHistory(track)

            historyLayout.visibility = View.VISIBLE
            adapterHistory.updateItems(trackSearch)
            history.adapter = adapterHistory
        }

        adapterHistory.onItemClickListener = TrackViewHolder.OnItemClickListener { trackSearch ->
            openMedia(trackSearch)
        }

        clearHistoryButton.setOnClickListener {

            searchHistoryInteractor.clearHistory()

            historyLayout.visibility = View.GONE
            trackSearch = emptyList()
            adapterHistory.updateItems(trackSearch)
            adapter.notifyDataSetChanged()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedValue = searchEditText.text.toString()
        outState.putString(EDIT_TEXT_VIEW_KEY, savedValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedValue = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY, savedValue)
        searchEditText.setText(savedValue)
    }

    private fun search() {
        if (searchEditText.text.isNotEmpty()) {
            errorText.visibility = View.GONE
            recyclerViewTrack.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        val getTrack = Creator.provideTrackInteractor()
        getTrack.searchTrack(searchEditText.text.toString(), object : Consumer<List<Track>> {
            override fun consume(data: ConsumerData<List<Track>>) {
                runOnUiThread {
                    if (data is ConsumerData.Error) {
                        showMessage(StatusResponse.ERROR)
                    } else if (data is ConsumerData.Data) {
                        if (data.value.isNotEmpty() == true) {
                            showMessage(StatusResponse.SUCCESS)
                            track.addAll(data.value)
                            adapter.updateItems(track)
                            adapter.notifyDataSetChanged()
                        }
                        if (data.value.isEmpty() and searchEditText.text.isNotEmpty()) {
                            showMessage(StatusResponse.EMPTY)
                        }
                    }
                }
            }
        })
    }


    private fun showMessage(status: StatusResponse) {
        errorText.isVisible = true
        track.clear()
        adapter.notifyDataSetChanged()
        when (status) {
            StatusResponse.SUCCESS -> {
                errorText.visibility = View.GONE
                progressBar.visibility = View.GONE
                historyLayout.visibility = View.GONE
                errorIc.visibility = View.GONE
                recyclerViewTrack.visibility = View.VISIBLE
            }

            StatusResponse.EMPTY -> {
                errorText.text = getString(R.string.not_found)
                errorIc.setImageResource(R.drawable.sad_face)
                refreshButt.visibility = View.GONE
                progressBar.visibility = View.GONE
                errorIc.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                recyclerViewTrack.visibility = View.GONE
            }

            StatusResponse.ERROR -> {
                errorText.text = getString(R.string.connectivity_issue)
                errorIc.setImageResource(R.drawable.no_internet)
                errorIc.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                refreshButt.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
                recyclerViewTrack.visibility = View.GONE
            }
        }
    }

    enum class StatusResponse {
        SUCCESS, EMPTY, ERROR
    }

    fun showHistory() {
        recyclerViewTrack.visibility = View.GONE
        errorText.visibility = View.GONE
        historyLayout.visibility = View.VISIBLE
    }

    private fun openMedia(track: Track) {
        if (clickDebounce()) {
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(TRACK, Gson().toJson(track))
            startActivity(playerIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_ITEM_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}