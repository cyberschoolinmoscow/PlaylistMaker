package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IMDbApiService {
    @GET("/search?entity=song")
    fun findTrack(@Query("term") text: String): Call<TrackResponse>
}
