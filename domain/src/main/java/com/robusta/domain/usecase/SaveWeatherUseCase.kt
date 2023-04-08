package com.robusta.domain.usecase

import com.robusta.data.model.location.WeatherData
import com.robusta.data.repo.WeatherRepository
import javax.inject.Inject

class SaveWeatherUseCase  @Inject constructor (private val weatherRepository: WeatherRepository) {
    suspend fun execute(weatherData: WeatherData)=weatherRepository.saveWeather(weatherData)

}