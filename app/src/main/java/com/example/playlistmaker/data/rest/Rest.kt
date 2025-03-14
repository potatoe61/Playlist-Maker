package com.example.playlistmaker.data.rest

import com.example.playlistmaker.data.dr.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Rest {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}