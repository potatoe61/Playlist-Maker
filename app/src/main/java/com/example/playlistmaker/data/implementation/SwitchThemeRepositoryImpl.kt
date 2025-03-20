package com.example.playlistmaker.data.implementation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.repositories.SwitchThemeRepository
import androidx.core.content.edit

class SwitchThemeRepositoryImpl(private val sharedPreferences:SharedPreferences):SwitchThemeRepository {
    companion object {
        private const val THEME = "THEME"
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else  {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun sharedPreferencesEdit(checked : Boolean) {
        sharedPreferences.edit() {
            putBoolean(THEME, checked)
        }
    }

    override fun getSharedPreferencesThemeValue():Boolean{
        val darkTheme = sharedPreferences.getBoolean(THEME, false)
        return darkTheme
    }
}