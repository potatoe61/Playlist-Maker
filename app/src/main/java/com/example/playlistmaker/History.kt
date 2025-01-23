package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class History(val sharedPreferences: SharedPreferences) {

    companion object {
        const val HISTORY = "HISTORY"
    }
    private fun saveList(historyTracks: ArrayList<Track>) {
        val i = Gson().toJson(historyTracks)
        sharedPreferences.edit()
            .putString(HISTORY, i)
            .apply()
    }
    fun addTrack(track: Track) {
        val history = getList()
        history.remove(track)
        history.add(0, track)
        if (history.size > 10) history.removeLast()
        saveList(history)
    }
    fun getList(): ArrayList<Track> {
        val JSON = sharedPreferences.getString(HISTORY, "")
        return if (JSON.isNullOrBlank()) {
            arrayListOf()
        } else {
            Gson().fromJson(JSON, object : TypeToken<ArrayList<Track>>() {}.type)
        }
    }
    fun clearList() = sharedPreferences.edit()
        .remove(HISTORY)
        .apply()
}
