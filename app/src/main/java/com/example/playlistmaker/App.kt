package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        val sharedPreferencesInteractor = Creator.provideSwitchThemeInteractor()
        val darkTheme = sharedPreferencesInteractor.getSharedPreferencesThemeValue()
        sharedPreferencesInteractor.switchTheme(darkTheme)
    }
}