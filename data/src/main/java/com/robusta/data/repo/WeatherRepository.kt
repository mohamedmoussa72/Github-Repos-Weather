package com.robusta.data.repo

import com.robusta.data.locale.WeatherDataBase
import com.robusta.data.model.WeatherResponse
import com.robusta.data.model.location.WeatherData
import com.robusta.data.utile.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherResponse(lat : Double, lon : Double): Resource<WeatherResponse>?
    suspend fun saveWeather(weatherData: WeatherData)
    fun getSavedWeather(): Flow<List<WeatherData>>
}