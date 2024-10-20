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
import com.google.android.material.color.MaterialColors


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "SearchActivity"
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
    }

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

        val clearButton = findViewById<ImageView>(R.id.clear)
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            val inputMethodManager = getSystemService(InputMethodManager::class.java)

        }
        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                Log.i(LOG_TAG, "Введеное значение: $savedValue")
                clearButton.isVisible = isClearButtonVisible(s)
            }

            override fun afterTextChanged(s: Editable?) {}
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
}