package com.robusta.data.api

import com.robusta.data.BuildConfig
import com.robusta.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ApiService {
    @GET("weather")
    suspend fun getWeatherResponse(
        @Query("lat")
        lat:Double,
        @Query("lon")
        lon:Double,
        @Query("appid")
        apiKey:String = BuildConfig.API_KEY
    ): Response<WeatherResponse>
}