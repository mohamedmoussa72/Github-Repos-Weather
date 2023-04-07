package com.robusta.data.repo.dataSourceImpl

import com.robusta.data.api.ApiService
import com.robusta.data.model.WeatherResponse
import com.robusta.data.repo.dataSource.WeatherRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService): WeatherRemoteDataSource {
    override suspend fun getWeatherResponse(lat: Double, lon: Double): Response<WeatherResponse> {
        return apiService.getWeatherResponse(lat,lon)
    }
}