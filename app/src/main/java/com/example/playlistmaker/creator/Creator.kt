package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.setting.data.repositories.SwitchThemeRepositoryImpl
import com.example.playlistmaker.setting.domain.interactors.SwitchThemeInteractorImpl
import com.example.playlistmaker.search.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.player.domain.interactors.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.repositories.MediaPlayerRepositoriesImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.setting.domain.repositories.ExternalNavigatorRepository
import com.example.playlistmaker.setting.data.repositories.SharingRepositoryImpl
import com.example.playlistmaker.setting.data.repositories.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.interactors.SharingInteractorImpl
import com.example.playlistmaker.setting.domain.repositories.SharingRepository
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient

object Creator {
    private lateinit var application: Application
    const val SHARED_PREFERENCES = "shared_preferences"


    fun initApplication(application: Application){
        Creator.application = application
    }


    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(application), SearchHistoryRepositoryImpl(
                application.getSharedPreferences(
                    SHARED_PREFERENCES, MODE_PRIVATE
                )
            )
        )
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun provideSharedPreferences(key:String): SharedPreferences {
        val keyPref = key
        return application.getSharedPreferences(keyPref, Context.MODE_PRIVATE)
    }

    fun provideSwitchThemeInteractor() : SwitchThemeInteractor {
        return SwitchThemeInteractorImpl(provideSwitchThemeRepository())
    }

    fun provideSwitchThemeRepository() : SwitchThemeRepository {
        return SwitchThemeRepositoryImpl()
    }


    fun provideSharingInteractor() : SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(), provideExternalNavigator())
    }
    fun provideSharingRepository():SharingRepository {
        return SharingRepositoryImpl(application)
    }
    fun provideExternalNavigator(): ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl(application)
    }
    fun provideMediaPlayerInteractor():MediaPlayerInteractor{
        return  MediaPlayerInteractorImpl(MediaPlayerRepositories())
    }
    fun MediaPlayerRepositories():MediaPlayerRepositories{
        return MediaPlayerRepositoriesImpl()
    }


}