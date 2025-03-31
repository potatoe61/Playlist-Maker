package com.example.playlistmaker.di

import com.example.playlistmaker.media.presentation.viewmodel.MediaViewModel
import com.example.playlistmaker.media.presentation.viewmodel.TabFavoriteViewModel
import com.example.playlistmaker.media.presentation.viewmodel.TabMediaViewModel
import com.example.playlistmaker.player.presentation.viewModel.MediaPlayerViewModel
import com.example.playlistmaker.search.presentation.ViewModel.TrackSearchViewModel
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TrackSearchViewModel(get())
    }

    viewModel { (url: String) ->
        MediaPlayerViewModel(url, get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }
    viewModel{
        MediaViewModel()
    }
    viewModel{
        TabMediaViewModel()
    }
    viewModel{
        TabFavoriteViewModel()
    }
}