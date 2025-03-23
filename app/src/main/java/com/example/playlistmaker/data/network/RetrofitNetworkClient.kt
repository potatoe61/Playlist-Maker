package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesAPI = retrofit.create(SearchTrackApi::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            try {
                val resp = itunesAPI.search(dto.exception).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
                return body.apply { resultCode = resp.code() }
            }  catch (e: Exception) {
                Response().apply {
                    resultCode = 500
                }
            }
        }
        return Response().apply { resultCode = 400 }
    }
}