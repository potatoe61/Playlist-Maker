package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "shared_preferences"
const val THEME = "theme"

class App: Application() {

    var dark = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        switchTheme(sharedPreferences.getBoolean(THEME, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        dark = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}