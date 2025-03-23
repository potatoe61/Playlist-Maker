package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.implementation.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.implementation.SwitchThemeRepositoryImpl
import com.example.playlistmaker.data.implementation.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.interactors.HistoryInteractorImpl
import com.example.playlistmaker.domain.interactors.SwitchThemeInteractorImpl
import com.example.playlistmaker.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.domain.repositories.SwitchThemeRepository
import com.example.playlistmaker.domain.repositories.TrackRepository

object Creator {
    private lateinit var application: Application
    const val SHARED_PREFERENCES = "shared_preferences"

    fun initApplication(application: Application){
        this.application = application
    }
    private  fun provideTrackRepository (): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }
    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(provideTrackRepository())
    }

    fun provideSharedPreferences(key:String): SharedPreferences {
        val keyPref = key
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }

    fun provideHistoryRepository() : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSharedPreferences(SHARED_PREFERENCES))
    }
    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(provideHistoryRepository())
    }

    fun provideSwitchThemeInteractor() : SwitchThemeInteractor {
        return SwitchThemeInteractorImpl(provideSwitchThemeRepository())
    }

    fun provideSwitchThemeRepository() : SwitchThemeRepository {
        return SwitchThemeRepositoryImpl(provideSharedPreferences(SHARED_PREFERENCES))
    }


}