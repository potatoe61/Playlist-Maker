package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors


class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText

    private var savedValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        savedValue = savedInstanceState?.getString(EDIT_TEXT_VIEW_KEY)

        val backImage = findViewById<Button>(R.id.back)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setText(savedValue)
        val recyclerView: RecyclerView = findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        val trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter
        val clearButton = findViewById<ImageView>(R.id.clear)
        clearButton.setOnClickListener {
            searchEditText.text.clear()
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
}