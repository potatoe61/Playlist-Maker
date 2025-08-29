package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.FavoriteTrackInteractor
import com.example.playlistmaker.media.domain.FavoriteTrackInteractorImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.interactors.MediaPlayerInteractorImpl
import com.example.playlistmaker.playlist.PlaylistInteractor
import com.example.playlistmaker.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.interactors.TrackInteractorImpl
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor
import com.example.playlistmaker.setting.domain.interactors.SharingInteractorImpl
import com.example.playlistmaker.setting.domain.interactors.SwitchThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    factory <MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory< SwitchThemeInteractor> {
        SwitchThemeInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(),get())
    }
    factory<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }
    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}
