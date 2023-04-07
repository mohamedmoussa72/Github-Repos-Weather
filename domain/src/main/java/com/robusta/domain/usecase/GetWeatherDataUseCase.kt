package com.robusta.domain.usecase

import com.robusta.data.model.WeatherResponse
import com.robusta.data.repo.WeatherRepository
import com.robusta.data.utile.Resource
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor (private val weatherRepository: WeatherRepository) {
    suspend fun execute(lat : Double, lon : Double): Resource<WeatherResponse>{
        return weatherRepository.getWeatherResponse(lat,lon)
    }
}