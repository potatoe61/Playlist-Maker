package com.example.playlistmaker.setting.data.repositories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository

class SwitchThemeRepositoryImpl(private val sharedPref:SharedPreferences):
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
        sharedPref.edit()
            .putBoolean(THEME,checked)
            .apply()
    }

    override fun getSharedPreferencesThemeValue():Boolean{
        val darkTheme = sharedPref.getBoolean(THEME,false)
        return darkTheme
    }
}