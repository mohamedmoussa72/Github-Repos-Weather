package com.robusta.domain.usecase

import com.robusta.data.model.location.WeatherData
import com.robusta.data.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedWeatherUseCase @Inject constructor (private val weatherRepository: WeatherRepository) {
    fun execute(): Flow<List<WeatherData>> {
        return weatherRepository.getSavedWeather()
    }

}