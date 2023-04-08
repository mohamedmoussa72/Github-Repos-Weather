package com.robusta.weatherapp.repo

import com.robusta.data.model.WeatherResponse
import com.robusta.data.model.location.WeatherData
import com.robusta.data.repo.WeatherRepository
import com.robusta.data.utile.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.zip
import java.lang.reflect.Array.set

class FakeWeatherRepo: WeatherRepository {
    private val weatherList = mutableListOf<WeatherData>()
    val weatherListFlow = MutableStateFlow<List<WeatherData>>(emptyList())

    init {
        weatherList.add(WeatherData( "overview1", "133", "date1", "title1","title1","title1","image1"))

    }


    override suspend fun getWeatherResponse(lat: Double, lon: Double): Resource<WeatherResponse>? {
       return null
    }

    override suspend fun saveWeather(weatherData: WeatherData) {
    }

    override fun getSavedWeather(): Flow<List<WeatherData>> {

            weatherList.map {
                weatherListFlow.value = listOf(it)
            }
        return weatherListFlow
    }
}