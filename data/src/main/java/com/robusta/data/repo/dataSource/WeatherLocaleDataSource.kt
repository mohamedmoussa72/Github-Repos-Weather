package com.robusta.data.repo.dataSource

import com.robusta.data.model.location.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherLocaleDataSource {
    suspend fun saveWeatherToDB(weatherData: WeatherData)
    fun getSavedWeatherData(): Flow<List<WeatherData>>
}