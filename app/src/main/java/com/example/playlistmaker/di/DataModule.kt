package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.network.SearchTrackApi
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.repositories.SEARCH_HISTORY_PREFERENCES
import com.example.playlistmaker.search.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<SearchTrackApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTrackApi::class.java)
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(named("search_history_prefs")))
    }

    factory { Gson() }

    single(named("search_history_prefs")) {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    single(named("theme")) {
        val THEME = "THEME"
        androidContext()
            .getSharedPreferences(THEME, Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.playlistmaker")
            .build()
    }
}