package com.robusta.data.repo.dataSource

import com.robusta.data.model.WeatherResponse
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherResponse(lat : Double, lon : Double): Response<WeatherResponse>
}