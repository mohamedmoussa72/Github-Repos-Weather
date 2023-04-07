package com.robusta.data.repo

import com.robusta.data.model.WeatherResponse
import com.robusta.data.utile.Resource

interface WeatherRepository {
    suspend fun getWeatherResponse(lat : Double, lon : Double): Resource<WeatherResponse>

}