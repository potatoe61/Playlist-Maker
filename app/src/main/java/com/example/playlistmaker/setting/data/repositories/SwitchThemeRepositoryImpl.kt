package com.example.playlistmaker.setting.data.repositories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository

class SwitchThemeRepositoryImpl():
    SwitchThemeRepository {
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
        val sharedPref = Creator.provideSharedPreferences(THEME)
        sharedPref.edit()
            .putBoolean(THEME,checked)
            .apply()
    }

    override fun getSharedPreferencesThemeValue():Boolean{
        val sharedPref = Creator.provideSharedPreferences(THEME)
        val darkTheme = sharedPref.getBoolean(THEME,false)
        return darkTheme
    }
}