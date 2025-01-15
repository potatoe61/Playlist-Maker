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
import android.util.Log;

class SearchActivity : AppCompatActivity() {
    private enum class StateType {
        CONNECTION_ERROR, NOT_FOUND, SEARCH_RESULT
    }
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
    private val trackAdapter = TrackAdapter(trackList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val inputMethodManager = getSystemService(InputMethodManager::class.java)
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                clearButton.isVisible = isClearButtonVisible(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    searchTrackList()
                } catch (e: Exception) {
                    Log.e("SearchError", "Error occurred in searchTrackList: ${e.message}", e) // у меня не работал поиск и я дебажил проблемы. Могу ли я оставлять это в пул реквесте как есть или надо будет потом чистить код? Мне кажется ловить эксепшен тут хорошая идея
                }
                true
            }
            false
        }

        searchEditText.addTextChangedListener(textWatcherEditText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_VIEW_KEY, savedValue)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedValue = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY)
        searchEditText.setText(savedValue)
    }

    private fun isClearButtonVisible(s: CharSequence?): Boolean = !s.isNullOrEmpty()
    companion object {
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
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
            }
            StateType.NOT_FOUND -> {
                recyclerViewTrack.visibility = View.GONE
                error.visibility = View.VISIBLE
                refreshButt.visibility = View.GONE
                errorIc.setImageResource(R.drawable.sad_face)
                errorText.setText(R.string.not_found)
            }
            StateType.SEARCH_RESULT -> {
                recyclerViewTrack.visibility = View.VISIBLE
                error.visibility = View.GONE
                refreshButt.visibility = View.GONE
            }
        }
    }
}