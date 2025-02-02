package com.example.playlistmaker

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import android.content.Intent
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var refreshButt: Button
    private lateinit var errorText: TextView
    private lateinit var errorIc: ImageView
    private lateinit var error: LinearLayout
    private var savedValue: String? = null
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(Rest::class.java)
    private val trackList = ArrayList<Track>()
    private val trackAdapter = TracksAdapter { clickOnTrack(it) }
    private val historyTrackAdapter = TracksAdapter { clickOnTrack(it) }
    private lateinit var searchHistory: History
    private lateinit var clearHistoryButton: Button
    private lateinit var titleHistory: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchHistory = History(getSharedPreferences("HISTORY", MODE_PRIVATE))
        setContentView(R.layout.activity_search)
        recyclerViewTrack = findViewById(R.id.list)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = trackAdapter
        savedValue = savedInstanceState?.getString(EDIT_TEXT_VIEW_KEY)
        val backImage = findViewById<Button>(R.id.back)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        refreshButt = findViewById(R.id.refresh)
        refreshButt.setOnClickListener {
            searchTrackList()
        }
        clearHistoryButton = findViewById(R.id.clear_history_button)
        clearHistoryButton.setOnClickListener {
            searchHistory.clearList()
            showState(StateType.SEARCH_RESULT)
            recyclerViewTrack.visibility = View.GONE
        }
        titleHistory = findViewById(R.id.history_title)
        errorIc = findViewById(R.id.error_search)
        errorText = findViewById(R.id.error_connectivity)
        error = findViewById(R.id.error)
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setText(savedValue)
        val recyclerView: RecyclerView = findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.adapter = trackAdapter
        val clearButton = findViewById<ImageView>(R.id.clear)
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            recyclerViewTrack.visibility = View.GONE
            clearButton.visibility = View.GONE
            errorIc.visibility=View.GONE
            errorText.visibility=View.GONE
            if (searchHistory.getList().isNotEmpty()){
                showState(StateType.HISTORY_LIST)
            } else {
                recyclerViewTrack.visibility = View.GONE
            }
            val inputMethodManager = getSystemService(InputMethodManager::class.java)
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getList().isNotEmpty() && hasFocus) showState(StateType.HISTORY_LIST)
        }
        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                clearButton.isVisible = isClearButtonVisible(s)
                if (searchEditText.hasFocus()
                    && s.isNullOrEmpty()
                    && searchHistory.getList().isNotEmpty()
                ) {
                    showState(StateType.HISTORY_LIST)
                } else {
                    showState(StateType.SEARCH_RESULT)
                    recyclerViewTrack.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (searchEditText.hasFocus() && searchHistory.getList().isNotEmpty()) {
                    showState(StateType.HISTORY_LIST)
                } else {
                    showState(StateType.SEARCH_RESULT)
                }
            }
        }
        searchEditText.addTextChangedListener(textWatcherEditText)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    searchTrackList()
                    Log.d("TrackTimeDebug", "Track Time: ${trackList}")
                } catch (e: Exception) {
                    Log.e("SearchError", "Error occurred in searchTrackList: ${e.message}", e) // у меня не работал поиск и я дебажил проблемы. Могу ли я оставлять это в пул реквесте как есть или надо будет потом чистить код? Мне кажется ловить эксепшен тут хорошая идея
                }
                true
            }
            false
        }
    }

    private fun clickOnTrack(track: Track) {
        searchHistory.addTrack(track)
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra(TRACK, Gson().toJson(track))
        showState(StateType.SEARCH_RESULT)
        startActivity(playerIntent)
    }
    private val searchWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val clearButton = findViewById<ImageView>(R.id.clear)
            clearButton.isVisible = isClearButtonVisible(s)
            savedValue = s.toString()
            if (searchEditText.hasFocus()
                && s.isNullOrEmpty()
                && searchHistory.getList().isNotEmpty()
            ) {
                showState(StateType.HISTORY_LIST)
            } else {
                showState(StateType.SEARCH_RESULT)
            }
        }
        override fun afterTextChanged(s: Editable?) {
            if (searchEditText.hasFocus() && searchHistory.getList().isNotEmpty()) {
                showState(StateType.HISTORY_LIST)
            } else {
                showState(StateType.SEARCH_RESULT)
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_VIEW_KEY, savedValue)
        super.onSaveInstanceState(outState)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = trackAdapter
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(searchWatcher)

        searchEditText.requestFocus()
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedValue = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY)
        searchEditText.setText(savedValue)
    }

    private fun isClearButtonVisible(s: CharSequence?): Boolean = !s.isNullOrEmpty()
    companion object {
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
        const val TRACK = "TRACK"
    }
    private fun searchTrackList() {
        if (searchEditText.text.isNotEmpty()) {
            itunesService.search(searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                Log.d("TrackTimeDebug", "Track Time: ${trackList}")
                                trackAdapter.notifyDataSetChanged()
                                showState(StateType.SEARCH_RESULT)
                            } else showState(StateType.NOT_FOUND)
                        } else showState(StateType.CONNECTION_ERROR)
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showState(StateType.CONNECTION_ERROR)
                    }
                })
        }
    }
    private fun showState(stateType: StateType) {
        when (stateType) {
            StateType.CONNECTION_ERROR -> {
                recyclerViewTrack.visibility = View.GONE
                error.visibility = View.VISIBLE
                refreshButt.visibility = View.VISIBLE

                errorIc.setImageResource(R.drawable.no_internet)
                errorText.setText(R.string.connectivity_issue)
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
            }

            StateType.NOT_FOUND -> {
                recyclerViewTrack.visibility = View.GONE
                error.visibility = View.VISIBLE
                refreshButt.visibility = View.GONE
                errorIc.setImageResource(R.drawable.sad_face)
                errorText.setText(R.string.not_found)
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE

            }

            StateType.SEARCH_RESULT -> {
                trackAdapter.track = trackList
                recyclerViewTrack.adapter = trackAdapter
                recyclerViewTrack.visibility = View.VISIBLE
                error.visibility = View.GONE
                refreshButt.visibility = View.GONE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
            }

            StateType.HISTORY_LIST -> {
                historyTrackAdapter.track = searchHistory.getList()
                recyclerViewTrack.adapter = historyTrackAdapter
                recyclerViewTrack.visibility = View.VISIBLE
                error.visibility = View.GONE
                refreshButt.visibility = View.GONE
                titleHistory.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
            }
        }
    }
}