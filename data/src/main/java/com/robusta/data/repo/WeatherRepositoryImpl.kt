package com.robusta.data.repo

import com.robusta.data.model.WeatherResponse
import com.robusta.data.model.location.WeatherData
import com.robusta.data.repo.dataSource.WeatherLocaleDataSource
import com.robusta.data.repo.dataSource.WeatherRemoteDataSource
import com.robusta.data.utile.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class WeatherRepositoryImpl  @Inject constructor(private val weatherRemoteDataSource: WeatherRemoteDataSource
,private val weatherLocaleDataSource: WeatherLocaleDataSource):WeatherRepository {
    override suspend fun getWeatherResponse(lat: Double, lon: Double): Resource<WeatherResponse> {
       return responseToResource(weatherRemoteDataSource.getWeatherResponse(lat , lon))
    }

    override suspend fun saveWeather(weatherData: WeatherData) {
        weatherLocaleDataSource.saveWeatherToDB(weatherData)
    }

    override fun getSavedWeather(): Flow<List<WeatherData>> {
        return weatherLocaleDataSource.getSavedWeatherData()
    }

    private fun responseToResource(response:Response<WeatherResponse>): Resource<WeatherResponse> {
        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

}