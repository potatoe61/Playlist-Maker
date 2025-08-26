package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.media.domain.FavoriteTrackRepository
import com.example.playlistmaker.player.data.repositories.MediaPlayerRepositoriesImpl
import com.example.playlistmaker.player.domain.repositories.MediaPlayerRepositories
import com.example.playlistmaker.search.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.setting.data.repositories.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.setting.data.repositories.SharingRepositoryImpl
import com.example.playlistmaker.setting.data.repositories.SwitchThemeRepositoryImpl
import com.example.playlistmaker.setting.domain.repositories.ExternalNavigatorRepository
import com.example.playlistmaker.setting.domain.repositories.SharingRepository
import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get(), get())
    }

    factory<MediaPlayerRepositories> {
        MediaPlayerRepositoriesImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

    single<SwitchThemeRepository> {
        SwitchThemeRepositoryImpl(get(named("theme")))
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(androidContext())
    }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get())
    }
}