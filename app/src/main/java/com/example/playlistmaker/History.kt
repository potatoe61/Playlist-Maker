package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class History(val sharedPreferences: SharedPreferences) {


    private fun saveList(historyTracks: ArrayList<Track>) {
        val saveList = Gson().toJson(historyTracks)
        sharedPreferences.edit()
            .putString(HISTORY, saveList)
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
        val json = sharedPreferences.getString(HISTORY, "")
        return if (json.isNullOrBlank()) {
            arrayListOf()
        } else {
            Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        }
    }
    fun clearList() = sharedPreferences.edit()
        .remove(HISTORY)
        .apply()
    companion object {
        const val HISTORY = "history"
    }
}
