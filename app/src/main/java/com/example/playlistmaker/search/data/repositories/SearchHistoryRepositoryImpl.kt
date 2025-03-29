package com.example.playlistmaker.search.data.repositories

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.google.gson.Gson


const val TRACK_HISTORY_KEY = "key_for_history_search"
const val SEARCH_HISTORY_PREFERENCES = "search_history"

class SearchHistoryRepositoryImpl(private val gson: Gson, private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {

    companion object {
        const val MAX_LIMIT_SONGS = 10
    }

    override fun saveTrackToHistory(tracks: ArrayList<TrackDto>) {
        sharedPreferences.edit()
            .putString(TRACK_HISTORY_KEY, gson.toJson(tracks))
            .apply()
    }

    override fun getHistoryTrack(): List<TrackDto> {
        val json = sharedPreferences.getString(TRACK_HISTORY_KEY, null) ?: return ArrayList()
        val array = gson.fromJson(json, Array<TrackDto>::class.java)
        return ArrayList(array.toList())
    }

    override fun addTrackToHistory(track: TrackDto) {
        val history = getHistoryTrack().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > MAX_LIMIT_SONGS) {
            history.removeAt(history.lastIndex)
        }

        saveTrackToHistory(ArrayList(history))
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    override fun registerChangeListener(onChange: () -> Unit) {
        val listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACK_HISTORY_KEY) {
                onChange()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
}