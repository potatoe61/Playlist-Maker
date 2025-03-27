package com.example.playlistmaker.menu.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.media.presentation.MediatekaActivity
import com.example.playlistmaker.search.presentation.SearchActivity
import com.example.playlistmaker.setting.presentation.SettingsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search)

        search.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }
        val mediateka = findViewById<Button>(R.id.mediateka)

        mediateka.setOnClickListener {
            val displayIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(displayIntent)
        }
        val settings = findViewById<Button>(R.id.settings)

        settings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}